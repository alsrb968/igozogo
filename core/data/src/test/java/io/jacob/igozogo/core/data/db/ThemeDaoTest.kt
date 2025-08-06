package io.jacob.igozogo.core.data.db

import androidx.paging.PagingSource
import io.jacob.igozogo.core.data.mapper.asThemeEntities
import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity
import io.jacob.igozogo.core.testing.data.placeTestData
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ThemeDaoTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val dbRule = RoomDatabaseRule()

    private lateinit var dao: ThemeDao

    @Before
    fun setup() {
        dao = dbRule.db.themeDao()
    }

    @Test
    fun getThemesPagingSourceTest() = runTest {
        dao.insertThemes(entities)

        val pagingSource = dao.getThemesPagingSource()
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        val data = (result as PagingSource.LoadResult.Page).data
        assertEquals(10, data.size)
        assertEquals("부산시 영도구 문화관광지", data[0].title)
        assertEquals("부산 세븐브릿지", data[1].title)
        assertEquals("부산 서구 천마산로", data[2].title)
        assertEquals("부산 서구 꽃마을로", data[3].title)
        assertEquals("부산 서구 옛 전찻길 여행", data[4].title)
        assertEquals("부산 서구 개별 관광지", data[5].title)
        assertEquals("전설따라 설화따라-부산", data[6].title)
        assertEquals("부산 영도 물양장거리", data[7].title)
        assertEquals("부산 관광 명소", data[8].title)
        assertEquals("부산 현대모터 스튜디오", data[9].title)
    }

    @Test
    fun getThemesTest() = runTest {
        dao.insertThemes(entities)

        val themes = dao.getThemes(10)
        assertEquals(10, themes.size)
        assertEquals("부산시 영도구 문화관광지", themes[0].title)
        assertEquals("부산 세븐브릿지", themes[1].title)
        assertEquals("부산 서구 천마산로", themes[2].title)
        assertEquals("부산 서구 꽃마을로", themes[3].title)
        assertEquals("부산 서구 옛 전찻길 여행", themes[4].title)
        assertEquals("부산 서구 개별 관광지", themes[5].title)
        assertEquals("전설따라 설화따라-부산", themes[6].title)
        assertEquals("부산 영도 물양장거리", themes[7].title)
        assertEquals("부산 관광 명소", themes[8].title)
        assertEquals("부산 현대모터 스튜디오", themes[9].title)
    }

    @Test
    fun getThemeCategoriesPagingSourceTest() = runTest {
        dao.insertThemes(entities)

        val pagingSource = dao.getThemeCategoriesPagingSource()
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        val data = (result as PagingSource.LoadResult.Page).data
        assertEquals(1, data.size)
        assertEquals("", data[0])
    }

    @Test
    fun getThemeCategoriesTest() = runTest {
        dao.insertThemes(entities)

        val categories = dao.getThemeCategories()
        assertEquals(1, categories.size)
        assertEquals("", categories[0])
    }

    @Test
    fun getThemesByCategoryPagingSourceTest() = runTest {
        dao.insertThemes(entities)

        val pagingSource = dao.getThemesByCategoryPagingSource("")
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        val data = (result as PagingSource.LoadResult.Page).data
        assertEquals(10, data.size)
    }

    @Test
    fun getThemesByCategoryTest() = runTest {
        dao.insertThemes(entities)

        val themes = dao.getThemesByCategory("", 10)
        assertEquals(10, themes.size)
    }

    @Test
    fun getThemesByLocationPagingSourceTest() = runTest {
        dao.insertThemes(entities)

        val mapX = 129.05
        val mapY = 35.1

        fun isSortedAscByDistance(
            entities: List<ThemeEntity>
        ): Boolean {
            val distances = entities.map { entity ->
                val dx = entity.mapX - mapX
                val dy = entity.mapY - mapY
                dx * dx + dy * dy // sqrt 없이 제곱 거리 비교 (더 빠름)
            }
            return distances == distances.sorted()
        }

        val pagingSource1 =
            dao.getThemesByLocationPagingSource(mapX, mapY, 15000 / METERS_PER_DEGREE)
        val result1 = pagingSource1.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        val data1 = (result1 as PagingSource.LoadResult.Page).data
        assertEquals(9, data1.size)
        assertTrue(isSortedAscByDistance(data1))

        val pagingSource2 =
            dao.getThemesByLocationPagingSource(mapX, mapY, 3000 / METERS_PER_DEGREE)
        val result2 = pagingSource2.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        val data2 = (result2 as PagingSource.LoadResult.Page).data
        assertEquals(3, data2.size)
        assertTrue(isSortedAscByDistance(data2))
    }

    @Test
    fun getThemesByLocationTest() = runTest {
        dao.insertThemes(entities)

        val mapX = 129.05
        val mapY = 35.1

        fun isSortedAscByDistance(
            entities: List<ThemeEntity>
        ): Boolean {
            val distances = entities.map { entity ->
                val dx = entity.mapX - mapX
                val dy = entity.mapY - mapY
                dx * dx + dy * dy // sqrt 없이 제곱 거리 비교 (더 빠름)
            }
            return distances == distances.sorted()
        }

        val themes1 = dao.getThemesByLocation(mapX, mapY, 15000 / METERS_PER_DEGREE, 10)
        assertEquals(9, themes1.size)
        assertTrue(isSortedAscByDistance(themes1))

        val themes2 = dao.getThemesByLocation(mapX, mapY, 3000 / METERS_PER_DEGREE, 10)
        assertEquals(3, themes2.size)
        assertTrue(isSortedAscByDistance(themes2))
    }


    @Test
    fun getThemesByKeywordPagingSourceTest() = runTest {
        dao.insertThemes(entities)

        val pagingSource1 = dao.getThemesByKeywordPagingSource("서구")
        val result1 = pagingSource1.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        val data1 = (result1 as PagingSource.LoadResult.Page).data
        assertEquals(4, data1.size)

        val pagingSource2 = dao.getThemesByKeywordPagingSource("영도")
        val result2 = pagingSource2.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        val data2 = (result2 as PagingSource.LoadResult.Page).data
        assertEquals(2, data2.size)
    }

    @Test
    fun getThemesByKeywordTest() = runTest {
        dao.insertThemes(entities)

        val themes1 = dao.getThemesByKeyword("서구", 10)
        assertEquals(4, themes1.size)

        val themes2 = dao.getThemesByKeyword("영도", 10)
        assertEquals(2, themes2.size)
    }

    @Test
    fun getThemeById() = runTest {
        dao.insertThemes(entities)

        val theme = dao.getThemeById(themeId = 2812, themeLangId = 3826)
        assertNotNull(theme)
        assertEquals("", theme?.themeCategory)
        assertEquals("부산시 영도구 문화관광지", theme?.title)
    }

    @Test
    fun getThemesCountTest() = runTest {
        dao.insertThemes(entities)

        val count = dao.getThemesCount()
        assertEquals(10, count)
    }

    companion object {
        private const val METERS_PER_DEGREE = 111000.0

        private val entities = placeTestData.asThemeEntities()
    }
}