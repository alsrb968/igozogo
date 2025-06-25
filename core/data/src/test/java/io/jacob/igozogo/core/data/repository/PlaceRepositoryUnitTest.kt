package io.jacob.igozogo.core.data.repository

import io.jacob.igozogo.core.data.TestPagingSource
import io.jacob.igozogo.core.data.datasource.local.ThemeDataSource
import io.jacob.igozogo.core.data.datasource.remote.OdiiDataSource
import io.jacob.igozogo.core.data.mapper.toPlace
import io.jacob.igozogo.core.data.mapper.toThemeEntity
import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity
import io.jacob.igozogo.core.data.model.remote.odii.ThemeResponse
import io.jacob.igozogo.core.domain.repository.PlaceRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class PlaceRepositoryUnitTest {
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val themeDataSource = mockk<ThemeDataSource>()
    private val odiiDataSource = mockk<OdiiDataSource>()

    private lateinit var repository: PlaceRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = PlaceRepositoryImpl(
            themeDataSource = themeDataSource,
            odiiDataSource = odiiDataSource,
        )
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Given getThemeBasedList successful, When syncPlaces called, Then insertThemes into db`() =
        testScope.runTest {
            // Given
            coEvery {
                odiiDataSource.getThemeBasedList(any(), any())
            } returns Result.success(themeResponses)
            val slot = slot<List<ThemeEntity>>()
            coEvery { themeDataSource.insertThemes(capture(slot)) } just Runs

            // When
            repository.syncPlaces()

            // Then
            coVerify { odiiDataSource.getThemeBasedList(any(), any()) }
            coVerify { themeDataSource.insertThemes(any()) }
            val capturedThemes = slot.captured
            assertEquals(3, capturedThemes.size)
            assertEquals("백제문화단지", capturedThemes[0].title)
            assertEquals("경주 불국사", capturedThemes[1].title)
            assertEquals("괘릉", capturedThemes[2].title)
        }

    @Test
    fun `Given themes, When getPlacesPaging called, Then call dataSource`() =
        testScope.runTest {
            // Given
            coEvery { themeDataSource.getThemesPagingSource() } returns TestPagingSource(
                themeEntities
            )

            // When
            val flow = repository.getPlacesPaging()

            val job = launch { flow.collectLatest { pagingData -> } }

            // Then
            advanceUntilIdle()
            job.cancel()
            coVerify { themeDataSource.getThemesPagingSource() }
        }

    @Test
    fun `Given themes, When getPlaces called, Then call dataSource`() =
        testScope.runTest {
            // Given
            coEvery { themeDataSource.getThemes(any()) } returns themeEntities

            // When
            repository.getPlaces()

            // Then
            coVerify { themeDataSource.getThemes(any()) }
        }

    @Test
    fun `Given themes, When getPlaceCategoriesPaging called, Then call dataSource`() =
        testScope.runTest {
            // Given
            coEvery { themeDataSource.getThemeCategoriesPagingSource() } returns TestPagingSource(
                listOf()
            )

            // When
            val flow = repository.getPlaceCategoriesPaging()

            val job = launch { flow.collectLatest { pagingData -> } }

            // Then
            advanceUntilIdle()
            job.cancel()
            coVerify { themeDataSource.getThemeCategoriesPagingSource() }
        }

    @Test
    fun `Given themes, When getPlaceCategories called, Then call dataSource`() =
        testScope.runTest {
            // Given
            coEvery { themeDataSource.getThemeCategories() } returns listOf()

            // When
            repository.getPlaceCategories()

            // Then
            coVerify { themeDataSource.getThemeCategories() }
        }

    @Test
    fun `Given themes, When getPlacesByCategoryPaging called, Then call dataSource`() =
        testScope.runTest {
            // Given
            coEvery { themeDataSource.getThemesByCategoryPagingSource(any()) } returns TestPagingSource(
                themeEntities
            )

            // When
            val flow = repository.getPlacesByCategoryPaging("백제역사여행")

            val job = launch { flow.collectLatest { pagingData -> } }

            // Then
            advanceUntilIdle()
            job.cancel()
            coVerify { themeDataSource.getThemesByCategoryPagingSource(any()) }
        }

    @Test
    fun `Given themes, When getPlacesByCategory called with empty category, Then call dataSource`() =
        testScope.runTest {
            // Given
            coEvery { themeDataSource.getThemesByCategory(any(), any()) } returns themeEntities

            // When
            repository.getPlacesByCategory("", 10)

            // Then
            coVerify { themeDataSource.getThemesByCategory(any(), any()) }
        }

    @Test
    fun `Given themes, When getPlacesByLocationPaging called, Then call dataSource`() =
        testScope.runTest {
            // Given
            every {
                themeDataSource.getThemesByLocationPagingSource(any(), any(), any())
            } returns TestPagingSource(themeEntities)

            // When
            val flow = repository.getPlacesByLocationPaging(126.852601, 35.159545, 20000)

            val job = launch { flow.collectLatest { pagingData -> } }

            // Then
            advanceUntilIdle()
            job.cancel()
            coVerify { themeDataSource.getThemesByLocationPagingSource(any(), any(), any()) }
        }

    @Test
    fun `Given themes, When getPlacesByLocation called, Then call dataSource`() =
        testScope.runTest {
            // Given
            coEvery {
                themeDataSource.getThemesByLocation(any(), any(), any(), any())
            } returns themeEntities

            // When
            repository.getPlacesByLocation(126.852601, 35.159545, 200, 10)

            // Then
            coVerify { themeDataSource.getThemesByLocation(any(), any(), any(), any()) }
        }

    @Test
    fun `Given themes, When getPlacesByKeywordPaging called, Then call dataSource`() =
        testScope.runTest {
            // Given
            every { themeDataSource.getThemesByKeywordPagingSource(any()) } returns TestPagingSource(
                themeEntities
            )

            // When
            val flow = repository.getPlacesByKeywordPaging("백제")

            val job = launch { flow.collectLatest { pagingData -> } }

            // Then
            advanceUntilIdle()
            job.cancel()
            coVerify { themeDataSource.getThemesByKeywordPagingSource(any()) }
        }

    @Test
    fun `Given themes, When getPlacesByKeyword called, Then call dataSource`() =
        testScope.runTest {
            // Given
            coEvery { themeDataSource.getThemesByKeyword(any(), any()) } returns themeEntities

            // When
            repository.getPlacesByKeyword("백제", 10)

            // Then
            coVerify { themeDataSource.getThemesByKeyword(any(), any()) }
        }

    @Test
    fun `Given themes, When getPlaceById called, Then call dataSource`() =
        testScope.runTest {
            // Given
            coEvery { themeDataSource.getThemeById(any(), any()) } returns themeEntities[0]

            // When
            val _place = repository.getPlaceById(1, 2)

            // Then
            assertNotNull(_place)
            assertEquals(place, _place)
            coVerify { themeDataSource.getThemeById(any(), any()) }
        }

    @Test
    fun `Given themes, When getPlacesCount called, Then call dataSource`() =
        testScope.runTest {
            // Given
            coEvery { themeDataSource.getThemesCount() } returns 10

            // When
            val count = repository.getPlacesCount()

            // Then
            assertEquals(10, count)
            coVerify { themeDataSource.getThemesCount() }
        }

    companion object {
        private val themeResponses = listOf(
            ThemeResponse(
                themeId = 1,
                themeLangId = 1,
                themeCategory = "백제역사여행",
                addr1 = "충청남도",
                addr2 = "부여군",
                title = "백제문화단지",
                mapX = 126.905507,
                mapY = 36.306984,
                langCheck = "1111",
                langCode = "ko",
                imageUrl = "",
                createdTime = "20190923193941",
                modifiedTime = "20200615142618",
            ),
            ThemeResponse(
                themeId = 2,
                themeLangId = 5,
                themeCategory = "신라역사여행",
                addr1 = "경상북도",
                addr2 = "경주시",
                title = "경주 불국사",
                mapX = 129.332099,
                mapY = 35.790122,
                langCheck = "1111",
                langCode = "ko",
                imageUrl = "",
                createdTime = "20190923194000",
                modifiedTime = "20230725172140",
            ),
            ThemeResponse(
                themeId = 4,
                themeLangId = 13,
                themeCategory = "신라역사여행",
                addr1 = "경상북도",
                addr2 = "경주시",
                title = "괘릉",
                mapX = 129.320083,
                mapY = 35.759648,
                langCheck = "1111",
                langCode = "ko",
                imageUrl = "",
                createdTime = "20190923194001",
                modifiedTime = "20200921110253",
            ),
        )

        private val themeEntities = themeResponses.toThemeEntity()

        private val place = themeEntities[0].toPlace()
    }
}