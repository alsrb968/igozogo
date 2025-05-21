package io.jacob.igozogo.core.data.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.jacob.igozogo.core.data.model.local.odii.StoryRemoteKey
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StoryRemoteKeyDaoInstrumentedTest {
    private lateinit var db: VisitKoreaDatabase
    private lateinit var dao: StoryRemoteKeyDao

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, VisitKoreaDatabase::class.java)
            .build()
        dao = db.storyRemoteKeyDao()
    }

    @After
    fun teardown() {
        db.close()
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