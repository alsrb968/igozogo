package io.jacob.igozogo.core.data.api

import com.google.gson.Gson
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class OdiiApiTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val mockWebServerRule = MockWebServerRule()

    private val mockWebServer: MockWebServer
        get() = mockWebServerRule.mockWebServer
    private val gson: Gson
        get() = mockWebServerRule.gson
    private val odiiApi: OdiiApi
        get() = mockWebServerRule.odiiApi

    // Theme API 관련 테스트

    @Test
    fun `Given valid parameters When getThemeBasedList Then returns theme list successfully`() =
        runTest {
            // Given
            val mockThemeResponse = OdiiApiTestDataHelper.createSuccessfulThemeResponse()
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(gson.toJson(mockThemeResponse))
                    .addHeader("Content-Type", "application/json")
            )

            // When
            val response = odiiApi.getThemeBasedList(
                numOfRows = 10,
                pageNo = 1
            )

            // Then
            assertNotNull(response)
            assertEquals("0000", response.response.header.resultCode)
            assertEquals("OK", response.response.header.resultMsg)
            assertEquals(1, response.response.body.items.item.size)

            val theme = response.response.body.items.item.first()
            assertEquals(2812, theme.themeId)
            assertEquals(3826, theme.themeLangId)
            assertEquals("부산시 영도구 문화관광지", theme.title)
        }

    @Test
    fun `Given valid location parameters When getThemeLocationBasedList Then returns location-based theme list successfully`() =
        runTest {
            // Given
            val mockThemeResponse = OdiiApiTestDataHelper.createSuccessfulThemeResponse()
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(gson.toJson(mockThemeResponse))
                    .addHeader("Content-Type", "application/json")
            )

            // When
            val response = odiiApi.getThemeLocationBasedList(
                numOfRows = 10,
                pageNo = 1,
                mapX = 129.075323,
                mapY = 35.074916,
                radius = 10000
            )

            // Then
            assertNotNull(response)
            assertEquals("0000", response.response.header.resultCode)
            assertEquals(1, response.response.body.items.item.size)

            val theme = response.response.body.items.item.first()
            assertEquals(129.075323, theme.mapX, 0.0001)
            assertEquals(35.074916, theme.mapY, 0.0001)
        }

    @Test
    fun `Given valid keyword When getThemeSearchList Then returns search results successfully`() =
        runTest {
            // Given
            val mockThemeResponse =
                OdiiApiTestDataHelper.createFilteredThemeResponse(titleFilter = "부산")
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(gson.toJson(mockThemeResponse))
                    .addHeader("Content-Type", "application/json")
            )

            // When
            val response = odiiApi.getThemeSearchList(
                numOfRows = 10,
                pageNo = 1,
                keyword = "부산"
            )

            // Then
            assertNotNull(response)
            assertEquals("0000", response.response.header.resultCode)
            assert(response.response.body.items.item.isNotEmpty())

            // 모든 결과가 '부산' 키워드를 포함하는지 확인
            response.response.body.items.item.forEach { theme ->
                assert(
                    theme.title.contains("부산", ignoreCase = true) ||
                            theme.addr1.contains("부산", ignoreCase = true)
                )
            }
        }

    // Story API 관련 테스트

    @Test
    fun `Given valid theme parameters When getStoryBasedList Then returns story list successfully`() =
        runTest {
            // Given
            val mockStoryResponse = OdiiApiTestDataHelper.createSuccessfulStoryResponse()
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(gson.toJson(mockStoryResponse))
                    .addHeader("Content-Type", "application/json")
            )

            // When
            val response = odiiApi.getStoryBasedList(
                numOfRows = 10,
                pageNo = 1,
                themeId = 2885,
                themeLangId = 4462
            )

            // Then
            assertNotNull(response)
            assertEquals("0000", response.response.header.resultCode)
            assertEquals(1, response.response.body.items.item.size)

            val story = response.response.body.items.item.first()
            assertEquals(2885, story.themeId)
            assertEquals(4462, story.themeLangId)
            assertEquals(4976, story.storyId)
            assertEquals("무각사", story.title)
        }

    @Test
    fun `Given valid location parameters When getStoryLocationBasedList Then returns location-based story list successfully`() =
        runTest {
            // Given
            val mockStoryResponse = OdiiApiTestDataHelper.createSuccessfulStoryResponse()
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(gson.toJson(mockStoryResponse))
                    .addHeader("Content-Type", "application/json")
            )

            // When
            val response = odiiApi.getStoryLocationBasedList(
                numOfRows = 10,
                pageNo = 1,
                mapX = 126.8539117,
                mapY = 35.1529468,
                radius = 5000
            )

            // Then
            assertNotNull(response)
            assertEquals("0000", response.response.header.resultCode)
            assertEquals(1, response.response.body.items.item.size)

            val story = response.response.body.items.item.first()
            assertEquals(126.8539117, story.mapX, 0.0001)
            assertEquals(35.1529468, story.mapY, 0.0001)
            assertEquals(257, story.playTime) // 257000L -> 257 (seconds in response)
        }

    @Test
    fun `Given valid keyword When getStorySearchList Then returns search results successfully`() =
        runTest {
            // Given
            val mockStoryResponse =
                OdiiApiTestDataHelper.createFilteredStoryResponse(titleFilter = "무각사")
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(gson.toJson(mockStoryResponse))
                    .addHeader("Content-Type", "application/json")
            )

            // When
            val response = odiiApi.getStorySearchList(
                numOfRows = 10,
                pageNo = 1,
                keyword = "무각사"
            )

            // Then
            assertNotNull(response)
            assertEquals("0000", response.response.header.resultCode)
            assert(response.response.body.items.item.isNotEmpty())

            response.response.body.items.item.forEach { story ->
                assert(
                    story.title.contains("무각사", ignoreCase = true) ||
                            story.audioTitle.contains("무각사", ignoreCase = true)
                )
            }
        }

    // Error handling 테스트

    @Test
    fun `Given server error When calling any API Then throws appropriate exception`() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
        )

        // When & Then
        try {
            odiiApi.getThemeBasedList(10, 1)
            assert(false) { "Expected exception was not thrown" }
        } catch (e: Exception) {
            // Expected exception
            assert(true)
        }
    }

    @Test
    fun `Given malformed JSON When calling API Then throws parsing exception`() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{ invalid json }")
                .addHeader("Content-Type", "application/json")
        )

        // When & Then
        try {
            odiiApi.getThemeBasedList(10, 1)
            assert(false) { "Expected exception was not thrown" }
        } catch (e: Exception) {
            // Expected parsing exception
            assert(true)
        }
    }

    @Test
    fun `Given API error response When calling theme API Then returns error response`() = runTest {
        // Given
        val errorResponse =
            OdiiApiTestDataHelper.createErrorThemeResponse("1001", "Invalid Parameter")
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(gson.toJson(errorResponse))
                .addHeader("Content-Type", "application/json")
        )

        // When
        val response = odiiApi.getThemeBasedList(10, 1)

        // Then
        assertNotNull(response)
        assertEquals("1001", response.response.header.resultCode)
        assertEquals("Invalid Parameter", response.response.header.resultMsg)
        assertEquals(0, response.response.body.items.item.size)
    }

    @Test
    fun `Given empty result When calling theme API Then returns empty list`() = runTest {
        // Given
        val emptyResponse = OdiiApiTestDataHelper.createEmptyThemeResponse()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(gson.toJson(emptyResponse))
                .addHeader("Content-Type", "application/json")
        )

        // When
        val response = odiiApi.getThemeBasedList(10, 1)

        // Then
        assertNotNull(response)
        assertEquals("0000", response.response.header.resultCode)
        assertEquals(0, response.response.body.items.item.size)
        assertEquals(0, response.response.body.totalCount)
    }

    @Test
    fun `Given multiple theme items When calling theme API Then returns all items correctly`() =
        runTest {
            // Given
            val multipleItemsResponse = OdiiApiTestDataHelper.createSuccessfulThemeListResponse()
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(gson.toJson(multipleItemsResponse))
                    .addHeader("Content-Type", "application/json")
            )

            // When
            val response = odiiApi.getThemeBasedList(10, 1)

            // Then
            assertNotNull(response)
            assertEquals("0000", response.response.header.resultCode)
            assert(response.response.body.items.item.size >= 3) // TestData helper takes first 3 items

            // 각 아이템이 올바른 구조를 가지는지 확인
            response.response.body.items.item.forEach { theme ->
                assert(theme.themeId > 0)
                assert(theme.title.isNotBlank())
                assert(theme.langCode.isNotBlank())
            }
        }

    @Test
    fun `Given specific place ID When calling story API Then returns stories for that place`() =
        runTest {
            // Given
            val placeSpecificResponse =
                OdiiApiTestDataHelper.createFilteredStoryResponse(placeIdFilter = 2897)
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(gson.toJson(placeSpecificResponse))
                    .addHeader("Content-Type", "application/json")
            )

            // When
            val response = odiiApi.getStoryBasedList(
                numOfRows = 10,
                pageNo = 1,
                themeId = 2897,
                themeLangId = 4473
            )

            // Then
            assertNotNull(response)
            assertEquals("0000", response.response.header.resultCode)

            // 모든 스토리가 지정된 placeId를 가져야 함
            response.response.body.items.item.forEach { story ->
                assertEquals(2897, story.themeId)
            }
        }

    // Paging 테스트

    @Test
    fun `Given different page parameters When calling APIs Then returns correct pagination info`() =
        runTest {
            // Given
            val pageResponse = OdiiApiTestDataHelper.createSuccessfulThemeResponse(
                numOfRows = 20,
                pageNo = 2,
                totalCount = 100
            )
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(gson.toJson(pageResponse))
                    .addHeader("Content-Type", "application/json")
            )

            // When
            val response = odiiApi.getThemeBasedList(20, 2)

            // Then
            assertNotNull(response)
            assertEquals("0000", response.response.header.resultCode)
            assertEquals(20, response.response.body.numOfRows)
            assertEquals(2, response.response.body.pageNo)
            assertEquals(100, response.response.body.totalCount)
        }

}