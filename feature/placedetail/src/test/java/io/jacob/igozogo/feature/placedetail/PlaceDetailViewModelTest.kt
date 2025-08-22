package io.jacob.igozogo.feature.placedetail

import app.cash.turbine.test
import io.jacob.igozogo.core.domain.repository.PlayerRepository
import io.jacob.igozogo.core.domain.usecase.GetPlaceAndStoriesByIdUseCase
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

class PlaceDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getPlaceAndStoriesByIdUseCase = mockk<GetPlaceAndStoriesByIdUseCase>()
    private val playerRepository = mockk<PlayerRepository>(relaxed = true)

    private val testPlaceId = 2812
    private val testPlaceLangId = 3826
    private val testPlace = placeTestData.first()
    private val testStories = storyTestData.take(3)

    @Test
    fun `Given getPlaceAndStoriesByIdUseCase returns success, When viewModel is initialized, Then success state is emitted with correct data`() =
        runTest {
            coEvery {
                getPlaceAndStoriesByIdUseCase(testPlaceId, testPlaceLangId)
            } returns Result.success(testPlace to testStories)

            val viewModel = PlaceDetailViewModel(
                getPlaceAndStoriesByIdUseCase = getPlaceAndStoriesByIdUseCase,
                playerRepository = playerRepository,
                placeId = testPlaceId,
                placeLangId = testPlaceLangId
            )

            viewModel.state.test {
                val currentState = awaitItem()
                assertTrue(
                    "State should be Success after loading",
                    currentState is PlaceDetailState.Success
                )
                val successState = currentState as PlaceDetailState.Success
                assertEquals("Place should match", testPlace, successState.place)
                assertEquals("Stories should match", testStories, successState.stories)
            }
        }

    @Test
    fun `Given getPlaceAndStoriesByIdUseCase returns failure, When viewModel is initialized, Then error state is emitted`() =
        runTest {
            coEvery {
                getPlaceAndStoriesByIdUseCase(testPlaceId, testPlaceLangId)
            } returns Result.failure(RuntimeException("Network error"))

            val viewModel = PlaceDetailViewModel(
                getPlaceAndStoriesByIdUseCase = getPlaceAndStoriesByIdUseCase,
                playerRepository = playerRepository,
                placeId = testPlaceId,
                placeLangId = testPlaceLangId
            )

            viewModel.state.test {
                val currentState = awaitItem()
                assertTrue(
                    "State should be Error after failure",
                    currentState is PlaceDetailState.Error
                )
            }
        }

    @Test
    fun `Given getPlaceAndStoriesByIdUseCase throws exception, When viewModel is initialized, Then error state is emitted`() =
        runTest {
            coEvery {
                getPlaceAndStoriesByIdUseCase(testPlaceId, testPlaceLangId)
            } throws RuntimeException("Unexpected error")

            val viewModel = PlaceDetailViewModel(
                getPlaceAndStoriesByIdUseCase = getPlaceAndStoriesByIdUseCase,
                playerRepository = playerRepository,
                placeId = testPlaceId,
                placeLangId = testPlaceLangId
            )

            viewModel.state.test {
                val currentState = awaitItem()
                assertTrue(
                    "State should be Error after exception",
                    currentState is PlaceDetailState.Error
                )
            }
        }

    @Test
    fun `Given empty stories, When play is called, Then playerRepository play is called with empty list`() =
        runTest {
            coEvery {
                getPlaceAndStoriesByIdUseCase(testPlaceId, testPlaceLangId)
            } returns Result.success(testPlace to emptyList())

            val viewModel = PlaceDetailViewModel(
                getPlaceAndStoriesByIdUseCase = getPlaceAndStoriesByIdUseCase,
                playerRepository = playerRepository,
                placeId = testPlaceId,
                placeLangId = testPlaceLangId
            )

            val emptyStories = emptyList<io.jacob.igozogo.core.model.Story>()
            viewModel.play(emptyStories)

            verify { playerRepository.play(emptyStories) }
        }

    @Test
    fun `Given stories list, When play is called, Then playerRepository play is called with same stories`() =
        runTest {
            coEvery {
                getPlaceAndStoriesByIdUseCase(testPlaceId, testPlaceLangId)
            } returns Result.success(testPlace to testStories)

            val viewModel = PlaceDetailViewModel(
                getPlaceAndStoriesByIdUseCase = getPlaceAndStoriesByIdUseCase,
                playerRepository = playerRepository,
                placeId = testPlaceId,
                placeLangId = testPlaceLangId
            )

            viewModel.play(testStories)

            verify { playerRepository.play(testStories) }
        }

    @Test
    fun `Given different placeId and placeLangId, When viewModel is initialized, Then useCase is called with correct parameters`() =
        runTest {
            val customPlaceId = 9999
            val customPlaceLangId = 8888

            coEvery {
                getPlaceAndStoriesByIdUseCase(customPlaceId, customPlaceLangId)
            } returns Result.success(testPlace to testStories)

            PlaceDetailViewModel(
                getPlaceAndStoriesByIdUseCase = getPlaceAndStoriesByIdUseCase,
                playerRepository = playerRepository,
                placeId = customPlaceId,
                placeLangId = customPlaceLangId
            )

            coEvery { getPlaceAndStoriesByIdUseCase(customPlaceId, customPlaceLangId) }
        }

    @Test
    fun `Given viewModel initialized with specific parameters, When accessing placeId and placeLangId, Then correct values are returned`() =
        runTest {
            coEvery {
                getPlaceAndStoriesByIdUseCase(testPlaceId, testPlaceLangId)
            } returns Result.success(testPlace to testStories)

            val viewModel = PlaceDetailViewModel(
                getPlaceAndStoriesByIdUseCase = getPlaceAndStoriesByIdUseCase,
                playerRepository = playerRepository,
                placeId = testPlaceId,
                placeLangId = testPlaceLangId
            )

            assertEquals("PlaceId should match", testPlaceId, viewModel.placeId)
            assertEquals("PlaceLangId should match", testPlaceLangId, viewModel.placeLangId)
        }

    @Test
    fun `Given successful data loading, When checking initial state, Then loading state is initial value`() =
        runTest {
            coEvery {
                getPlaceAndStoriesByIdUseCase(testPlaceId, testPlaceLangId)
            } returns Result.success(testPlace to testStories)

            val viewModel = PlaceDetailViewModel(
                getPlaceAndStoriesByIdUseCase = getPlaceAndStoriesByIdUseCase,
                playerRepository = playerRepository,
                placeId = testPlaceId,
                placeLangId = testPlaceLangId
            )

            // Before any emission, initial state should be Loading
            assertEquals(
                "Initial state should be Loading",
                PlaceDetailState.Loading,
                viewModel.state.value
            )
        }

    @Test
    fun `Given null place returned from repository, When viewModel is initialized, Then error state is emitted`() =
        runTest {
            coEvery {
                getPlaceAndStoriesByIdUseCase(testPlaceId, testPlaceLangId)
            } returns Result.failure(NullPointerException("place is null"))

            val viewModel = PlaceDetailViewModel(
                getPlaceAndStoriesByIdUseCase = getPlaceAndStoriesByIdUseCase,
                playerRepository = playerRepository,
                placeId = testPlaceId,
                placeLangId = testPlaceLangId
            )

            viewModel.state.test {
                val currentState = awaitItem()
                assertTrue(
                    "State should be Error when place is null",
                    currentState is PlaceDetailState.Error
                )
            }
        }

    @Test
    fun `Given multiple play calls, When play is called multiple times, Then playerRepository play is called each time`() =
        runTest {
            coEvery {
                getPlaceAndStoriesByIdUseCase(testPlaceId, testPlaceLangId)
            } returns Result.success(testPlace to testStories)

            val viewModel = PlaceDetailViewModel(
                getPlaceAndStoriesByIdUseCase = getPlaceAndStoriesByIdUseCase,
                playerRepository = playerRepository,
                placeId = testPlaceId,
                placeLangId = testPlaceLangId
            )

            val firstStories = testStories.take(2)
            val secondStories = testStories.drop(1)

            viewModel.play(firstStories)
            viewModel.play(secondStories)

            verify { playerRepository.play(firstStories) }
            verify { playerRepository.play(secondStories) }
        }
}