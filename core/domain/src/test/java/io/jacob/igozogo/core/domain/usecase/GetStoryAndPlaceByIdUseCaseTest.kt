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
    fun `Given story, When GetStoryAndPlaceByIdUseCase called, Then call repositories`() =
        runTest {
            // Given
            val testStory = storyTestData.first()
            val testPlace = placeTestData.first()
            coEvery { storyRepository.getStoryById(any(), any()) } returns testStory
            coEvery { placeRepository.getPlaceById(any(), any()) } returns testPlace

            // When
            val result = useCase(1, 1)

            // Then
            assertEquals(testPlace to testStory, result)
            coVerify { storyRepository.getStoryById(any(), any()) }
            coVerify { placeRepository.getPlaceById(any(), any()) }
        }
}