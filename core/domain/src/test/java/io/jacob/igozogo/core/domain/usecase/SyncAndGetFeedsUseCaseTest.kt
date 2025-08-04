package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.repository.PlaceRepository
import io.jacob.igozogo.core.domain.repository.StoryRepository
import io.jacob.igozogo.core.testing.data.categoryTestData
import io.jacob.igozogo.core.testing.data.placeTestData
import io.jacob.igozogo.core.testing.data.storyTestData
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SyncAndGetFeedsUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val placeRepository = mockk<PlaceRepository>()
    private val storyRepository = mockk<StoryRepository>()

    private val useCase = SyncAndGetFeedsUseCase(
        placeRepository = placeRepository,
        storyRepository = storyRepository,
    )

    @Test
    fun `Given synced db, When SyncAndGetFeedsUseCase called, Then call repositories`() =
        runTest {
            // Given
            coEvery { placeRepository.getPlacesCount() } returns 2079
            coEvery { placeRepository.getPlaceCategories() } returns categoryTestData
            coEvery { placeRepository.getPlaces(any()) } returns placeTestData
            coEvery { storyRepository.getStories(any()) } returns storyTestData

            // When
            var isSynced = false
            useCase({ isSynced = true })

            // Then
            assertFalse(isSynced)
            coVerify { placeRepository.getPlacesCount() }
            coVerify(exactly = 0) { placeRepository.syncPlaces(any()) }
            coVerify { placeRepository.getPlaceCategories() }
            coVerify { placeRepository.getPlaces(any()) }
            coVerify { storyRepository.getStories(any()) }
        }

    @Test
    fun `Given not synced db, When SyncAndGetFeedsUseCase called, Then call repositories`() =
        runTest {
            // Given
            coEvery { placeRepository.getPlacesCount() } returns 2078
            coEvery { placeRepository.syncPlaces(any()) } just Runs
            coEvery { placeRepository.getPlaceCategories() } returns categoryTestData
            coEvery { placeRepository.getPlaces(any()) } returns placeTestData
            coEvery { storyRepository.getStories(any()) } returns storyTestData

            // When
            var isSynced = false
            useCase({ isSynced = true })

            // Then
            assertTrue(isSynced)
            coVerify { placeRepository.getPlacesCount() }
            coVerify { placeRepository.syncPlaces(any()) }
            coVerify { placeRepository.getPlaceCategories() }
            coVerify { placeRepository.getPlaces(any()) }
            coVerify { storyRepository.getStories(any()) }
        }
}