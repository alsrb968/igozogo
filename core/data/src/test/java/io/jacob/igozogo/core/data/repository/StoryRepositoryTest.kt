package io.jacob.igozogo.core.data.repository

import io.jacob.igozogo.core.data.TestPagingSource
import io.jacob.igozogo.core.data.datasource.local.StoryDataSource
import io.jacob.igozogo.core.data.datasource.remote.OdiiDataSource
import io.jacob.igozogo.core.data.mapper.toStory
import io.jacob.igozogo.core.data.place
import io.jacob.igozogo.core.data.storyEntities
import io.jacob.igozogo.core.data.storyResponses
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import io.mockk.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class StoryRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val storyDataSource = mockk<StoryDataSource>()
    private val storyRemoteMediatorFactory = mockk<StoryRemoteMediator.Factory>()
    private val odiiDataSource = mockk<OdiiDataSource>()

    private val repository = StoryRepositoryImpl(
        storyDataSource = storyDataSource,
        storyRemoteMediatorFactory = storyRemoteMediatorFactory,
        odiiDataSource = odiiDataSource,
    )

    @Test
    fun `Given Stories, When getStories called, Then call dataSource`() =
        runTest {
            // Given
            coEvery { storyDataSource.getStories(any()) } returns storyEntities

            // When
            repository.getStories()

            // Then
            coVerify { storyDataSource.getStories(any()) }
        }

    @Test
    fun `Given RemoteMediator, When getStoriesByPlacePaging called, Then call dataSource`() =
        runTest {
            // Given
            val fakeRemoteMediator = mockk<StoryRemoteMediator>(relaxed = true)
            every { storyRemoteMediatorFactory.create(any()) } returns fakeRemoteMediator
            every {
                storyDataSource.getStoriesByThemePagingSource(any(), any())
            } returns TestPagingSource(storyEntities)

            // When
            val flow = repository.getStoriesByPlacePaging(place = place, pageSize = 10)

            val job = launch { flow.collectLatest { pagingData -> } }

            // Then
            advanceUntilIdle()
            job.cancel()
            coVerify { storyDataSource.getStoriesByThemePagingSource(any(), any()) }
        }

    @Test
    fun `Given cached Stories, When getStoriesByPlace called, Then call dataSource`() =
        runTest {
            // Given
            coEvery { storyDataSource.getStoriesByTheme(any(), any()) } returns storyEntities

            // When
            repository.getStoriesByPlace(place = place)

            // Then
            coVerify { storyDataSource.getStoriesByTheme(any(), any()) }
            coVerify(exactly = 0) {
                odiiDataSource.getStoryBasedList(any(), any(), any(), any(), any())
            }
            coVerify(exactly = 0) { storyDataSource.insertStories(any()) }
        }

    @Test
    fun `Given not cached Stories, When getStoriesByPlace called, Then call dataSource`() =
        runTest {
            // Given
            coEvery { storyDataSource.getStoriesByTheme(any(), any()) } returns listOf()
            coEvery {
                odiiDataSource.getStoryBasedList(any(), any(), any(), any(), any())
            } returns Result.success(storyResponses)
            coEvery { storyDataSource.insertStories(any()) } just Runs

            // When
            repository.getStoriesByPlace(place = place)

            // Then
            coVerify { storyDataSource.getStoriesByTheme(any(), any()) }
            coVerify { odiiDataSource.getStoryBasedList(any(), any(), any(), any(), any()) }
            coVerify { storyDataSource.insertStories(any()) }
        }

    @Test
    fun `Given RemoteMediator, When getStoriesByLocationPaging called, Then call dataSource`() =
        runTest {
            // Given
            val fakeRemoteMediator = mockk<StoryRemoteMediator>(relaxed = true)
            every { storyRemoteMediatorFactory.create(any()) } returns fakeRemoteMediator
            every {
                storyDataSource.getStoriesByLocationPagingSource(any(), any(), any())
            } returns TestPagingSource(storyEntities)

            // When
            val flow = repository.getStoriesByLocationPaging(1.0, 1.0, 1)

            val job = launch { flow.collectLatest { pagingData -> } }

            // Then
            advanceUntilIdle()
            job.cancel()
            coVerify { storyDataSource.getStoriesByLocationPagingSource(any(), any(), any()) }
        }

    @Test
    fun `Given cached Stories, When getStoriesByLocation called, Then call dataSource`() =
        runTest {
            // Given
            coEvery {
                storyDataSource.getStoriesByLocation(any(), any(), any(), any())
            } returns storyEntities

            // When
            repository.getStoriesByLocation(1.0, 1.0, 1, 10)

            // Then
            coVerify { storyDataSource.getStoriesByLocation(any(), any(), any(), any()) }
            coVerify(exactly = 0) {
                odiiDataSource.getStoryLocationBasedList(any(), any(), any(), any(), any(), any())
            }
            coVerify(exactly = 0) { storyDataSource.insertStories(any()) }
        }

    @Test
    fun `Given not cached Stories, When getStoriesByLocation called, Then call dataSource`() =
        runTest {
            // Given
            coEvery {
                storyDataSource.getStoriesByLocation(any(), any(), any(), any())
            } returns listOf()
            coEvery {
                odiiDataSource.getStoryLocationBasedList(any(), any(), any(), any(), any(), any())
            } returns Result.success(storyResponses)
            coEvery { storyDataSource.insertStories(any()) } just Runs

            // When
            repository.getStoriesByLocation(1.0, 1.0, 1, 10)

            // Then
            coVerify { storyDataSource.getStoriesByLocation(any(), any(), any(), any()) }
            coVerify {
                odiiDataSource.getStoryLocationBasedList(any(), any(), any(), any(), any(), any())
            }
            coVerify { storyDataSource.insertStories(any()) }
        }

    @Test
    fun `Given RemoteMediator, When getStoriesByKeywordPaging called, Then call dataSource`() =
        runTest {
            // Given
            val fakeRemoteMediator = mockk<StoryRemoteMediator>(relaxed = true)
            every { storyRemoteMediatorFactory.create(any()) } returns fakeRemoteMediator
            every {
                storyDataSource.getStoriesByKeywordPagingSource(any())
            } returns TestPagingSource(storyEntities)

            // When
            val flow = repository.getStoriesByKeywordPaging("test", 1)

            val job = launch { flow.collectLatest { pagingData -> } }

            // Then
            advanceUntilIdle()
            job.cancel()
            coVerify { storyDataSource.getStoriesByKeywordPagingSource(any()) }
        }

    @Test
    fun `Given cached Stories, When getStoriesByKeyword called, Then call dataSource`() =
        runTest {
            // Given
            coEvery { storyDataSource.getStoriesByKeyword(any(), any()) } returns storyEntities

            // When
            repository.getStoriesByKeyword("test", 10)

            // Then
            coVerify { storyDataSource.getStoriesByKeyword(any(), any()) }
            coVerify(exactly = 0) { odiiDataSource.getStorySearchList(any(), any(), any(), any()) }
            coVerify(exactly = 0) { storyDataSource.insertStories(any()) }
        }

    @Test
    fun `Given not cached Stories, When getStoriesByKeyword called, Then call dataSource`() =
        runTest {
            // Given
            coEvery { storyDataSource.getStoriesByKeyword(any(), any()) } returns listOf()
            coEvery {
                odiiDataSource.getStorySearchList(any(), any(), any(), any())
            } returns Result.success(storyResponses)
            coEvery { storyDataSource.insertStories(any()) } just Runs

            // When
            repository.getStoriesByKeyword("test", 10)

            // Then
            coVerify { storyDataSource.getStoriesByKeyword(any(), any()) }
            coVerify { odiiDataSource.getStorySearchList(any(), any(), any(), any()) }
            coVerify { storyDataSource.insertStories(any()) }
        }

    @Test
    fun `Given Stories, When getStoryById called, Then call dataSource`() =
        runTest {
            // Given
            coEvery { storyDataSource.getStoryById(any(), any()) } returns storyEntities[0]

            // When
            val story = repository.getStoryById(1, 2)

            // Then
            assertNotNull(story)
            assertEquals(storyEntities[0].toStory(), story)
            coVerify { storyDataSource.getStoryById(any(), any()) }
        }
}