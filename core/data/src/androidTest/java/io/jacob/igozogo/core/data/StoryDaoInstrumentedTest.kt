package io.jacob.igozogo.core.data

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.jacob.igozogo.core.data.db.StoryDao
import io.jacob.igozogo.core.data.db.VisitKoreaDatabase
import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
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
        assertEquals(1, data.size)
        assertEquals("수로왕릉 - 정문", data[0].title)
    }

    @Test
    fun getStoriesByThemeIdTest() = runTest {
        dao.insertStories(entities)

        val pagingSource1 = dao.getStoriesByTheme(35, 119)
        val result1 = pagingSource1.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false,
            )
        )
        val data1 = (result1 as PagingSource.LoadResult.Page).data
        assertEquals(1, data1.size)

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

    companion object {
        private val entities = listOf(
            StoryEntity(
                themeId = 35,
                themeLangId = 119,
                storyId = 296,
                storyLangId = 670,
                title = "수로왕릉 - 정문",
                mapX = 128.8783827,
                mapY = 35.2353114,
                audioTitle = "인도의 문양, 쌍어문의 비밀",
                script = "인도의 문양, 쌍어문의 비밀  수로왕릉으로 들어가기 전에 커다란 정문을 살펴보고 갈까요? 이곳엔 수로왕의 왕비, 허왕후의 출신과 관련된 비밀스러운 문양이 그려져 있어요.  총 세 개로 나눠진 정문의 윗부분을 보시면 현판이 보이실 거예요. 그 현판 양옆, 그러니까 왼쪽과 오른쪽 문의 위쪽을 자세히 살펴보세요.   물고기 두 마리가 마주 보는 문양을 찾으셨나요?  네, 그게 바로 쌍어문이에요. 자세히 보지 않으면 그냥 지나치기 쉬운 이 작은 문양이 어디서 왔는지에 대해서는 다양한 의견들이 있어요.    그중 가장 크게 이슈가 됐던 주장은, 이 쌍어문이 수로왕의 왕비 허왕후가 그녀의 고향, 인도에서 가져온 문양이라는 거예요.  <삼국유사>에 기록된 허왕후 결혼 설화에 따르면 허왕후는 원래 인도 아유타국의 공주였답니다. 그러던 어느 날, 그녀의 부모님은 딸을 가야의 수로왕과 혼인시키라는 하늘의 계시를 받게 되었어요. 이에 허왕후는 고향에서부터 배를 타고 가야로 건너왔고, 수로왕과 결혼해 수로왕비가 되었다고 합니다.   허왕후의 고향이라는 아유타국은 지금의 인도 “아요디아”로 추정되고 있는데요.  현재, 쌍어문이 이 도시를 대표하는 문양으로 사용되고 있죠. 실제로 아요디아에 가면 학교 정문이나 경찰 모자, 택시 번호판 등에서 쉽게 쌍어문을 발견할 수 있다고 하네요.   하지만 이 주장에 반대하는 의견도 있어요. 물고기 문양은 불교 문화권에서 널리 쓰이는 문양이기 때문에, 조선시대 후기에 세워진 정문의 쌍어문은 후대 불교의 영향으로 새겨진 것으로 생각한답니다.  물론 이에 대해서도, 건물 자체는 후에 세워진 것이 맞지만 쌍어문 자체는 이 지역에서 오랫동안 전해 내려온 것이 아닌가 하는 이야기도 있어요.   이처럼 쌍어문에는 아직까지도 풀리지 않는 수수께끼들이 담겨있어 그 신비감을 더해주고 있답니다. ",
                playTime = 144,
                audioUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/audio/56/682.mp3",
                langCode = "ko",
                imageUrl = "",
                createdTime = "20150618171709",
                modifiedTime = "20240102121142",
            )
        )
    }
}