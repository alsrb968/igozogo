package io.jacob.igozogo.core.data.api

import io.jacob.igozogo.core.data.BuildConfig
import io.jacob.igozogo.core.data.model.odii.ResponseWrapper
import io.jacob.igozogo.core.data.model.odii.StoryResponse
import io.jacob.igozogo.core.data.model.odii.ThemeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OdiiApi {
    @GET("Odii/themeBasedList")
    suspend fun getThemeBasedList(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") mobileOS: String = BuildConfig.TOURAPI_OS,
        @Query("MobileApp") mobileApp: String = BuildConfig.TOURAPI_APP_NAME,
        @Query("serviceKey") serviceKey: String = BuildConfig.TOURAPI_SERVICE_KEY,
        @Query("_type") type: String = "Json",
        @Query("langCode") langCode: String = "ko",
    ): ResponseWrapper<ThemeResponse>

    @GET("Odii/themeLocationBasedList")
    suspend fun getThemeLocationBasedList(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") mobileOS: String = BuildConfig.TOURAPI_OS,
        @Query("MobileApp") mobileApp: String = BuildConfig.TOURAPI_APP_NAME,
        @Query("serviceKey") serviceKey: String = BuildConfig.TOURAPI_SERVICE_KEY,
        @Query("_type") type: String = "Json",
        @Query("langCode") langCode: String = "ko",
        @Query("mapX") mapX: Double,
        @Query("mapY") mapY: Double,
        @Query("radius") radius: Int,
    ): ResponseWrapper<ThemeResponse>

    @GET("Odii/themeSearchList")
    suspend fun getThemeSearchList(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") mobileOS: String = BuildConfig.TOURAPI_OS,
        @Query("MobileApp") mobileApp: String = BuildConfig.TOURAPI_APP_NAME,
        @Query("serviceKey") serviceKey: String = BuildConfig.TOURAPI_SERVICE_KEY,
        @Query("_type") type: String = "Json",
        @Query("langCode") langCode: String = "ko",
        @Query("keyword") keyword: String,
    ): ResponseWrapper<ThemeResponse>

    @GET("Odii/storyBasedList")
    suspend fun getStoryBasedList(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") mobileOS: String = BuildConfig.TOURAPI_OS,
        @Query("MobileApp") mobileApp: String = BuildConfig.TOURAPI_APP_NAME,
        @Query("serviceKey") serviceKey: String = BuildConfig.TOURAPI_SERVICE_KEY,
        @Query("_type") type: String = "Json",
        @Query("langCode") langCode: String = "ko",
        @Query("tid") tid: Int,
        @Query("tlid") tlid: Int,
    ): ResponseWrapper<StoryResponse>

    @GET("Odii/storyLocationBasedList")
    suspend fun getStoryLocationBasedList(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") mobileOS: String = BuildConfig.TOURAPI_OS,
        @Query("MobileApp") mobileApp: String = BuildConfig.TOURAPI_APP_NAME,
        @Query("serviceKey") serviceKey: String = BuildConfig.TOURAPI_SERVICE_KEY,
        @Query("_type") type: String = "Json",
        @Query("langCode") langCode: String = "ko",
        @Query("mapX") mapX: Double,
        @Query("mapY") mapY: Double,
        @Query("radius") radius: Int,
    ): ResponseWrapper<StoryResponse>

    @GET("Odii/storySearchList")
    suspend fun getStorySearchList(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("MobileOS") mobileOS: String = BuildConfig.TOURAPI_OS,
        @Query("MobileApp") mobileApp: String = BuildConfig.TOURAPI_APP_NAME,
        @Query("serviceKey") serviceKey: String = BuildConfig.TOURAPI_SERVICE_KEY,
        @Query("_type") type: String = "Json",
        @Query("langCode") langCode: String = "ko",
        @Query("keyword") keyword: String,
    ): ResponseWrapper<StoryResponse>
}