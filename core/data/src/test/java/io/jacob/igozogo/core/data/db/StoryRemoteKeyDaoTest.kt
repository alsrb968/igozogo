package io.jacob.igozogo.core.data.db

import io.jacob.igozogo.core.data.model.local.odii.StoryRemoteKey
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StoryRemoteKeyDaoTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val dbRule = RoomDatabaseRule()

    private lateinit var dao: StoryRemoteKeyDao

    @Before
    fun setup() {
        dao = dbRule.db.storyRemoteKeyDao()
    }

    @Test
    fun getRemoteKeyTest() = runTest {
        dao.insertRemoteKeys(entities)
        val result1 = dao.getRemoteKey(1, "test1")
        val result2 = dao.getRemoteKey(1, "test2")
        assertNotNull(result1)
        assertEquals(1, result1?.id)
        assertEquals(null, result2)
    }

    @Test
    fun deleteRemoteKeysByQueryTypeTest() = runTest {
        dao.insertRemoteKeys(entities)
        dao.deleteRemoteKeysByQueryType("test1")
        val result1 = dao.getRemoteKey(1, "test1")
        val result2 = dao.getRemoteKey(2, "test2")
        val result3 = dao.getRemoteKey(3, "test3")
        assertEquals(null, result1)
        assertEquals(2, result2?.id)
        assertEquals(3, result3?.id)
    }

    companion object {
        private val entities = listOf(
            StoryRemoteKey(
                id = 1,
                queryType = "test1",
                prevPage = 0,
                nextPage = 2
            ),
            StoryRemoteKey(
                id = 2,
                queryType = "test2",
                prevPage = 1,
                nextPage = 3
            ),
            StoryRemoteKey(
                id = 3,
                queryType = "test3",
                prevPage = 2,
                nextPage = null
            ),
        )
    }
}