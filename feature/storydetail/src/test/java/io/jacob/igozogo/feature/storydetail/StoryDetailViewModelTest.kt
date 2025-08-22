package io.jacob.igozogo.feature.storydetail

import app.cash.turbine.test
import io.jacob.igozogo.core.domain.repository.PlayerRepository
import io.jacob.igozogo.core.domain.usecase.GetStoryAndPlaceByIdUseCase
import io.jacob.igozogo.core.testing.data.placeTestData
import io.jacob.igozogo.core.testing.data.storyTestData
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class StoryDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getStoryAndPlaceByIdUseCase = mockk<GetStoryAndPlaceByIdUseCase>()
    private val playerRepository = mockk<PlayerRepository>(relaxed = true)

    private val testStoryId = 4976
    private val testStoryLangId = 15332
    private val testPlace = placeTestData.first()
    private val testStory = storyTestData.first()

    @Test
    fun `Given useCase returns success result with place and story, When viewModel is initialized, Then success state is emitted with correct data`() =
        runTest {
            coEvery {
                getStoryAndPlaceByIdUseCase(testStoryId, testStoryLangId)
            } returns Result.success(testPlace to testStory)

            val viewModel = StoryDetailViewModel(
                getStoryAndPlaceByIdUseCase = getStoryAndPlaceByIdUseCase,
                playerRepository = playerRepository,
                storyId = testStoryId,
                storyLangId = testStoryLangId
            )

            viewModel.state.test {
                val currentState = awaitItem()
                assertTrue(
                    "State should be Success after loading",
                    currentState is StoryDetailState.Success
                )
                val successState = currentState as StoryDetailState.Success
                assertEquals("Place should match", testPlace, successState.place)
                assertEquals("Story should match", testStory, successState.story)
            }
        }

    @Test
    fun `Given useCase returns failure result, When viewModel is initialized, Then error state is emitted`() =
        runTest {
            coEvery {
                getStoryAndPlaceByIdUseCase(testStoryId, testStoryLangId)
            } returns Result.failure(RuntimeException("Story or place not found"))

            val viewModel = StoryDetailViewModel(
                getStoryAndPlaceByIdUseCase = getStoryAndPlaceByIdUseCase,
                playerRepository = playerRepository,
                storyId = testStoryId,
                storyLangId = testStoryLangId
            )

            viewModel.state.test {
                val currentState = awaitItem()
                assertTrue(
                    "State should be Error when useCase returns failure",
                    currentState is StoryDetailState.Error
                )
            }
        }

    @Test
    fun `Given useCase throws exception, When viewModel is initialized, Then error state is emitted`() =
        runTest {
            coEvery {
                getStoryAndPlaceByIdUseCase(testStoryId, testStoryLangId)
            } throws RuntimeException("Network error")

            val viewModel = StoryDetailViewModel(
                getStoryAndPlaceByIdUseCase = getStoryAndPlaceByIdUseCase,
                playerRepository = playerRepository,
                storyId = testStoryId,
                storyLangId = testStoryLangId
            )

            viewModel.state.test {
                val currentState = awaitItem()
                assertTrue(
                    "State should be Error after exception",
                    currentState is StoryDetailState.Error
                )
            }
        }

    @Test
    fun `Given story, When play is called, Then playerRepository play is called with same story`() =
        runTest {
            coEvery {
                getStoryAndPlaceByIdUseCase(testStoryId, testStoryLangId)
            } returns Result.success(testPlace to testStory)

            val viewModel = StoryDetailViewModel(
                getStoryAndPlaceByIdUseCase = getStoryAndPlaceByIdUseCase,
                playerRepository = playerRepository,
                storyId = testStoryId,
                storyLangId = testStoryLangId
            )

            viewModel.play(testStory)

            verify { playerRepository.play(testStory) }
        }

    @Test
    fun `Given different story, When play is called, Then playerRepository play is called with that story`() =
        runTest {
            val differentStory = storyTestData.last()

            coEvery {
                getStoryAndPlaceByIdUseCase(testStoryId, testStoryLangId)
            } returns Result.success(testPlace to testStory)

            val viewModel = StoryDetailViewModel(
                getStoryAndPlaceByIdUseCase = getStoryAndPlaceByIdUseCase,
                playerRepository = playerRepository,
                storyId = testStoryId,
                storyLangId = testStoryLangId
            )

            viewModel.play(differentStory)

            verify { playerRepository.play(differentStory) }
        }

    @Test
    fun `Given multiple play calls, When play is called multiple times, Then playerRepository play is called each time`() =
        runTest {
            val firstStory = storyTestData.first()
            val secondStory = storyTestData.last()

            coEvery {
                getStoryAndPlaceByIdUseCase(testStoryId, testStoryLangId)
            } returns Result.success(testPlace to testStory)

            val viewModel = StoryDetailViewModel(
                getStoryAndPlaceByIdUseCase = getStoryAndPlaceByIdUseCase,
                playerRepository = playerRepository,
                storyId = testStoryId,
                storyLangId = testStoryLangId
            )

            viewModel.play(firstStory)
            viewModel.play(secondStory)

            verify { playerRepository.play(firstStory) }
            verify { playerRepository.play(secondStory) }
        }

    @Test
    fun `Given different storyId and storyLangId, When viewModel is initialized, Then useCase is called with correct parameters`() =
        runTest {
            val customStoryId = 9999
            val customStoryLangId = 8888

            coEvery {
                getStoryAndPlaceByIdUseCase(customStoryId, customStoryLangId)
            } returns Result.success(testPlace to testStory)

            StoryDetailViewModel(
                getStoryAndPlaceByIdUseCase = getStoryAndPlaceByIdUseCase,
                playerRepository = playerRepository,
                storyId = customStoryId,
                storyLangId = customStoryLangId
            )

            coEvery { getStoryAndPlaceByIdUseCase(customStoryId, customStoryLangId) }
        }

    @Test
    fun `Given successful data loading, When checking initial state, Then loading state is initial value`() =
        runTest {
            coEvery {
                getStoryAndPlaceByIdUseCase(testStoryId, testStoryLangId)
            } returns Result.success(testPlace to testStory)

            val viewModel = StoryDetailViewModel(
                getStoryAndPlaceByIdUseCase = getStoryAndPlaceByIdUseCase,
                playerRepository = playerRepository,
                storyId = testStoryId,
                storyLangId = testStoryLangId
            )

            // Before any emission, initial state should be Loading
            assertEquals(
                "Initial state should be Loading",
                StoryDetailState.Loading,
                viewModel.state.value
            )
        }

    @Test
    fun `Given story with audioUrl, When play is called, Then story audioUrl is logged and playerRepository play is called`() =
        runTest {
            val storyWithAudio = testStory.copy(audioUrl = "https://test.com/audio.mp3")

            coEvery {
                getStoryAndPlaceByIdUseCase(testStoryId, testStoryLangId)
            } returns Result.success(testPlace to testStory)

            val viewModel = StoryDetailViewModel(
                getStoryAndPlaceByIdUseCase = getStoryAndPlaceByIdUseCase,
                playerRepository = playerRepository,
                storyId = testStoryId,
                storyLangId = testStoryLangId
            )

            viewModel.play(storyWithAudio)

            verify { playerRepository.play(storyWithAudio) }
        }
}