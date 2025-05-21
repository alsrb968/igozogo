package io.jacob.igozogo.core.data.datasource.remote

import io.jacob.igozogo.core.data.api.OdiiApi
import io.jacob.igozogo.core.data.model.remote.Body
import io.jacob.igozogo.core.data.model.remote.odii.StoryResponse
import io.jacob.igozogo.core.data.model.remote.odii.ThemeResponse
import javax.inject.Inject

interface OdiiDataSource {
    suspend fun getThemeBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String = "ko",
    ): Body<ThemeResponse>

    suspend fun getThemeLocationBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String = "ko",
        mapX: Double,
        mapY: Double,
        radius: Int,
    ): Body<ThemeResponse>

    suspend fun getThemeSearchList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String = "ko",
        keyword: String,
    ): Body<ThemeResponse>

    suspend fun getStoryBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String = "ko",
        themeId: Int,
        themeLangId: Int,
    ): Body<StoryResponse>

    suspend fun getStoryLocationBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String = "ko",
        mapX: Double,
        mapY: Double,
        radius: Int,
    ): Body<StoryResponse>

    suspend fun getStorySearchList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String = "ko",
        keyword: String,
    ): Body<StoryResponse>
}

class OdiiDataSourceImpl @Inject constructor(
    private val api: OdiiApi
) : OdiiDataSource {
    override suspend fun getThemeBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String,
    ): Body<ThemeResponse> {
        return api.getThemeBasedList(
            numOfRows = numOfRows,
            pageNo = pageNo,
            langCode = langCode,
        ).response.body
    }

    override suspend fun getThemeLocationBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String,
        mapX: Double,
        mapY: Double,
        radius: Int,
    ): Body<ThemeResponse> {
        return api.getThemeLocationBasedList(
            numOfRows = numOfRows,
            pageNo = pageNo,
            langCode = langCode,
            mapX = mapX,
            mapY = mapY,
            radius = radius,
        ).response.body
    }

    override suspend fun getThemeSearchList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String,
        keyword: String,
    ): Body<ThemeResponse> {
        return api.getThemeSearchList(
            numOfRows = numOfRows,
            pageNo = pageNo,
            langCode = langCode,
            keyword = keyword,
        ).response.body
    }

    override suspend fun getStoryBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String,
        themeId: Int,
        themeLangId: Int,
    ): Body<StoryResponse> {
        return api.getStoryBasedList(
            numOfRows = numOfRows,
            pageNo = pageNo,
            langCode = langCode,
            themeId = themeId,
            themeLangId = themeLangId,
        ).response.body
    }

    override suspend fun getStoryLocationBasedList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String,
        mapX: Double,
        mapY: Double,
        radius: Int,
    ): Body<StoryResponse> {
        return api.getStoryLocationBasedList(
            numOfRows = numOfRows,
            pageNo = pageNo,
            langCode = langCode,
            mapX = mapX,
            mapY = mapY,
            radius = radius,
        ).response.body
    }

    override suspend fun getStorySearchList(
        numOfRows: Int,
        pageNo: Int,
        langCode: String,
        keyword: String,
    ): Body<StoryResponse> {
        return api.getStorySearchList(
            numOfRows = numOfRows,
            pageNo = pageNo,
            langCode = langCode,
            keyword = keyword,
        ).response.body
    }
}