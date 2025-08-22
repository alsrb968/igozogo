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

class GetStoryAndPlaceByIdUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val placeRepository = mockk<PlaceRepository>()
    private val storyRepository = mockk<StoryRepository>()

    private val useCase = GetStoryAndPlaceByIdUseCase(
        placeRepository = placeRepository,
        storyRepository = storyRepository,
    )

    @Test
    fun `Given story and place exist, When GetStoryAndPlaceByIdUseCase called, Then return success result with data`() =
        runTest {
            // Given
            val testStory = storyTestData.first()
            val testPlace = placeTestData.first()
            coEvery { storyRepository.getStoryById(any(), any()) } returns testStory
            coEvery { placeRepository.getPlaceById(any(), any()) } returns testPlace

            // When
            val result = useCase(1, 1)

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return place and story", testPlace to testStory, result.getOrNull())
            coVerify { storyRepository.getStoryById(any(), any()) }
            coVerify { placeRepository.getPlaceById(any(), any()) }
        }

    @Test
    fun `Given story is null, When GetStoryAndPlaceByIdUseCase called, Then return failure result`() =
        runTest {
            // Given
            coEvery { storyRepository.getStoryById(any(), any()) } returns null
            coEvery { placeRepository.getPlaceById(any(), any()) } returns placeTestData.first()

            // When
            val result = useCase(1, 1)

            // Then
            assertTrue("Result should be failure", result.isFailure)
            coVerify { storyRepository.getStoryById(any(), any()) }
            coVerify(exactly = 0) { placeRepository.getPlaceById(any(), any()) }
        }

    @Test
    fun `Given place is null, When GetStoryAndPlaceByIdUseCase called, Then return failure result`() =
        runTest {
            // Given
            val testStory = storyTestData.first()
            coEvery { storyRepository.getStoryById(any(), any()) } returns testStory
            coEvery { placeRepository.getPlaceById(any(), any()) } returns null

            // When
            val result = useCase(1, 1)

            // Then
            assertTrue("Result should be failure", result.isFailure)
            coVerify { storyRepository.getStoryById(any(), any()) }
            coVerify { placeRepository.getPlaceById(testStory.placeId, testStory.placeLangId) }
        }

    @Test
    fun `Given story repository throws exception, When GetStoryAndPlaceByIdUseCase called, Then return failure result`() =
        runTest {
            // Given
            coEvery { storyRepository.getStoryById(any(), any()) } throws RuntimeException("Network error")

            // When
            val result = useCase(1, 1)

            // Then
            assertTrue("Result should be failure", result.isFailure)
            coVerify { storyRepository.getStoryById(any(), any()) }
            coVerify(exactly = 0) { placeRepository.getPlaceById(any(), any()) }
        }

    @Test
    fun `Given place repository throws exception, When GetStoryAndPlaceByIdUseCase called, Then return failure result`() =
        runTest {
            // Given
            val testStory = storyTestData.first()
            coEvery { storyRepository.getStoryById(any(), any()) } returns testStory
            coEvery { placeRepository.getPlaceById(any(), any()) } throws RuntimeException("Network error")

            // When
            val result = useCase(1, 1)

            // Then
            assertTrue("Result should be failure", result.isFailure)
            coVerify { storyRepository.getStoryById(any(), any()) }
            coVerify { placeRepository.getPlaceById(testStory.placeId, testStory.placeLangId) }
        }
}