package io.jacob.igozogo.core.data.datasource.local

import app.cash.turbine.test
import io.jacob.igozogo.core.data.db.RecentSearchDao
import io.jacob.igozogo.core.data.model.local.search.RecentSearchEntity
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RecentSearchDataSourceTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val dao = mockk<RecentSearchDao>()
    private val dataSource = RecentSearchDataSourceImpl(dao)

    private val testEntity = RecentSearchEntity(
        query = "서울",
        queriedDate = Clock.System.now()
    )

    @Test
    fun `Given default limit, When getRecentSearches, Then call dao with default limit`() = runTest {
        // Given
        val expectedEntities = listOf(testEntity)
        every { dao.getRecentSearches(any()) } returns flowOf(expectedEntities)

        // When & Then
        dataSource.getRecentSearches().test {
            val result = awaitItem()
            assertEquals(expectedEntities, result)
            awaitComplete()
        }

        verify { dao.getRecentSearches(10) }
    }

    @Test
    fun `Given custom limit, When getRecentSearches, Then call dao with custom limit`() = runTest {
        // Given
        val customLimit = 5
        val expectedEntities = listOf(testEntity)
        every { dao.getRecentSearches(any()) } returns flowOf(expectedEntities)

        // When & Then
        dataSource.getRecentSearches(customLimit).test {
            val result = awaitItem()
            assertEquals(expectedEntities, result)
            awaitComplete()
        }

        verify { dao.getRecentSearches(customLimit) }
    }

    @Test
    fun `Given recent search entity, When upsertRecentSearch, Then call dao upsertRecentSearch`() = runTest {
        // Given
        coEvery { dao.upsertRecentSearch(any()) } returns Unit

        // When
        dataSource.upsertRecentSearch(testEntity)

        // Then
        coVerify { dao.upsertRecentSearch(testEntity) }
    }

    @Test
    fun `Given query string, When deleteRecentSearch, Then call dao deleteRecentSearch`() = runTest {
        // Given
        val query = "부산"
        coEvery { dao.deleteRecentSearch(any()) } returns Unit

        // When
        dataSource.deleteRecentSearch(query)

        // Then
        coVerify { dao.deleteRecentSearch(query) }
    }

    @Test
    fun `When clearRecentSearches, Then call dao clearRecentSearches`() = runTest {
        // Given
        coEvery { dao.clearRecentSearches() } returns Unit

        // When
        dataSource.clearRecentSearches()

        // Then
        coVerify { dao.clearRecentSearches() }
    }

    @Test
    fun `When getRecentSearchesCount, Then call dao getRecentSearchesCount and return count`() = runTest {
        // Given
        val expectedCount = 5
        coEvery { dao.getRecentSearchesCount() } returns expectedCount

        // When
        val result = dataSource.getRecentSearchesCount()

        // Then
        assertEquals(expectedCount, result)
        coVerify { dao.getRecentSearchesCount() }
    }
}