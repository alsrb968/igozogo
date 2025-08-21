package io.jacob.igozogo.core.domain.usecase

import app.cash.turbine.test
import io.jacob.igozogo.core.domain.repository.PlaceRepository
import io.jacob.igozogo.core.domain.repository.StoryRepository
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.SearchResult
import io.jacob.igozogo.core.model.Story
import io.jacob.igozogo.core.testing.data.placeTestData
import io.jacob.igozogo.core.testing.data.storyTestData
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SearchKeywordUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val placeRepository = mockk<PlaceRepository>()
    private val storyRepository = mockk<StoryRepository>()

    private val useCase = SearchKeywordUseCase(
        placeRepository = placeRepository,
        storyRepository = storyRepository
    )

    @Test
    fun `Given keyword and default size, When invoke, Then call both repositories and emit SearchResult`() =
        runTest {
            // Given
            val keyword = "서울"
            val expectedPlaces = placeTestData.take(3)
            val expectedStories = storyTestData.take(3)
            coEvery { placeRepository.getPlacesByKeyword(any(), any()) } returns expectedPlaces
            coEvery { storyRepository.getStoriesByKeyword(any(), any()) } returns expectedStories

            // When & Then
            useCase(keyword).test {
                val result = awaitItem()
                assertEquals(expectedPlaces, result.places)
                assertEquals(expectedStories, result.stories)
                awaitComplete()
            }

            coVerify { placeRepository.getPlacesByKeyword(keyword, 20) }
            coVerify { storyRepository.getStoriesByKeyword(keyword, 20) }
        }

    @Test
    fun `Given keyword and custom size, When invoke, Then call repositories with custom size`() =
        runTest {
            // Given
            val keyword = "부산"
            val customSize = 10
            val expectedPlaces = placeTestData.take(2)
            val expectedStories = storyTestData.take(2)
            coEvery { placeRepository.getPlacesByKeyword(any(), any()) } returns expectedPlaces
            coEvery { storyRepository.getStoriesByKeyword(any(), any()) } returns expectedStories

            // When & Then
            useCase(keyword, customSize).test {
                val result = awaitItem()
                assertEquals(expectedPlaces, result.places)
                assertEquals(expectedStories, result.stories)
                awaitComplete()
            }

            coVerify { placeRepository.getPlacesByKeyword(keyword, customSize) }
            coVerify { storyRepository.getStoriesByKeyword(keyword, customSize) }
        }

    @Test
    fun `Given repositories return empty results, When invoke, Then emit empty SearchResult`() =
        runTest {
            // Given
            val keyword = "존재하지않는키워드"
            coEvery { placeRepository.getPlacesByKeyword(any(), any()) } returns emptyList()
            coEvery { storyRepository.getStoriesByKeyword(any(), any()) } returns emptyList()

            // When & Then
            useCase(keyword).test {
                val result = awaitItem()
                assertEquals(SearchResult(), result)
                assertEquals(emptyList<Place>(), result.places)
                assertEquals(emptyList<Story>(), result.stories)
                awaitComplete()
            }

            coVerify { placeRepository.getPlacesByKeyword(keyword, 20) }
            coVerify { storyRepository.getStoriesByKeyword(keyword, 20) }
        }

    @Test
    fun `Given only places found, When invoke, Then emit SearchResult with places only`() =
        runTest {
            // Given
            val keyword = "장소만"
            val expectedPlaces = placeTestData.take(2)
            coEvery { placeRepository.getPlacesByKeyword(any(), any()) } returns expectedPlaces
            coEvery { storyRepository.getStoriesByKeyword(any(), any()) } returns emptyList()

            // When & Then
            useCase(keyword).test {
                val result = awaitItem()
                assertEquals(expectedPlaces, result.places)
                assertEquals(emptyList<Story>(), result.stories)
                awaitComplete()
            }

            coVerify { placeRepository.getPlacesByKeyword(keyword, 20) }
            coVerify { storyRepository.getStoriesByKeyword(keyword, 20) }
        }

    @Test
    fun `Given only stories found, When invoke, Then emit SearchResult with stories only`() =
        runTest {
            // Given
            val keyword = "스토리만"
            val expectedStories = storyTestData.take(2)
            coEvery { placeRepository.getPlacesByKeyword(any(), any()) } returns emptyList()
            coEvery { storyRepository.getStoriesByKeyword(any(), any()) } returns expectedStories

            // When & Then
            useCase(keyword).test {
                val result = awaitItem()
                assertEquals(emptyList<Place>(), result.places)
                assertEquals(expectedStories, result.stories)
                awaitComplete()
            }

            coVerify { placeRepository.getPlacesByKeyword(keyword, 20) }
            coVerify { storyRepository.getStoriesByKeyword(keyword, 20) }
        }
}