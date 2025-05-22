package io.jacob.igozogo.core.data.db

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StoryDaoInstrumentedTest {
    private lateinit var db: VisitKoreaDatabase
    private lateinit var dao: StoryDao

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, VisitKoreaDatabase::class.java)
            .build()
        dao = db.storyDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun getStoriesTest() = runTest {
        dao.insertStories(entities)

        val pagingSource = dao.getStories()
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
    fun getStoriesByThemeTest() = runTest {
        dao.insertStories(entities)

        val pagingSource1 = dao.getStoriesByTheme(2897, 4473)
        val result1 = pagingSource1.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false,
            )
        )
        val data1 = (result1 as PagingSource.LoadResult.Page).data
        assertEquals(8, data1.size)

        val pagingSource2 = dao.getStoriesByTheme(1, 1)
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

        val pagingSource1 = dao.getStoriesByLocation(mapX, mapY, 1000 / METERS_PER_DEGREE)
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

        val pagingSource2 = dao.getStoriesByLocation(mapX, mapY, 700 / METERS_PER_DEGREE)
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
    fun getStoriesByKeywordTest() = runTest {
        dao.insertStories(entities)

        val pagingSource1 = dao.getStoriesByKeyword("광주")
        val result1 = pagingSource1.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false,
            )
        )
        val data1 = (result1 as PagingSource.LoadResult.Page).data
        assertEquals(6, data1.size)

        val pagingSource2 = dao.getStoriesByKeyword("경복궁")
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

    companion object {
        private const val METERS_PER_DEGREE = 111000.0

        private val entities = listOf(
            StoryEntity(
                themeId = 2885,
                themeLangId = 4462,
                storyId = 4976,
                storyLangId = 15332,
                title = "무각사",
                mapX = 126.8539117,
                mapY = 35.1529468,
                audioTitle = "무각사",
                script = "광주시 서구 치평동 상무지구  상무지구는 현란한 네온사인과 미식가의 입맛을 돋울 다양한 종류의 먹을거리...",
                playTime = 257,
                audioUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/audio/56/15329.mp3",
                langCode = "ko",
                imageUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/image/service/10687.jpg",
                createdTime = "20230406162945",
                modifiedTime = "20230406162946"
            ),
            StoryEntity(
                themeId = 2897,
                themeLangId = 4473,
                storyId = 4987,
                storyLangId = 15351,
                title = "환영인사 및 비엔날레 특별노선 순환형 시티투어버스 소개",
                mapX = 126.856489,
                mapY = 35.152982,
                audioTitle = "환영인사 및 비엔날레 특별노선 순환형 시티투어버스 소개",
                script = "대한민국 예술여행대표도시 광주광역시를 찾아주신 여러분 환영합니다...",
                playTime = 153,
                audioUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/audio/56/15340.mp3",
                langCode = "ko",
                imageUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/image/service/10722.jpg",
                createdTime = "20230418153343",
                modifiedTime = "20230530110805"
            ),
            StoryEntity(
                themeId = 2897,
                themeLangId = 4473,
                storyId = 4989,
                storyLangId = 15354,
                title = "안전 사항 소개",
                mapX = 126.856489,
                mapY = 35.152982,
                audioTitle = "안전 사항 소개",
                script = "다음은 버스 탑승 시 안전유의 사항에 대해 안내해 드리겠습니다...",
                playTime = 55,
                audioUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/audio/56/15344.mp3",
                langCode = "ko",
                imageUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/image/service/10722.jpg",
                createdTime = "20230418153634",
                modifiedTime = "20230530110750"
            ),
            StoryEntity(
                themeId = 2897,
                themeLangId = 4473,
                storyId = 4990,
                storyLangId = 15357,
                title = "2023 광주 비엔날레 소개",
                mapX = 126.856489,
                mapY = 35.152982,
                audioTitle = "2023 광주 비엔날레 소개",
                script = "세계 3대 비엔날레의 위상을 갖춘 고품격 현대미술축제 [광주 비엔날레]...",
                playTime = 184,
                audioUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/audio/56/15345.mp3",
                langCode = "ko",
                imageUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/image/service/10722.jpg",
                createdTime = "20230418153834",
                modifiedTime = "20230530110737"
            ),
            StoryEntity(
                themeId = 2897,
                themeLangId = 4473,
                storyId = 4991,
                storyLangId = 15359,
                title = "파빌리온 전시관 소개",
                mapX = 126.856489,
                mapY = 35.152982,
                audioTitle = "파빌리온 전시관 소개",
                script = "[광주 비엔날레]는 주 전시 외에도 세계 유수의 문화 예술기관과 광주 지역의 예술기관이 연계해...",
                playTime = 105,
                audioUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/audio/56/15341.mp3",
                langCode = "ko",
                imageUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/image/service/10722.jpg",
                createdTime = "20230418154023",
                modifiedTime = "20230530110717"
            ),
            StoryEntity(
                themeId = 2897,
                themeLangId = 4473,
                storyId = 4992,
                storyLangId = 15361,
                title = "비엔날레 특별노선 순환형 시티투어버스 제휴할인 소개",
                mapX = 126.856489,
                mapY = 35.152982,
                audioTitle = "비엔날레 특별노선 순환형 시티투어버스 제휴할인 소개",
                script = "[비엔날레 특별노선 순환형 시티투어버스]를 이용해주시는 분들께 다양한 제휴할인 혜택을 제공하고 있습니다...",
                playTime = 53,
                audioUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/audio/56/15346.mp3",
                langCode = "ko",
                imageUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/image/service/10722.jpg",
                createdTime = "20230418154516",
                modifiedTime = "20230530110655"
            ),
            StoryEntity(
                themeId = 2897,
                themeLangId = 4473,
                storyId = 5015,
                storyLangId = 15407,
                title = "무각사 (비엔날레 관외전시관)",
                mapX = 126.856489,
                mapY = 35.152982,
                audioTitle = "무각사 (비엔날레 관외전시관) ",
                script = "다음역은 무각사 비엔날레 관외전시관입니다...",
                playTime = 159,
                audioUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/audio/56/15352.mp3",
                langCode = "ko",
                imageUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/image/service/10730.jpg",
                createdTime = "20230418170440",
                modifiedTime = "20230424110709"
            ),
            StoryEntity(
                themeId = 2897,
                themeLangId = 4473,
                storyId = 5017,
                storyLangId = 15411,
                title = "광주 송정역",
                mapX = 126.856489,
                mapY = 35.152982,
                audioTitle = "광주 송정역",
                script = "마지막 종착역은 광주 송정역입니다...",
                playTime = 143,
                audioUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/audio/56/15354.mp3",
                langCode = "ko",
                imageUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/image/service/10735.jpg",
                createdTime = "20230418170631",
                modifiedTime = "20230424110925"
            ),
            StoryEntity(
                themeId = 2897,
                themeLangId = 4473,
                storyId = 5018,
                storyLangId = 15413,
                title = "종착역 마지막 안내멘트",
                mapX = 126.856489,
                mapY = 35.152982,
                audioTitle = "종착역 마지막 안내멘트",
                script = "곧 마지막 종착역 광주 송정역에 도착합니다...",
                playTime = 30,
                audioUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/audio/56/15355.mp3",
                langCode = "ko",
                imageUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/image/service/10722.jpg",
                createdTime = "20230418170714",
                modifiedTime = "20230530110625"
            ),
            StoryEntity(
                themeId = 2950,
                themeLangId = 4605,
                storyId = 5248,
                storyLangId = 16246,
                title = "5.18 기념 공원",
                mapX = 126.8572119,
                mapY = 35.1556848,
                audioTitle = "5.18 기념 공원",
                script = "오늘날 우리나라가 민주주의 국가로 발전할 수 있었던 가장 큰 이유는 앞선 시대의 많은 사람들의 희생이 있었기 때문입니다...",
                playTime = 192,
                audioUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/audio/56/16173.mp3",
                langCode = "ko",
                imageUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/image/service/11132.jpg",
                createdTime = "20230719161804",
                modifiedTime = "20230719161805"
            )
        )
    }
}