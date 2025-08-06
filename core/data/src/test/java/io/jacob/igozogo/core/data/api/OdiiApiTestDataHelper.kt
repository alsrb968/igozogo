package io.jacob.igozogo.core.data.api

import io.jacob.igozogo.core.data.mapper.toStoryResponses
import io.jacob.igozogo.core.data.mapper.toThemeResponses
import io.jacob.igozogo.core.data.model.remote.*
import io.jacob.igozogo.core.data.model.remote.odii.StoryResponse
import io.jacob.igozogo.core.data.model.remote.odii.ThemeResponse
import io.jacob.igozogo.core.testing.data.placeTestData
import io.jacob.igozogo.core.testing.data.storyTestData

/**
 * OdiiApi 테스트를 위한 Mock Response 생성 헬퍼
 * Testing 모듈의 TestData를 기반으로 JSON 응답을 생성합니다.
 */
object OdiiApiTestDataHelper {

    /**
     * 성공적인 응답을 위한 Header 생성
     */
    private fun createSuccessHeader() = Header(
        resultCode = "0000",
        resultMsg = "OK"
    )

    /**
     * 에러 응답을 위한 Header 생성
     */
    private fun createErrorHeader(code: String = "9999", message: String = "Error") = Header(
        resultCode = code,
        resultMsg = message
    )

    /**
     * PlaceTestData를 기반으로 ThemeResponse 목록 생성
     */
    fun createThemeResponsesFromTestData(): List<ThemeResponse> {
        return placeTestData.toThemeResponses()
    }

    /**
     * StoryTestData를 기반으로 StoryResponse 목록 생성
     */
    fun createStoryResponsesFromTestData(): List<StoryResponse> {
        return storyTestData.toStoryResponses()
    }

    /**
     * 성공적인 Theme 응답 생성 (단일 아이템)
     */
    fun createSuccessfulThemeResponse(
        item: ThemeResponse = createThemeResponsesFromTestData().first(),
        numOfRows: Int = 10,
        pageNo: Int = 1,
        totalCount: Int = 1
    ): ResponseWrapper<ThemeResponse> {
        return ResponseWrapper(
            response = Response(
                header = createSuccessHeader(),
                body = Body(
                    items = Items(item = listOf(item)),
                    numOfRows = numOfRows,
                    pageNo = pageNo,
                    totalCount = totalCount
                )
            )
        )
    }

    /**
     * 성공적인 Theme 응답 생성 (다중 아이템)
     */
    fun createSuccessfulThemeListResponse(
        items: List<ThemeResponse> = createThemeResponsesFromTestData().take(3),
        numOfRows: Int = 10,
        pageNo: Int = 1,
        totalCount: Int = items.size
    ): ResponseWrapper<ThemeResponse> {
        return ResponseWrapper(
            response = Response(
                header = createSuccessHeader(),
                body = Body(
                    items = Items(item = items),
                    numOfRows = numOfRows,
                    pageNo = pageNo,
                    totalCount = totalCount
                )
            )
        )
    }

    /**
     * 성공적인 Story 응답 생성 (단일 아이템)
     */
    fun createSuccessfulStoryResponse(
        item: StoryResponse = createStoryResponsesFromTestData().first(),
        numOfRows: Int = 10,
        pageNo: Int = 1,
        totalCount: Int = 1
    ): ResponseWrapper<StoryResponse> {
        return ResponseWrapper(
            response = Response(
                header = createSuccessHeader(),
                body = Body(
                    items = Items(item = listOf(item)),
                    numOfRows = numOfRows,
                    pageNo = pageNo,
                    totalCount = totalCount
                )
            )
        )
    }

    /**
     * 성공적인 Story 응답 생성 (다중 아이템)
     */
    fun createSuccessfulStoryListResponse(
        items: List<StoryResponse> = createStoryResponsesFromTestData().take(3),
        numOfRows: Int = 10,
        pageNo: Int = 1,
        totalCount: Int = items.size
    ): ResponseWrapper<StoryResponse> {
        return ResponseWrapper(
            response = Response(
                header = createSuccessHeader(),
                body = Body(
                    items = Items(item = items),
                    numOfRows = numOfRows,
                    pageNo = pageNo,
                    totalCount = totalCount
                )
            )
        )
    }

    /**
     * 빈 결과 Theme 응답 생성
     */
    fun createEmptyThemeResponse(
        numOfRows: Int = 10,
        pageNo: Int = 1
    ): ResponseWrapper<ThemeResponse> {
        return ResponseWrapper(
            response = Response(
                header = createSuccessHeader(),
                body = Body(
                    items = Items(item = emptyList()),
                    numOfRows = numOfRows,
                    pageNo = pageNo,
                    totalCount = 0
                )
            )
        )
    }

    /**
     * 빈 결과 Story 응답 생성
     */
    fun createEmptyStoryResponse(
        numOfRows: Int = 10,
        pageNo: Int = 1
    ): ResponseWrapper<StoryResponse> {
        return ResponseWrapper(
            response = Response(
                header = createSuccessHeader(),
                body = Body(
                    items = Items(item = emptyList()),
                    numOfRows = numOfRows,
                    pageNo = pageNo,
                    totalCount = 0
                )
            )
        )
    }

    /**
     * 에러 Theme 응답 생성
     */
    fun createErrorThemeResponse(
        resultCode: String = "9999",
        resultMsg: String = "System Error"
    ): ResponseWrapper<ThemeResponse> {
        return ResponseWrapper(
            response = Response(
                header = createErrorHeader(resultCode, resultMsg),
                body = Body(
                    items = Items(item = emptyList()),
                    numOfRows = 0,
                    pageNo = 0,
                    totalCount = 0
                )
            )
        )
    }

    /**
     * 에러 Story 응답 생성
     */
    fun createErrorStoryResponse(
        resultCode: String = "9999",
        resultMsg: String = "System Error"
    ): ResponseWrapper<StoryResponse> {
        return ResponseWrapper(
            response = Response(
                header = createErrorHeader(resultCode, resultMsg),
                body = Body(
                    items = Items(item = emptyList()),
                    numOfRows = 0,
                    pageNo = 0,
                    totalCount = 0
                )
            )
        )
    }

    /**
     * 특정 조건으로 필터링된 Theme 응답 생성
     */
    fun createFilteredThemeResponse(
        address1Filter: String? = null,
        titleFilter: String? = null,
        numOfRows: Int = 10,
        pageNo: Int = 1
    ): ResponseWrapper<ThemeResponse> {
        var filteredItems = createThemeResponsesFromTestData()

        address1Filter?.let { filter ->
            filteredItems = filteredItems.filter { it.addr1.contains(filter, ignoreCase = true) }
        }

        titleFilter?.let { filter ->
            filteredItems = filteredItems.filter { it.title.contains(filter, ignoreCase = true) }
        }

        return ResponseWrapper(
            response = Response(
                header = createSuccessHeader(),
                body = Body(
                    items = Items(item = filteredItems),
                    numOfRows = numOfRows,
                    pageNo = pageNo,
                    totalCount = filteredItems.size
                )
            )
        )
    }

    /**
     * 특정 조건으로 필터링된 Story 응답 생성
     */
    fun createFilteredStoryResponse(
        titleFilter: String? = null,
        placeIdFilter: Int? = null,
        numOfRows: Int = 10,
        pageNo: Int = 1
    ): ResponseWrapper<StoryResponse> {
        var filteredItems = createStoryResponsesFromTestData()

        titleFilter?.let { filter ->
            filteredItems = filteredItems.filter { it.title.contains(filter, ignoreCase = true) }
        }

        placeIdFilter?.let { filter ->
            filteredItems = filteredItems.filter { it.themeId == filter }
        }

        return ResponseWrapper(
            response = Response(
                header = createSuccessHeader(),
                body = Body(
                    items = Items(item = filteredItems),
                    numOfRows = numOfRows,
                    pageNo = pageNo,
                    totalCount = filteredItems.size
                )
            )
        )
    }
}