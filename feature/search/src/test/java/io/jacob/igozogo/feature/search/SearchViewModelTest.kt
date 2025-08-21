package io.jacob.igozogo.feature.search

import app.cash.turbine.test
import io.jacob.igozogo.core.domain.repository.RecentSearchRepository
import io.jacob.igozogo.core.domain.usecase.GetCategoriesUseCase
import io.jacob.igozogo.core.domain.usecase.GetRecentSearchesUseCase
import io.jacob.igozogo.core.domain.usecase.SearchKeywordUseCase
import io.jacob.igozogo.core.model.SearchResult
import io.jacob.igozogo.core.testing.data.categoryTestData
import io.jacob.igozogo.core.testing.data.placeTestData
import io.jacob.igozogo.core.testing.data.recentSearchTestData
import io.jacob.igozogo.core.testing.data.storyTestData
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCategoriesUseCase = mockk<GetCategoriesUseCase>()
    private val getRecentSearchesUseCase = mockk<GetRecentSearchesUseCase>()
    private val searchKeywordUseCase = mockk<SearchKeywordUseCase>()
    private val recentSearchRepository = mockk<RecentSearchRepository>()

    private fun createViewModel(): SearchViewModel {
        return SearchViewModel(
            getCategoriesUseCase = getCategoriesUseCase,
            getRecentSearchesUseCase = getRecentSearchesUseCase,
            searchKeywordUseCase = searchKeywordUseCase,
            recentSearchRepository = recentSearchRepository
        )
    }

    @Test
    fun `Given initial state, When viewModel is created, Then show categories display`() =
        runTest {
            // Given
            every { getCategoriesUseCase() } returns flowOf(categoryTestData)
            every { getRecentSearchesUseCase() } returns flowOf(recentSearchTestData)

            // When
            val viewModel = createViewModel()

            // Then
            viewModel.state.test {
                // Skip Loading state
                val loadingState = awaitItem()
                assertTrue("Initial state should be Loading", loadingState is SearchState.Loading)

                // Wait for actual state
                val state = awaitItem()
                assertTrue(
                    "State should be CategoriesDisplay",
                    state is SearchState.CategoriesDisplay
                )
                assertEquals(
                    "Categories should match test data",
                    categoryTestData, (state as SearchState.CategoriesDisplay).categories
                )
            }
        }

    @Test
    fun `Given search query changed, When sendAction QueryChanged, Then update searchQuery`() =
        runTest {
            // Given
            every { getCategoriesUseCase() } returns flowOf(categoryTestData)
            every { getRecentSearchesUseCase() } returns flowOf(recentSearchTestData)
            every { searchKeywordUseCase(any()) } returns flowOf(SearchResult())

            val viewModel = createViewModel()

            // When
            viewModel.sendAction(SearchAction.QueryChanged("서울"))

            // Then
            viewModel.searchQuery.test {
                val query = awaitItem()
                assertEquals("Search query should be updated", "서울", query)
            }

            viewModel.state.test {
                // Skip initial states and wait for the query to take effect
                skipItems(1) // Skip Loading
                val state = awaitItem()
                assertTrue(
                    "State should be Success after query change",
                    state is SearchState.Success
                )
            }
        }

    @Test
    fun `Given focus changed to true with empty query, When sendAction FocusChanged, Then show recent searches`() =
        runTest {
            // Given
            every { getCategoriesUseCase() } returns flowOf(categoryTestData)
            every { getRecentSearchesUseCase() } returns flowOf(recentSearchTestData)

            val viewModel = createViewModel()

            // When
            viewModel.sendAction(SearchAction.FocusChanged(true))

            // Then
            viewModel.state.test {
                skipItems(1) // Skip Loading state  
                val currentState = awaitItem()
                assertTrue(
                    "State should be RecentSearchesDisplay",
                    currentState is SearchState.RecentSearchesDisplay
                )
                assertEquals(
                    "Recent searches should match test data",
                    recentSearchTestData,
                    (currentState as SearchState.RecentSearchesDisplay).recentSearches
                )
            }
        }

    @Test
    fun `Given search performed, When sendAction ClickSearch, Then upsert to recent searches and update query`() =
        runTest {
            // Given
            every { getCategoriesUseCase() } returns flowOf(categoryTestData)
            every { getRecentSearchesUseCase() } returns flowOf(recentSearchTestData)
            every { searchKeywordUseCase(any()) } returns flowOf(SearchResult())
            coEvery { recentSearchRepository.upsertRecentSearch(any()) } returns Unit

            val viewModel = createViewModel()
            val searchQuery = "부산"

            // When
            viewModel.sendAction(SearchAction.ClickSearch(searchQuery))

            // Then
            coVerify { recentSearchRepository.upsertRecentSearch(searchQuery) }

            viewModel.searchQuery.test {
                val query = awaitItem()
                assertEquals("Search query should be updated", searchQuery, query)
            }

            viewModel.state.test {
                skipItems(1) // Skip Loading state
                val state = awaitItem()
                assertTrue("State should be Success after search", state is SearchState.Success)
            }
        }

    @Test
    fun `Given query with debounce, When search results available, Then show success state`() =
        runTest {
            // Given
            val searchResults = SearchResult(
                places = placeTestData.take(2),
                stories = storyTestData.take(2)
            )
            every { getCategoriesUseCase() } returns flowOf(categoryTestData)
            every { getRecentSearchesUseCase() } returns flowOf(recentSearchTestData)
            every { searchKeywordUseCase(any()) } returns flowOf(searchResults)

            val viewModel = createViewModel()

            // Then - Subscribe first to activate StateFlow
            viewModel.state.test {
                skipItems(1) // Skip Loading state
                val initialState = awaitItem() // CategoriesDisplay state
                assertTrue(
                    "Initial should be CategoriesDisplay",
                    initialState is SearchState.CategoriesDisplay
                )

                // When - Now perform action
                viewModel.sendAction(SearchAction.QueryChanged("서울"))

                // debounce 500ms 대기
                advanceTimeBy(501)
                verify { searchKeywordUseCase("서울") }

                val finalState = awaitItem()
                assertTrue("State should be Success", finalState is SearchState.Success)

                val successState = awaitItem() as SearchState.Success
                assertEquals("Places should match", searchResults.places, successState.places)
                assertEquals("Stories should match", searchResults.stories, successState.stories)
            }
        }

    @Test
    fun `Given empty search results, When search query provided, Then show empty success state`() =
        runTest {
            // Given
            val emptyResults = SearchResult()
            every { getCategoriesUseCase() } returns flowOf(categoryTestData)
            every { getRecentSearchesUseCase() } returns flowOf(recentSearchTestData)
            every { searchKeywordUseCase(any()) } returns flowOf(emptyResults)

            val viewModel = createViewModel()

            // Then - Subscribe first to activate StateFlow
            viewModel.state.test {
                skipItems(1) // Skip Loading state
                val initialState = awaitItem() // CategoriesDisplay state
                assertTrue(
                    "Initial should be CategoriesDisplay",
                    initialState is SearchState.CategoriesDisplay
                )

                // When - Now perform action
                viewModel.sendAction(SearchAction.QueryChanged("존재하지않는검색어"))

                // May receive multiple state changes during search
                var finalState = awaitItem()
                if (finalState !is SearchState.Success) {
                    finalState = awaitItem()
                }
                assertTrue("State should be Success", finalState is SearchState.Success)
                val successState = finalState as SearchState.Success
                assertTrue("Success state should be empty", successState.isEmpty)
                assertTrue("Places should be empty", successState.places.isEmpty())
                assertTrue("Stories should be empty", successState.stories.isEmpty())
            }
        }

    @Test
    fun `Given clear query action, When sendAction ClearQuery, Then reset query and focus`() =
        runTest {
            // Given
            every { getCategoriesUseCase() } returns flowOf(categoryTestData)
            every { getRecentSearchesUseCase() } returns flowOf(recentSearchTestData)
            every { searchKeywordUseCase(any()) } returns flowOf(SearchResult())

            val viewModel = createViewModel()

            // Then - Subscribe first and test the flow
            viewModel.state.test {
                skipItems(1) // Skip Loading state
                val initialState = awaitItem()
                assertTrue(
                    "Initial should be CategoriesDisplay",
                    initialState is SearchState.CategoriesDisplay
                )

                // Set initial query and focus
                viewModel.sendAction(SearchAction.QueryChanged("서울"))
                viewModel.sendAction(SearchAction.FocusChanged(true))

                // Skip intermediate states caused by query and focus changes
                skipItems(1) // Skip possible intermediate state

                // When
                viewModel.sendAction(SearchAction.ClearQuery)

                // Then
                assertEquals("Query should be empty", "", viewModel.searchQuery.value)

                val finalState = awaitItem()
                assertTrue(
                    "State should be CategoriesDisplay after clear",
                    finalState is SearchState.CategoriesDisplay
                )
            }
        }

    @Test
    fun `Given remove recent search action, When sendAction RemoveRecentSearch, Then call repository deleteRecentSearch`() =
        runTest {
            // Given
            every { getCategoriesUseCase() } returns flowOf(categoryTestData)
            every { getRecentSearchesUseCase() } returns flowOf(recentSearchTestData)
            coEvery { recentSearchRepository.deleteRecentSearch(any()) } returns Unit

            val viewModel = createViewModel()
            val queryToRemove = "삭제할검색어"

            // When
            viewModel.sendAction(SearchAction.RemoveRecentSearch(queryToRemove))

            // Then
            coVerify { recentSearchRepository.deleteRecentSearch(queryToRemove) }
        }

    @Test
    fun `Given clear recent searches action, When sendAction ClearRecentSearches, Then call repository clearRecentSearches`() =
        runTest {
            // Given
            every { getCategoriesUseCase() } returns flowOf(categoryTestData)
            every { getRecentSearchesUseCase() } returns flowOf(recentSearchTestData)
            coEvery { recentSearchRepository.clearRecentSearches() } returns Unit

            val viewModel = createViewModel()

            // When
            viewModel.sendAction(SearchAction.ClearRecentSearches)

            // Then
            coVerify { recentSearchRepository.clearRecentSearches() }
        }

    @Test
    fun `Given click category action, When sendAction ClickCategory, Then emit NavigateToCategoryDetails effect`() =
        runTest {
            // Given
            every { getCategoriesUseCase() } returns flowOf(categoryTestData)
            every { getRecentSearchesUseCase() } returns flowOf(recentSearchTestData)

            val viewModel = createViewModel()
            val category = "테스트카테고리"

            // When & Then
            viewModel.effect.test {
                viewModel.sendAction(SearchAction.ClickCategory(category))

                val effect = awaitItem()
                assertEquals(
                    "Should emit NavigateToCategoryDetails effect",
                    SearchEffect.NavigateToCategoryDetails(category), effect
                )
            }
        }

    @Test
    fun `Given click place action, When sendAction ClickPlace, Then emit NavigateToPlaceDetails effect`() =
        runTest {
            // Given
            every { getCategoriesUseCase() } returns flowOf(categoryTestData)
            every { getRecentSearchesUseCase() } returns flowOf(recentSearchTestData)

            val viewModel = createViewModel()
            val place = placeTestData.first()

            // When & Then
            viewModel.effect.test {
                viewModel.sendAction(SearchAction.ClickPlace(place))

                val effect = awaitItem()
                assertEquals(
                    "Should emit NavigateToPlaceDetails effect",
                    SearchEffect.NavigateToPlaceDetails(place), effect
                )
            }
        }

    @Test
    fun `Given click story action, When sendAction ClickStory, Then emit NavigateToStoryDetails effect`() =
        runTest {
            // Given
            every { getCategoriesUseCase() } returns flowOf(categoryTestData)
            every { getRecentSearchesUseCase() } returns flowOf(recentSearchTestData)

            val viewModel = createViewModel()
            val story = storyTestData.first()

            // When & Then
            viewModel.effect.test {
                viewModel.sendAction(SearchAction.ClickStory(story))

                val effect = awaitItem()
                assertEquals(
                    "Should emit NavigateToStoryDetails effect",
                    SearchEffect.NavigateToStoryDetails(story), effect
                )
            }
        }

    @Test
    fun `Given click recent search action, When sendAction ClickRecentSearch, Then perform search with query`() =
        runTest {
            // Given
            every { getCategoriesUseCase() } returns flowOf(categoryTestData)
            every { getRecentSearchesUseCase() } returns flowOf(recentSearchTestData)
            every { searchKeywordUseCase(any()) } returns flowOf(SearchResult())
            coEvery { recentSearchRepository.upsertRecentSearch(any()) } returns Unit

            val viewModel = createViewModel()
            val recentQuery = "최근검색어"

            // When
            viewModel.sendAction(SearchAction.ClickRecentSearch(recentQuery))

            // Then
            coVerify { recentSearchRepository.upsertRecentSearch(recentQuery) }
            viewModel.searchQuery.test {
                val query = awaitItem()
                assertEquals(
                    "Search query should be updated with recent search",
                    recentQuery,
                    query
                )
            }
        }
}