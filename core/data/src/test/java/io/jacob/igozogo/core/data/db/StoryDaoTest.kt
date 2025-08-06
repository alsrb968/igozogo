package io.jacob.igozogo.core.data.db

import androidx.paging.PagingSource
import io.jacob.igozogo.core.data.mapper.asStoryEntities
import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import io.jacob.igozogo.core.testing.data.storyTestData
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StoryDaoTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val dbRule = RoomDatabaseRule()

    private lateinit var dao: StoryDao

    @Before
    fun setup() {
        dao = dbRule.db.storyDao()
    }

    @Test
    fun getStoriesPagingSourceTest() = runTest {
        dao.insertStories(entities)

        val pagingSource = dao.getStoriesPagingSource()
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false,
            )
        )
        val data = (result as PagingSource.LoadResult.Page).data
        assertEquals(10, data.size)
        assertEquals("무각사", data[0].title)
    }

    @Test
    fun getStoriesTest() = runTest {
        dao.insertStories(entities)

        val stories = dao.getStories(10)
        assertEquals(10, stories.size)
        assertEquals("무각사", stories[0].title)
    }

    @Test
    fun getStoriesByThemePagingSourceTest() = runTest {
        dao.insertStories(entities)

        val pagingSource1 = dao.getStoriesByThemePagingSource(2897, 4473)
        val result1 = pagingSource1.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false,
            )
        )
        val data1 = (result1 as PagingSource.LoadResult.Page).data
        assertEquals(8, data1.size)

        val pagingSource2 = dao.getStoriesByThemePagingSource(1, 1)
        val result2 = pagingSource2.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false,
            )
        )
        val data2 = (result2 as PagingSource.LoadResult.Page).data
        assertEquals(0, data2.size)
    }

    @Test
    fun getStoriesByThemeTest() = runTest {
        dao.insertStories(entities)

        val stories1 = dao.getStoriesByTheme(2897, 4473)
        assertEquals(8, stories1.size)

        val stories2 = dao.getStoriesByTheme(1, 1)
        assertEquals(0, stories2.size)
    }

    @Test
    fun getStoriesByLocationPagingSourceTest() = runTest {
        dao.insertStories(entities)

        val mapX = 126.852601
        val mapY = 35.159545

        fun isSortedAscByDistance(
            entities: List<StoryEntity>
        ): Boolean {
            val distances = entities.map { entity ->
                val dx = entity.mapX - mapX
                val dy = entity.mapY - mapY
                dx * dx + dy * dy // sqrt 없이 제곱 거리 비교 (더 빠름)
            }
            return distances == distances.sorted()
        }

        val pagingSource1 = dao.getStoriesByLocationPagingSource(mapX, mapY, 1000 / METERS_PER_DEGREE)
        val result1 = pagingSource1.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false,
            )
        )
        val data1 = (result1 as PagingSource.LoadResult.Page).data
        assertEquals(10, data1.size)
        assertTrue(isSortedAscByDistance(data1))

        val pagingSource2 = dao.getStoriesByLocationPagingSource(mapX, mapY, 700 / METERS_PER_DEGREE)
        val result2 = pagingSource2.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false,
            )
        )
        val data2 = (result2 as PagingSource.LoadResult.Page).data
        assertEquals(1, data2.size)
    }

    @Test
    fun getStoriesByLocationTest() = runTest {
        dao.insertStories(entities)

        val mapX = 126.852601
        val mapY = 35.159545

        fun isSortedAscByDistance(
            entities: List<StoryEntity>
        ): Boolean {
            val distances = entities.map { entity ->
                val dx = entity.mapX - mapX
                val dy = entity.mapY - mapY
                dx * dx + dy * dy // sqrt 없이 제곱 거리 비교 (더 빠름)
            }
            return distances == distances.sorted()
        }

        val stories1 = dao.getStoriesByLocation(mapX, mapY, 1000 / METERS_PER_DEGREE, 10)
        assertEquals(10, stories1.size)
        assertTrue(isSortedAscByDistance(stories1))

        val stories2 = dao.getStoriesByLocation(mapX, mapY, 700 / METERS_PER_DEGREE, 10)
        assertEquals(1, stories2.size)
    }

    @Test
    fun getStoriesByKeywordPagingSourceTest() = runTest {
        dao.insertStories(entities)

        val pagingSource1 = dao.getStoriesByKeywordPagingSource("광주")
        val result1 = pagingSource1.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false,
            )
        )
        val data1 = (result1 as PagingSource.LoadResult.Page).data
        assertEquals(6, data1.size)

        val pagingSource2 = dao.getStoriesByKeywordPagingSource("경복궁")
        val result2 = pagingSource2.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false,
            )
        )
        val data2 = (result2 as PagingSource.LoadResult.Page).data
        assertEquals(0, data2.size)
    }

    @Test
    fun getStoriesByKeywordTest() = runTest {
        dao.insertStories(entities)

        val stories1 = dao.getStoriesByKeyword("광주", 10)
        assertEquals(6, stories1.size)

        val stories2 = dao.getStoriesByKeyword("경복궁", 10)
        assertEquals(0, stories2.size)
    }

    @Test
    fun getStoryByIdTest() = runTest {
        dao.insertStories(entities)

        val story = dao.getStoryById(storyId = 4976, storyLangId = 15332)
        assertNotNull(story)
        assertEquals("무각사", story?.title)
        assertEquals("무각사", story?.audioTitle)
    }

    companion object {
        private const val METERS_PER_DEGREE = 111000.0

        private val entities = storyTestData.asStoryEntities()
    }
}