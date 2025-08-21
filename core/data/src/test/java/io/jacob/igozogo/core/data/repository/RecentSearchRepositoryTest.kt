package io.jacob.igozogo.core.data.repository

import app.cash.turbine.test
import io.jacob.igozogo.core.data.datasource.local.RecentSearchDataSource
import io.jacob.igozogo.core.data.model.local.search.RecentSearchEntity
import io.jacob.igozogo.core.testing.data.recentSearchTestData
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.hours

class RecentSearchRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val recentSearchDataSource = mockk<RecentSearchDataSource>()

    private val repository = RecentSearchRepositoryImpl(
        recentSearchDataSource = recentSearchDataSource
    )

    @Test
    fun `Given datasource returns entities, When getRecentSearches with default limit, Then map to query strings and return 10 items`() =
        runTest {
            // Given
            val baseTime = Clock.System.now()
            val entities = recentSearchTestData.mapIndexed { index: Int, query: String ->
                RecentSearchEntity(query, baseTime - index.hours)
            }
            every { recentSearchDataSource.getRecentSearches(any()) } returns flowOf(entities)

            // When
            repository.getRecentSearches().test {
                val result = awaitItem()

                // Then
                assertEquals(10, result.size)
                assertEquals(recentSearchTestData.first(), result.first())
                assertEquals(recentSearchTestData.last(), result.last())
                awaitComplete()
            }

            verify { recentSearchDataSource.getRecentSearches(10) }
        }

    @Test
    fun `Given datasource returns entities, When getRecentSearches with custom limit, Then map to query strings and return limited items`() =
        runTest {
            // Given
            val baseTime = Clock.System.now()
            val entities = recentSearchTestData.take(5).mapIndexed { index: Int, query: String ->
                RecentSearchEntity(query, baseTime - index.hours)
            }
            every { recentSearchDataSource.getRecentSearches(any()) } returns flowOf(entities)

            // When
            repository.getRecentSearches(5).test {
                val result = awaitItem()

                // Then
                assertEquals(5, result.size)
                assertEquals(recentSearchTestData.first(), result.first())
                assertEquals(recentSearchTestData[4], result.last())
                awaitComplete()
            }

            verify { recentSearchDataSource.getRecentSearches(5) }
        }

    @Test
    fun `Given query string, When upsertRecentSearch, Then call datasource with RecentSearchEntity`() =
        runTest {
            // Given
            val query = "제주도"
            val slot = slot<RecentSearchEntity>()
            coEvery { recentSearchDataSource.upsertRecentSearch(capture(slot)) } just Runs

            // When
            repository.upsertRecentSearch(query)

            // Then
            coVerify { recentSearchDataSource.upsertRecentSearch(any()) }

            // 비즈니스 로직 검증: 현재 시간으로 RecentSearchEntity가 생성되는지 확인
            val capturedEntity = slot.captured
            assertEquals(query, capturedEntity.query)
            assertTrue(capturedEntity.queriedDate <= Clock.System.now())
        }

    @Test
    fun `Given query string, When deleteRecentSearch, Then call datasource deleteRecentSearch`() =
        runTest {
            // Given
            coEvery { recentSearchDataSource.deleteRecentSearch(any()) } just Runs

            // When
            repository.deleteRecentSearch("삭제할검색어")

            // Then
            coVerify { recentSearchDataSource.deleteRecentSearch(any()) }
        }

    @Test
    fun `Given repository, When clearRecentSearches, Then call datasource clearRecentSearches`() =
        runTest {
            // Given
            coEvery { recentSearchDataSource.clearRecentSearches() } just Runs

            // When
            repository.clearRecentSearches()

            // Then
            coVerify { recentSearchDataSource.clearRecentSearches() }
        }

    @Test
    fun `Given repository, When getRecentSearchesCount, Then call datasource getRecentSearchesCount`() =
        runTest {
            // Given
            coEvery { recentSearchDataSource.getRecentSearchesCount() } returns 5

            // When
            repository.getRecentSearchesCount()

            // Then
            coVerify { recentSearchDataSource.getRecentSearchesCount() }
        }
}