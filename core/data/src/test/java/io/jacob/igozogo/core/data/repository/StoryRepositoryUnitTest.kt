package io.jacob.igozogo.core.data.repository

import io.jacob.igozogo.core.data.TestPagingSource
import io.jacob.igozogo.core.data.datasource.local.StoryDataSource
import io.jacob.igozogo.core.data.mapper.toPlace
import io.jacob.igozogo.core.data.mapper.toStoryEntity
import io.jacob.igozogo.core.data.mapper.toThemeEntity
import io.jacob.igozogo.core.data.model.remote.odii.StoryResponse
import io.jacob.igozogo.core.data.model.remote.odii.ThemeResponse
import io.jacob.igozogo.core.domain.repository.StoryRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StoryRepositoryUnitTest {
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val storyDataSource = mockk<StoryDataSource>()
    private val storyRemoteMediatorFactory = mockk<StoryRemoteMediator.Factory>()

    private lateinit var repository: StoryRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = StoryRepositoryImpl(
            storyDataSource = storyDataSource,
            storyRemoteMediatorFactory = storyRemoteMediatorFactory,
        )
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Given RemoteMediator, When getStoriesByPlace called, Then call dataSource`() =
        testScope.runTest {
            // Given
            val fakeRemoteMediator = mockk<StoryRemoteMediator>(relaxed = true)
            every { storyRemoteMediatorFactory.create(any()) } returns fakeRemoteMediator
            every {
                storyDataSource.getStoriesByTheme(any(), any())
            } returns TestPagingSource(storyEntities)

            // When
            val flow = repository.getStoriesByPlace(place = place, pageSize = 10)

            val job = launch { flow.collectLatest { pagingData -> } }

            // Then
            advanceUntilIdle()
            job.cancel()
            coVerify { storyDataSource.getStoriesByTheme(any(), any()) }
        }

    @Test
    fun `Given RemoteMediator, When getStoriesByLocation called, Then call dataSource`() =
        testScope.runTest {
            // Given
            val fakeRemoteMediator = mockk<StoryRemoteMediator>(relaxed = true)
            every { storyRemoteMediatorFactory.create(any()) } returns fakeRemoteMediator
            every {
                storyDataSource.getStoriesByLocation(any(), any(), any())
            } returns TestPagingSource(storyEntities)

            // When
            val flow = repository.getStoriesByLocation(1.0, 1.0, 1)

            val job = launch { flow.collectLatest { pagingData -> } }

            // Then
            advanceUntilIdle()
            job.cancel()
            coVerify { storyDataSource.getStoriesByLocation(any(), any(), any()) }
        }

    @Test
    fun `Given RemoteMediator, When getStoriesByKeyword called, Then call dataSource`() =
        testScope.runTest {
            // Given
            val fakeRemoteMediator = mockk<StoryRemoteMediator>(relaxed = true)
            every { storyRemoteMediatorFactory.create(any()) } returns fakeRemoteMediator
            every {
                storyDataSource.getStoriesByKeyword(any())
            } returns TestPagingSource(storyEntities)

            // When
            val flow = repository.getStoriesByKeyword("test", 1)

            val job = launch { flow.collectLatest { pagingData -> } }

            // Then
            advanceUntilIdle()
            job.cancel()
            coVerify { storyDataSource.getStoriesByKeyword(any()) }
        }

    companion object {
        private val storyResponses = listOf(
            StoryResponse(
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
            StoryResponse(
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
            StoryResponse(
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
        )

        private val storyEntities = storyResponses.toStoryEntity()

        private val place = ThemeResponse(
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
        ).toThemeEntity().toPlace()
    }
}