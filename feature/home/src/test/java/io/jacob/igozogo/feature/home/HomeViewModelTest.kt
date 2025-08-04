package io.jacob.igozogo.feature.home

import app.cash.turbine.test
import io.jacob.igozogo.core.domain.usecase.FeedSection
import io.jacob.igozogo.core.domain.usecase.SyncAndGetFeedsUseCase
import io.jacob.igozogo.core.testing.data.categoryTestData
import io.jacob.igozogo.core.testing.data.placeTestData
import io.jacob.igozogo.core.testing.data.storyTestData
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val syncAndGetFeedsUseCase = mockk<SyncAndGetFeedsUseCase>()

    private val testFeedSections = listOf(
        FeedSection.Categories(categoryTestData),
        FeedSection.Places(placeTestData),
        FeedSection.Stories(storyTestData)
    )

    @Test
    fun `given syncAndGetFeedsUseCase succeeds when homeViewModel is initialized then loading and success states are emitted`() =
        runTest {
            val callbackSlot = slot<() -> Unit>()
            coEvery {
                syncAndGetFeedsUseCase.invoke(capture(callbackSlot))
            } coAnswers {
                callbackSlot.captured.invoke()
                testFeedSections
            }

            val viewModel = HomeViewModel(syncAndGetFeedsUseCase)
            advanceUntilIdle() // Wait for all coroutines to complete

            viewModel.state.test {
                // The initial value is already Loading, and after loadFeeds() it becomes Success
                val currentState = awaitItem()
                assertTrue(
                    "State should be Success after loading",
                    currentState is HomeState.Success
                )
                assertEquals(
                    "Feed sections should match",
                    testFeedSections,
                    (currentState as HomeState.Success).feedSections
                )
            }
        }

    @Test
    fun `given syncAndGetFeedsUseCase succeeds when homeViewModel is initialized then synced effect is emitted`() =
        runTest {
            val callbackSlot = slot<() -> Unit>()
            var capturedCallback: (() -> Unit)? = null

            coEvery {
                syncAndGetFeedsUseCase.invoke(capture(callbackSlot))
            } coAnswers {
                capturedCallback = callbackSlot.captured
                testFeedSections
            }

            val viewModel = HomeViewModel(syncAndGetFeedsUseCase)
            advanceUntilIdle() // Wait for viewModel initialization

            // Now invoke the callback and test for the effect
            viewModel.effect.test {
                capturedCallback?.invoke()
                advanceUntilIdle()
                val effect = awaitItem()
                assertEquals("Should emit Synced effect", HomeEffect.Synced, effect)
            }
        }

    @Test
    fun `given homeViewModel initialized when sendAction with ClickCategory then NavigateToCategoryDetails effect is emitted`() =
        runTest {
            // Use a callback that doesn't invoke to avoid initial Synced effect
            coEvery {
                syncAndGetFeedsUseCase.invoke(any())
            } returns testFeedSections

            val viewModel = HomeViewModel(syncAndGetFeedsUseCase)
            advanceUntilIdle()
            val testCategory = "Test Category"

            viewModel.effect.test {
                viewModel.sendAction(HomeAction.ClickCategory(testCategory))

                val effect = awaitItem()
                assertEquals(
                    "Should emit NavigateToCategoryDetails",
                    HomeEffect.NavigateToCategoryDetails(testCategory), effect
                )
            }
        }

    @Test
    fun `given homeViewModel initialized when sendAction with ClickPlace then NavigateToPlaceDetails effect is emitted`() =
        runTest {
            coEvery {
                syncAndGetFeedsUseCase.invoke(any())
            } returns testFeedSections

            val viewModel = HomeViewModel(syncAndGetFeedsUseCase)
            advanceUntilIdle()
            val testPlace = placeTestData.first()

            viewModel.effect.test {
                viewModel.sendAction(HomeAction.ClickPlace(testPlace))

                val effect = awaitItem()
                assertEquals(
                    "Should emit NavigateToPlaceDetails",
                    HomeEffect.NavigateToPlaceDetails(testPlace), effect
                )
            }
        }

    @Test
    fun `given homeViewModel initialized when sendAction with ClickStory then NavigateToStoryDetails effect is emitted`() =
        runTest {
            coEvery {
                syncAndGetFeedsUseCase.invoke(any())
            } returns testFeedSections

            val viewModel = HomeViewModel(syncAndGetFeedsUseCase)
            advanceUntilIdle()
            val testStory = storyTestData.first()

            viewModel.effect.test {
                viewModel.sendAction(HomeAction.ClickStory(testStory))

                val effect = awaitItem()
                assertEquals(
                    "Should emit NavigateToStoryDetails",
                    HomeEffect.NavigateToStoryDetails(testStory), effect
                )
            }
        }

    @Test
    fun `given homeViewModel initialized when multiple actions are sent then corresponding effects are emitted in order`() =
        runTest {
            coEvery {
                syncAndGetFeedsUseCase.invoke(any())
            } returns testFeedSections

            val viewModel = HomeViewModel(syncAndGetFeedsUseCase)
            advanceUntilIdle()
            val testCategory = "Test Category"
            val testPlace = placeTestData.first()
            val testStory = storyTestData.first()

            viewModel.effect.test {
                // Send multiple actions
                viewModel.sendAction(HomeAction.ClickCategory(testCategory))
                viewModel.sendAction(HomeAction.ClickPlace(testPlace))
                viewModel.sendAction(HomeAction.ClickStory(testStory))

                val effect1 = awaitItem()
                assertEquals(
                    "First effect should be NavigateToCategoryDetails",
                    HomeEffect.NavigateToCategoryDetails(testCategory), effect1
                )

                val effect2 = awaitItem()
                assertEquals(
                    "Second effect should be NavigateToPlaceDetails",
                    HomeEffect.NavigateToPlaceDetails(testPlace), effect2
                )

                val effect3 = awaitItem()
                assertEquals(
                    "Third effect should be NavigateToStoryDetails",
                    HomeEffect.NavigateToStoryDetails(testStory), effect3
                )
            }
        }

    @Test
    fun `given syncAndGetFeedsUseCase returns empty feeds when homeViewModel is initialized then success state with empty list is emitted`() =
        runTest {
            val callbackSlot = slot<() -> Unit>()
            coEvery {
                syncAndGetFeedsUseCase.invoke(capture(callbackSlot))
            } coAnswers {
                callbackSlot.captured.invoke()
                emptyList<FeedSection>()
            }

            val viewModel = HomeViewModel(syncAndGetFeedsUseCase)
            advanceUntilIdle()

            viewModel.state.test {
                val currentState = awaitItem()
                assertTrue("State should be Success", currentState is HomeState.Success)
                assertTrue(
                    "Feed sections should be empty",
                    (currentState as HomeState.Success).feedSections.isEmpty()
                )
            }
        }

    @Test
    fun `given syncAndGetFeedsUseCase callback not invoked when homeViewModel is initialized then synced effect is not emitted`() =
        runTest {
            coEvery {
                syncAndGetFeedsUseCase.invoke(any())
            } returns testFeedSections // Return feeds but don't invoke callback

            val viewModel = HomeViewModel(syncAndGetFeedsUseCase)
            advanceUntilIdle()

            // State should still work (callback is only for effect)
            viewModel.state.test {
                val currentState = awaitItem()
                assertTrue("State should be Success", currentState is HomeState.Success)
                assertEquals(
                    "Feed sections should match",
                    testFeedSections,
                    (currentState as HomeState.Success).feedSections
                )
            }

            // No effects should be emitted since callback wasn't called
            viewModel.effect.test {
                expectNoEvents()
            }
        }

    @Test
    fun `given different feed section types when homeViewModel is initialized then all sections are properly mapped`() =
        runTest {
            val mixedSections = listOf(
                FeedSection.Categories(listOf("Category1", "Category2")),
                FeedSection.Places(listOf(placeTestData.first())),
                FeedSection.Stories(listOf(storyTestData.last()))
            )

            val callbackSlot = slot<() -> Unit>()
            coEvery {
                syncAndGetFeedsUseCase.invoke(capture(callbackSlot))
            } coAnswers {
                callbackSlot.captured.invoke()
                mixedSections
            }

            val viewModel = HomeViewModel(syncAndGetFeedsUseCase)
            advanceUntilIdle()

            viewModel.state.test {
                val currentState = awaitItem()
                assertTrue("State should be Success", currentState is HomeState.Success)
                assertEquals(
                    "Feed sections should match mixed sections",
                    mixedSections, (currentState as HomeState.Success).feedSections
                )
            }
        }

    @Test
    fun `given syncAndGetFeedsUseCase throws exception when homeViewModel is initialized then error state is emitted`() =
        runTest {
            coEvery {
                syncAndGetFeedsUseCase.invoke(any())
            } throws RuntimeException("Network error")

            val viewModel = HomeViewModel(syncAndGetFeedsUseCase)
            advanceUntilIdle()

            viewModel.state.test {
                val currentState = awaitItem()
                assertTrue("State should be Error after exception", currentState is HomeState.Error)
            }
        }
}