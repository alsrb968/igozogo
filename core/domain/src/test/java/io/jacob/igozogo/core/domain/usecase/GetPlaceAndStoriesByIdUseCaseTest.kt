package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.repository.PlaceRepository
import io.jacob.igozogo.core.domain.repository.StoryRepository
import io.jacob.igozogo.core.testing.data.placeTestData
import io.jacob.igozogo.core.testing.data.storyTestData
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class GetPlaceAndStoriesByIdUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val placeRepository = mockk<PlaceRepository>()
    private val storyRepository = mockk<StoryRepository>()

    private val useCase = GetPlaceAndStoriesByIdUseCase(
        placeRepository = placeRepository,
        storyRepository = storyRepository,
    )

    @Test
    fun `Given place, When GetPlaceAndStoriesByIdUseCase called, Then call repositories`() =
        runTest {
            // Given
            val testPlace = placeTestData.first()
            val testStories = storyTestData
            coEvery { placeRepository.getPlaceById(any(), any()) } returns testPlace
            coEvery { storyRepository.getStoriesByPlace(any()) } returns testStories

            // When
            val result = useCase(1, 1)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(testPlace to testStories, result.getOrNull())
            coVerify { placeRepository.getPlaceById(any(), any()) }
            coVerify { storyRepository.getStoriesByPlace(any()) }
        }

    @Test
    fun `Given null place, When GetPlaceAndStoriesByIdUseCase called, Then call repositories`() =
        runTest {
            // Given
            coEvery { placeRepository.getPlaceById(any(), any()) } returns null

            // When
            val result = useCase(1, 1)

            // Then
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is NullPointerException)
            coVerify { placeRepository.getPlaceById(any(), any()) }
            coVerify(exactly = 0) { storyRepository.getStoriesByPlace(any()) }
        }
}