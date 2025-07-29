package io.jacob.igozogo.core.data.db

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ThemeDaoTest {
    private lateinit var db: VisitKoreaDatabase
    private lateinit var dao: ThemeDao

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, VisitKoreaDatabase::class.java)
            .build()
        dao = db.themeDao()
    }

    @After
    fun teardown() {
        db.close()
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
        assertEquals(5, data.size)
        assertEquals("광주 양동시장", data[0].title)
        assertEquals("월봉서원", data[1].title)
        assertEquals("너브실마을", data[2].title)
        assertEquals("칠송정", data[3].title)
        assertEquals("용아생가", data[4].title)
    }

    @Test
    fun getThemesTest() = runTest {
        dao.insertThemes(entities)

        val themes = dao.getThemes(10)
        assertEquals(5, themes.size)
        assertEquals("광주 양동시장", themes[0].title)
        assertEquals("월봉서원", themes[1].title)
        assertEquals("너브실마을", themes[2].title)
        assertEquals("칠송정", themes[3].title)
        assertEquals("용아생가", themes[4].title)
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
        assertEquals(2, data.size)
        assertEquals("전통시장나들이", data[0])
        assertEquals("", data[1])
    }

    @Test
    fun getThemeCategoriesTest() = runTest {
        dao.insertThemes(entities)

        val categories = dao.getThemeCategories()
        assertEquals(2, categories.size)
        assertEquals("전통시장나들이", categories[0])
        assertEquals("", categories[1])
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
        assertEquals(4, data.size)
        assertEquals("월봉서원", data[0].title)
        assertEquals("너브실마을", data[1].title)
        assertEquals("칠송정", data[2].title)
        assertEquals("용아생가", data[3].title)
    }

    @Test
    fun getThemesByCategoryTest() = runTest {
        dao.insertThemes(entities)

        val themes = dao.getThemesByCategory("", 10)
        assertEquals(4, themes.size)
        assertEquals("월봉서원", themes[0].title)
        assertEquals("너브실마을", themes[1].title)
        assertEquals("칠송정", themes[2].title)
        assertEquals("용아생가", themes[3].title)
    }

    @Test
    fun getThemesByLocationPagingSourceTest() = runTest {
        dao.insertThemes(entities)

        val mapX = 126.852601
        val mapY = 35.159545

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
            dao.getThemesByLocationPagingSource(mapX, mapY, 20000 / METERS_PER_DEGREE)
        val result1 = pagingSource1.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        val data1 = (result1 as PagingSource.LoadResult.Page).data
        assertEquals(5, data1.size)
        assertTrue(isSortedAscByDistance(data1))
        assertEquals("광주 양동시장", data1[0].title)
        assertEquals("용아생가", data1[1].title)
        assertEquals("월봉서원", data1[2].title)
        assertEquals("칠송정", data1[3].title)
        assertEquals("너브실마을", data1[4].title)

        val pagingSource2 =
            dao.getThemesByLocationPagingSource(mapX, mapY, 10000 / METERS_PER_DEGREE)
        val result2 = pagingSource2.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        val data2 = (result2 as PagingSource.LoadResult.Page).data
        assertEquals(2, data2.size)
        assertTrue(isSortedAscByDistance(data2))
        assertEquals("광주 양동시장", data2[0].title)
        assertEquals("용아생가", data2[1].title)
    }

    @Test
    fun getThemesByLocationTest() = runTest {
        dao.insertThemes(entities)

        val mapX = 126.852601
        val mapY = 35.159545

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

        val themes1 = dao.getThemesByLocation(mapX, mapY, 20000 / METERS_PER_DEGREE, 10)
        assertEquals(5, themes1.size)
        assertTrue(isSortedAscByDistance(themes1))
        assertEquals("광주 양동시장", themes1[0].title)
        assertEquals("용아생가", themes1[1].title)
        assertEquals("월봉서원", themes1[2].title)
        assertEquals("칠송정", themes1[3].title)
        assertEquals("너브실마을", themes1[4].title)

        val themes2 = dao.getThemesByLocation(mapX, mapY, 10000 / METERS_PER_DEGREE, 10)
        assertEquals(2, themes2.size)
        assertTrue(isSortedAscByDistance(themes2))
        assertEquals("광주 양동시장", themes2[0].title)
        assertEquals("용아생가", themes2[1].title)
    }


    @Test
    fun getThemesByKeywordPagingSourceTest() = runTest {
        dao.insertThemes(entities)

        val pagingSource1 = dao.getThemesByKeywordPagingSource("전통")
        val result1 = pagingSource1.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        val data1 = (result1 as PagingSource.LoadResult.Page).data
        assertEquals(1, data1.size)
        assertEquals("광주 양동시장", data1[0].title)

        val pagingSource2 = dao.getThemesByKeywordPagingSource("광주")
        val result2 = pagingSource2.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        val data2 = (result2 as PagingSource.LoadResult.Page).data
        assertEquals(5, data2.size)
        assertEquals("광주 양동시장", data2[0].title)
        assertEquals("월봉서원", data2[1].title)
        assertEquals("너브실마을", data2[2].title)
        assertEquals("칠송정", data2[3].title)
        assertEquals("용아생가", data2[4].title)
    }

    @Test
    fun getThemesByKeywordTest() = runTest {
        dao.insertThemes(entities)

        val themes1 = dao.getThemesByKeyword("전통", 10)
        assertEquals(1, themes1.size)
        assertEquals("광주 양동시장", themes1[0].title)

        val themes2 = dao.getThemesByKeyword("광주", 10)
        assertEquals(5, themes2.size)
        assertEquals("광주 양동시장", themes2[0].title)
        assertEquals("월봉서원", themes2[1].title)
        assertEquals("너브실마을", themes2[2].title)
        assertEquals("칠송정", themes2[3].title)
        assertEquals("용아생가", themes2[4].title)
    }

    @Test
    fun getThemeById() = runTest {
        dao.insertThemes(entities)

        val theme = dao.getThemeById(themeId = 367, themeLangId = 1141)
        assertNotNull(theme)
        assertEquals("전통시장나들이", theme?.themeCategory)
        assertEquals("광주 양동시장", theme?.title)
    }

    @Test
    fun getThemesCountTest() = runTest {
        dao.insertThemes(entities)

        val count = dao.getThemesCount()
        assertEquals(5, count)
    }

    companion object {
        private const val METERS_PER_DEGREE = 111000.0

        private val entities = listOf(
            ThemeEntity(
                themeId = 367,
                themeLangId = 1141,
                themeCategory = "전통시장나들이",
                addr1 = "광주",
                addr2 = "",
                title = "광주 양동시장",
                mapX = 126.902957,
                mapY = 35.154165,
                langCheck = "1111",
                langCode = "ko",
                imageUrl = "https://example.com/image1.jpg",
                createdTime = "20190923194557",
                modifiedTime = "20191024155012"
            ),
            ThemeEntity(
                themeId = 610,
                themeLangId = 1452,
                themeCategory = "",
                addr1 = "광주",
                addr2 = "광산구",
                title = "월봉서원",
                mapX = 126.744865,
                mapY = 35.23571,
                langCheck = "1111",
                langCode = "ko",
                imageUrl = "https://example.com/image2.jpg",
                createdTime = "20190923194615",
                modifiedTime = "20191025221743"
            ),
            ThemeEntity(
                themeId = 611,
                themeLangId = 1456,
                themeCategory = "",
                addr1 = "광주",
                addr2 = "광산구",
                title = "너브실마을",
                mapX = 126.738165,
                mapY = 35.236174,
                langCheck = "1111",
                langCode = "ko",
                imageUrl = "https://example.com/image3.jpg",
                createdTime = "20190923194616",
                modifiedTime = "20191025221828"
            ),
            ThemeEntity(
                themeId = 612,
                themeLangId = 1460,
                themeCategory = "",
                addr1 = "광주",
                addr2 = "광산구",
                title = "칠송정",
                mapX = 126.742135,
                mapY = 35.236748,
                langCheck = "1111",
                langCode = "ko",
                imageUrl = "https://example.com/image4.jpg",
                createdTime = "20190923194616",
                modifiedTime = "20191025221921"
            ),
            ThemeEntity(
                themeId = 613,
                themeLangId = 1464,
                themeCategory = "",
                addr1 = "광주",
                addr2 = "광산구",
                title = "용아생가",
                mapX = 126.795216,
                mapY = 35.149407,
                langCheck = "1111",
                langCode = "ko",
                imageUrl = "https://example.com/image5.jpg",
                createdTime = "20190923194616",
                modifiedTime = "20230406161922"
            )
        )
    }
}