package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.repository.PlaceRepository
import io.jacob.igozogo.core.domain.repository.StoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPlaceAndStoriesByIdUseCaseUnitTest {
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val placeRepository = mockk<PlaceRepository>()
    private val storyRepository = mockk<StoryRepository>()

    private lateinit var usecase: GetPlaceAndStoriesByIdUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        usecase = GetPlaceAndStoriesByIdUseCase(
            placeRepository = placeRepository,
            storyRepository = storyRepository,
        )
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Given place, When GetPlaceAndStoriesByIdUseCase called, Then call repositories`() =
        testScope.runTest {
            // Given
            coEvery { placeRepository.getPlaceById(any(), any()) } returns testPlace
            coEvery { storyRepository.getStoriesByPlace(any()) } returns testStories

            // When
            val result = usecase(1, 1)

            // Then
            assertTrue(result.isSuccess)
            assertEquals(testPlace to testStories, result.getOrNull())
            coVerify { placeRepository.getPlaceById(any(), any()) }
            coVerify { storyRepository.getStoriesByPlace(any()) }
        }

    @Test
    fun `Given null place, When GetPlaceAndStoriesByIdUseCase called, Then call repositories`() =
        testScope.runTest {
            // Given
            coEvery { placeRepository.getPlaceById(any(), any()) } returns null

            // When
            val result = usecase(1, 1)

            // Then
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is NullPointerException)
            coVerify { placeRepository.getPlaceById(any(), any()) }
            coVerify(exactly = 0) { storyRepository.getStoriesByPlace(any()) }
        }

    companion object {
        private val testPlace = Place(
            placeId = 3375,
            placeLangId = 5093,
            placeCategory = "고궁 스토리텔링",
            address1 = "서울",
            address2 = "종로구",
            title = "경복궁",
            mapX = 126.97704,
            mapY = 37.579617,
            langCheck = "1000",
            langCode = "ko",
            imageUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/sightImage/service/3375.jpg",
            createdTime = "20240125120046",
            modifiedTime = "20240125120100",
        )

        private val testStory = Story(
            placeId = 2573,
            placeLangId = 2573,
            storyId = 5379,
            storyLangId = 16377,
            title = "광화문",
            mapX = 126.976869,
            mapY = 37.576011,
            audioTitle = "경복궁의 정문",
            script = "\"[김내관] 왕의 큰 덕이 온 나라를 비추는 광화문(光化門)에 오신 것을 환영합니다. 경복궁 남쪽의 정문인 광화문은 조선왕실의 상징적인 역할을 하는 문입니다. 조선시대에는 문을 기준으로 안 쪽은 경복궁, 바깥쪽은 ‘육조거리’라 불리는 한성부(지금의 서울시청 역할을 하는 부서)의 영역과 백성들이 생활하는 종로거리가 있었습니다. 모두가 지나다니는 중심에 있는 광화문은 다락위에 종과 북을 달아서 시간과 소식을 알려주는 역할도 했습니다.  [김상궁] “문 앞에 해치(해태) 한 쌍이 마치 ‘나는 정의를 수호한다!’라고 눈빛으로 말하는 것 같아요.”  [김내관] “그렇지요? 아마 이 해치가 육조거리에 있는 관리들과 임금님이 바른 정치를 하도록 지켜보고 있다-- 는 의미로 세운 거라 그런 느낌이 들 거에요.”  [김상궁] “해치가 양 옆에 있으니 든든하네요.! 와- 김내관! 나 이 문 처음 지나봐요! 천장에 봉황이 그려져 있네요!”  [김내관] “정말 멋지네요! 원래 광화문은 중국에서 온 사신이나 왕실가족이 공식적으로 행차할 때 이용하는 문이지요. 저도 옆 문으로는 많이 다녔는데, 가운데 문으로 들어와 본 건 처음이라 신기하네요!. 참고로, 왼쪽 문은 낙귀부서(洛龜負書)로 거북이가 그려져있고, 오른쪽 문은 하마부서(河馬負書)로 말이 그려져 있어요  [김상궁]  “광화문이 2010년에 여기로 돌아오기 까지 얼마나 고생이 많았는데, 당연히 멋지게 단장해야지요.! 광화문은 일제강점기 때 조선총독부 건물에 밀려서 건춘문 근처로 갔다가, 한국 전쟁 때 폭격을 맞아 없어졌지요. 1968년에야 다시 복원을 했지만, 1990년대에 중앙청으로 불리던 조선총독부 건물이 없어지고 나서야 원래 자리로 오게 된 거잖아요.  [김내관] “그래서 복원할 때 추녀마루에 잡상(어처구니)도 7개 딱! 올리고, 뒤로 보이는 북악산의 능선과 어우러지는 미적 요소도 딱! 살려놨지요! 예나 지금이나, 이 광화문이 여기에 있다는 것만으로 얼마나 마음이 벅차 오르는지, 다들 알고 있을거에요!”  [김상궁] “어머, 웬일로 나랑 통했네요! 그러면, 이제 안에 가서 입장권 끊고 본격적으로 입궐해보자구요!” \"",
            playTime = 158,
            audioUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/audio/56/7074.mp3",
            langCode = "ko",
            imageUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/image/service/5352.jpg",
            createdTime = "20230725102801",
            modifiedTime = "20250609093610"
        )

        private val testStories = List(10) {testStory }
    }
}