package io.jacob.igozogo.core.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.jacob.igozogo.core.data.db.ThemeDao
import io.jacob.igozogo.core.data.db.VisitKoreaDatabase
import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ThemeDaoInstrumentedTest {
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
    fun getThemesTest() = runTest {
        dao.insertThemes(entities)
        val result = dao.getThemes().first()
        assertEquals(3, result.size)
        assertEquals("백제문화단지", result[0].title)
        assertEquals("경주 불국사", result[1].title)
        assertEquals("괘릉", result[2].title)
    }

    @Test
    fun getThemeCategoriesTest() = runTest {
        dao.insertThemes(entities)
        val result = dao.getThemeCategories().first()
        assertEquals(2, result.size)
        assertEquals("백제역사여행", result[0])
        assertEquals("신라역사여행", result[1])
    }

    companion object {
        private val entities = listOf(
            ThemeEntity(
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
            ThemeEntity(
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
            ThemeEntity(
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
    }
}