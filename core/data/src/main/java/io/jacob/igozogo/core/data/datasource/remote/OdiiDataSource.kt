package io.jacob.igozogo.core.data.datasource.remote

import io.jacob.igozogo.core.data.api.OdiiApi
import io.jacob.igozogo.core.data.mapper.toResponses
import io.jacob.igozogo.core.data.model.remote.odii.StoryResponse
import io.jacob.igozogo.core.data.model.remote.odii.ThemeResponse
import javax.inject.Inject

interface OdiiDataSource {
    suspend fun getThemeBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String = "ko",
    ): Result<List<ThemeResponse>>

    suspend fun getThemeLocationBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String = "ko",
        mapX: Double,
        mapY: Double,
        radius: Int,
    ): Result<List<ThemeResponse>>

    suspend fun getThemeSearchList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String = "ko",
        keyword: String,
    ): Result<List<ThemeResponse>>

    suspend fun getStoryBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String = "ko",
        themeId: Int,
        themeLangId: Int,
    ): Result<List<StoryResponse>>

    suspend fun getStoryLocationBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String = "ko",
        mapX: Double,
        mapY: Double,
        radius: Int,
    ): Result<List<StoryResponse>>

    suspend fun getStorySearchList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String = "ko",
        keyword: String,
    ): Result<List<StoryResponse>>
}

class OdiiDataSourceImpl @Inject constructor(
    private val api: OdiiApi
) : OdiiDataSource {
    override suspend fun getThemeBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String,
    ): Result<List<ThemeResponse>> {
        return runCatching {
            api.getThemeBasedList(
                numOfRows = numOfRows,
                pageNo = pageNo,
                langCode = langCode,
            ).toResponses()
        }
    }

    override suspend fun getThemeLocationBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String,
        mapX: Double,
        mapY: Double,
        radius: Int,
    ): Result<List<ThemeResponse>> {
        return runCatching {
            api.getThemeLocationBasedList(
                numOfRows = numOfRows,
                pageNo = pageNo,
                langCode = langCode,
                mapX = mapX,
                mapY = mapY,
                radius = radius,
            ).toResponses()
        }
    }

    override suspend fun getThemeSearchList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String,
        keyword: String,
    ): Result<List<ThemeResponse>> {
        return runCatching {
            api.getThemeSearchList(
                numOfRows = numOfRows,
                pageNo = pageNo,
                langCode = langCode,
                keyword = keyword,
            ).toResponses()
        }
    }

    override suspend fun getStoryBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String,
        themeId: Int,
        themeLangId: Int,
    ): Result<List<StoryResponse>> {
        return runCatching {
            api.getStoryBasedList(
                numOfRows = numOfRows,
                pageNo = pageNo,
                langCode = langCode,
                themeId = themeId,
                themeLangId = themeLangId,
            ).toResponses()
        }
    }

    override suspend fun getStoryLocationBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String,
        mapX: Double,
        mapY: Double,
        radius: Int,
    ): Result<List<StoryResponse>> {
        return runCatching {
            api.getStoryLocationBasedList(
                numOfRows = numOfRows,
                pageNo = pageNo,
                langCode = langCode,
                mapX = mapX,
                mapY = mapY,
                radius = radius,
            ).toResponses()
        }
    }

    override suspend fun getStorySearchList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String,
        keyword: String,
    ): Result<List<StoryResponse>> {
        return runCatching {
            api.getStorySearchList(
                numOfRows = numOfRows,
                pageNo = pageNo,
                langCode = langCode,
                keyword = keyword,
            ).toResponses()
        }
    }
}