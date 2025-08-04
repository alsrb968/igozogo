package io.jacob.igozogo.core.data.mapper

import io.jacob.igozogo.core.data.model.remote.Body
import io.jacob.igozogo.core.data.model.remote.Header
import io.jacob.igozogo.core.data.model.remote.Items
import io.jacob.igozogo.core.data.model.remote.Response
import io.jacob.igozogo.core.data.model.remote.ResponseWrapper
import io.jacob.igozogo.core.data.model.remote.odii.StoryResponse
import io.jacob.igozogo.core.data.model.remote.odii.ThemeResponse
import io.jacob.igozogo.core.testing.data.placeTestData
import io.jacob.igozogo.core.testing.data.storyTestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResponseWrapperMapperTest {

    private val testThemeResponse1 = placeTestData.first().toThemeResponse()
    private val testThemeResponse2 = placeTestData[1].toThemeResponse()
    
    private val testStoryResponse1 = storyTestData.first().toStoryResponse()
    private val testStoryResponse2 = storyTestData[1].toStoryResponse()

    @Test
    fun `Given ResponseWrapper with ThemeResponse items, When toResponses is called, Then return List of ThemeResponse`() {
        // Given
        val themeItems = listOf(testThemeResponse1, testThemeResponse2)
        val responseWrapper = ResponseWrapper(
            response = Response(
                header = Header(
                    resultCode = "0000",
                    resultMsg = "OK"
                ),
                body = Body(
                    items = Items(
                        item = themeItems
                    ),
                    numOfRows = 2,
                    pageNo = 1,
                    totalCount = 2
                )
            )
        )

        // When
        val responses = responseWrapper.toResponses()

        // Then
        assertEquals(2, responses.size)
        assertEquals(testThemeResponse1, responses[0])
        assertEquals(testThemeResponse2, responses[1])
    }

    @Test
    fun `Given ResponseWrapper with StoryResponse items, When toResponses is called, Then return List of StoryResponse`() {
        // Given
        val storyItems = listOf(testStoryResponse1, testStoryResponse2)
        val responseWrapper = ResponseWrapper(
            response = Response(
                header = Header(
                    resultCode = "0000",
                    resultMsg = "OK"
                ),
                body = Body(
                    items = Items(
                        item = storyItems
                    ),
                    numOfRows = 2,
                    pageNo = 1,
                    totalCount = 2
                )
            )
        )

        // When
        val responses = responseWrapper.toResponses()

        // Then
        assertEquals(2, responses.size)
        assertEquals(testStoryResponse1, responses[0])
        assertEquals(testStoryResponse2, responses[1])
    }

    @Test
    fun `Given ResponseWrapper with empty items list, When toResponses is called, Then return empty List`() {
        // Given
        val emptyItems: List<ThemeResponse> = emptyList()
        val responseWrapper = ResponseWrapper(
            response = Response(
                header = Header(
                    resultCode = "0000",
                    resultMsg = "OK"
                ),
                body = Body(
                    items = Items(
                        item = emptyItems
                    ),
                    numOfRows = 0,
                    pageNo = 1,
                    totalCount = 0
                )
            )
        )

        // When
        val responses = responseWrapper.toResponses()

        // Then
        assertTrue(responses.isEmpty())
    }

    @Test
    fun `Given ResponseWrapper with single item, When toResponses is called, Then return List with single item`() {
        // Given
        val singleItem = listOf(testThemeResponse1)
        val responseWrapper = ResponseWrapper(
            response = Response(
                header = Header(
                    resultCode = "0000",
                    resultMsg = "OK"
                ),
                body = Body(
                    items = Items(
                        item = singleItem
                    ),
                    numOfRows = 1,
                    pageNo = 1,
                    totalCount = 1
                )
            )
        )

        // When
        val responses = responseWrapper.toResponses()

        // Then
        assertEquals(1, responses.size)
        assertEquals(testThemeResponse1, responses[0])
    }

    @Test
    fun `Given ResponseWrapper with error header, When toResponses is called, Then still return items List`() {
        // Given
        val themeItems = listOf(testThemeResponse1)
        val responseWrapper = ResponseWrapper(
            response = Response(
                header = Header(
                    resultCode = "9999",
                    resultMsg = "ERROR"
                ),
                body = Body(
                    items = Items(
                        item = themeItems
                    ),
                    numOfRows = 1,
                    pageNo = 1,
                    totalCount = 1
                )
            )
        )

        // When
        val responses = responseWrapper.toResponses()

        // Then
        assertEquals(1, responses.size)
        assertEquals(testThemeResponse1, responses[0])
    }

    @Test
    fun `Given ResponseWrapper with large items list, When toResponses is called, Then return List with same size`() {
        // Given
        val largeItemsList = (1..100).map { index ->
            testThemeResponse1.copy(
                themeId = index,
                title = "테스트 장소 $index"
            )
        }
        val responseWrapper = ResponseWrapper(
            response = Response(
                header = Header(
                    resultCode = "0000",
                    resultMsg = "OK"
                ),
                body = Body(
                    items = Items(
                        item = largeItemsList
                    ),
                    numOfRows = 100,
                    pageNo = 1,
                    totalCount = 100
                )
            )
        )

        // When
        val responses = responseWrapper.toResponses()

        // Then
        assertEquals(100, responses.size)
        responses.forEachIndexed { index, response ->
            assertEquals(index + 1, response.themeId)
            assertEquals("테스트 장소 ${index + 1}", response.title)
        }
    }

    @Test
    fun `Given ResponseWrapper with mixed page info, When toResponses is called, Then return items regardless of page info`() {
        // Given
        val themeItems = listOf(testThemeResponse1, testThemeResponse2)
        val responseWrapper = ResponseWrapper(
            response = Response(
                header = Header(
                    resultCode = "0000",
                    resultMsg = "OK"
                ),
                body = Body(
                    items = Items(
                        item = themeItems
                    ),
                    numOfRows = 10, // Different from actual items size
                    pageNo = 5,
                    totalCount = 1000 // Different from actual items size
                )
            )
        )

        // When
        val responses = responseWrapper.toResponses()

        // Then
        assertEquals(2, responses.size) // Should return actual items, not numOfRows
        assertEquals(testThemeResponse1, responses[0])
        assertEquals(testThemeResponse2, responses[1])
    }

    @Test
    fun `Given ResponseWrapper with null-like values, When toResponses is called, Then return items with null-like values`() {
        // Given
        val themeWithNullLikeValues = testThemeResponse1.copy(
            themeCategory = "",
            addr1 = "",
            addr2 = "",
            title = "",
            imageUrl = ""
        )
        val themeItems = listOf(themeWithNullLikeValues)
        val responseWrapper = ResponseWrapper(
            response = Response(
                header = Header(
                    resultCode = "0000",
                    resultMsg = "OK"
                ),
                body = Body(
                    items = Items(
                        item = themeItems
                    ),
                    numOfRows = 1,
                    pageNo = 1,
                    totalCount = 1
                )
            )
        )

        // When
        val responses = responseWrapper.toResponses()

        // Then
        assertEquals(1, responses.size)
        val response = responses[0]
        assertEquals("", response.themeCategory)
        assertEquals("", response.addr1)
        assertEquals("", response.addr2)
        assertEquals("", response.title)
        assertEquals("", response.imageUrl)
    }

    @Test
    fun `Given ResponseWrapper with zero coordinates, When toResponses is called, Then return items with zero coordinates`() {
        // Given
        val themeWithZeroCoords = testThemeResponse1.copy(mapX = 0.0, mapY = 0.0)
        val themeItems = listOf(themeWithZeroCoords)
        val responseWrapper = ResponseWrapper(
            response = Response(
                header = Header(
                    resultCode = "0000",
                    resultMsg = "OK"
                ),
                body = Body(
                    items = Items(
                        item = themeItems
                    ),
                    numOfRows = 1,
                    pageNo = 1,
                    totalCount = 1
                )
            )
        )

        // When
        val responses = responseWrapper.toResponses()

        // Then
        assertEquals(1, responses.size)
        val response = responses[0]
        assertEquals(0.0, response.mapX, 0.0)
        assertEquals(0.0, response.mapY, 0.0)
    }
}