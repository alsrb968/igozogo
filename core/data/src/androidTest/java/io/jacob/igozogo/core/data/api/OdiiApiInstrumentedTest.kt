package io.jacob.igozogo.core.data.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OdiiApiInstrumentedTest {
    private lateinit var api: OdiiApi

    @Before
    fun setup() {
        api = VisitKoreaClient.retrofit.create(OdiiApi::class.java)
    }

    @Test
    fun getThemeBasedListTest() = runTest {
        val numOfRows = 2

        val response = api.getThemeBasedList(
            numOfRows = numOfRows,
            pageNo = 1,
        )

        Assert.assertNotNull(response)
        Assert.assertTrue(numOfRows == response.response.body.items.item.size)
    }

    @Test
    fun getThemeLocationBasedListTest() = runTest {
        val numOfRows = 2

        val response = api.getThemeLocationBasedList(
            numOfRows = numOfRows,
            pageNo = 1,
            mapX = 126.852601,
            mapY = 35.159545,
            radius = 1000,
        )

        Assert.assertNotNull(response)
        Assert.assertTrue(numOfRows == response.response.body.items.item.size)
    }

    @Test
    fun getThemeSearchListTest() = runTest {
        val numOfRows = 2

        val response = api.getThemeSearchList(
            numOfRows = numOfRows,
            pageNo = 1,
            keyword = "백제",
        )

        Assert.assertNotNull(response)
        Assert.assertTrue(numOfRows == response.response.body.items.item.size)
    }

    @Test
    fun getStoryBasedListTest() = runTest {
        val numOfRows = 2

        val response = api.getStoryBasedList(
            numOfRows = numOfRows,
            pageNo = 1,
            themeId = 1,
            themeLangId = 1,
        )

        Assert.assertNotNull(response)
        Assert.assertTrue(numOfRows == response.response.body.items.item.size)
    }

    @Test
    fun getStoryLocationBasedListTest() = runTest {
        val numOfRows = 2

        val response = api.getStoryLocationBasedList(
            numOfRows = numOfRows,
            pageNo = 1,
            mapX = 126.852601,
            mapY = 35.159545,
            radius = 1000,
        )

        Assert.assertNotNull(response)
        Assert.assertTrue(numOfRows == response.response.body.items.item.size)
    }

    @Test
    fun getStorySearchListTest() = runTest {
        val numOfRows = 2

        val response = api.getStorySearchList(
            numOfRows = numOfRows,
            pageNo = 1,
            keyword = "백제",
        )

        Assert.assertNotNull(response)
        Assert.assertTrue(numOfRows == response.response.body.items.item.size)
    }
}