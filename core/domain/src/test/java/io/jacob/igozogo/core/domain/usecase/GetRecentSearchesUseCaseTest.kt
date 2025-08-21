package io.jacob.igozogo.core.domain.usecase

import app.cash.turbine.test
import io.jacob.igozogo.core.domain.repository.RecentSearchRepository
import io.jacob.igozogo.core.testing.data.recentSearchTestData
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class GetRecentSearchesUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val recentSearchRepository = mockk<RecentSearchRepository>()

    private val useCase = GetRecentSearchesUseCase(
        recentSearchRepository = recentSearchRepository
    )

    @Test
    fun `Given default parameter, When invoke, Then call repository with default limit 10`() =
        runTest {
            // Given
            every { recentSearchRepository.getRecentSearches(any()) } returns flowOf(
                recentSearchTestData
            )

            // When & Then
            useCase().test {
                val result = awaitItem()
                assertEquals(recentSearchTestData, result)
                awaitComplete()
            }

            verify { recentSearchRepository.getRecentSearches(10) }
        }

    @Test
    fun `Given custom limit parameter, When invoke, Then call repository with custom limit`() =
        runTest {
            // Given
            val customLimit = 5
            val limitedData = recentSearchTestData.take(customLimit)
            every { recentSearchRepository.getRecentSearches(any()) } returns flowOf(limitedData)

            // When & Then
            useCase(customLimit).test {
                val result = awaitItem()
                assertEquals(limitedData, result)
                awaitComplete()
            }

            verify { recentSearchRepository.getRecentSearches(customLimit) }
        }

    @Test
    fun `Given repository returns empty flow, When invoke, Then emit empty list`() =
        runTest {
            // Given
            every { recentSearchRepository.getRecentSearches(any()) } returns flowOf(emptyList())

            // When & Then
            useCase().test {
                val result = awaitItem()
                assertEquals(emptyList<String>(), result)
                awaitComplete()
            }

            verify { recentSearchRepository.getRecentSearches(10) }
        }
}