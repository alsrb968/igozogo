package io.jacob.igozogo.core.data.api

import io.jacob.igozogo.core.data.BuildConfig
import io.jacob.igozogo.core.data.model.odii.Response
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
        @Query("langCode") langCode: String = "ko"
    ): List<Response>
}