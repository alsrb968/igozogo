package io.jacob.igozogo.core.domain.usecase

import app.cash.turbine.test
import io.jacob.igozogo.core.domain.repository.PlaceRepository
import io.jacob.igozogo.core.testing.data.categoryTestData
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class GetCategoriesUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val placeRepository = mockk<PlaceRepository>()

    private val useCase = GetCategoriesUseCase(
        placeRepository = placeRepository
    )

    @Test
    fun `Given repository returns categories, When invoke, Then emit categories from repository`() =
        runTest {
            // Given
            coEvery { placeRepository.getPlaceCategories() } returns categoryTestData

            // When & Then
            useCase().test {
                val result = awaitItem()
                assertEquals(categoryTestData, result)
                awaitComplete()
            }

            coVerify { placeRepository.getPlaceCategories() }
        }

    @Test
    fun `Given repository returns empty list, When invoke, Then emit empty list`() =
        runTest {
            // Given
            coEvery { placeRepository.getPlaceCategories() } returns emptyList()

            // When & Then
            useCase().test {
                val result = awaitItem()
                assertEquals(emptyList<String>(), result)
                awaitComplete()
            }

            coVerify { placeRepository.getPlaceCategories() }
        }
}