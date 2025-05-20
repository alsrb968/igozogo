package io.jacob.igozogo.core.data.api

import io.jacob.igozogo.core.data.BuildConfig
import io.jacob.igozogo.core.data.model.odii.ResponseWrapper
import io.jacob.igozogo.core.data.model.odii.StoryResponse
import io.jacob.igozogo.core.data.model.odii.ThemeResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

private val commonQueryParams = mapOf(
    "MobileOS" to BuildConfig.TOURAPI_OS,
    "MobileApp" to BuildConfig.TOURAPI_APP_NAME,
    "serviceKey" to BuildConfig.TOURAPI_SERVICE_KEY,
    "_type" to "Json",
    "langCode" to "ko",
)

interface OdiiApi {
    /**
     * 관광지 기본 정보 목록 조회
     * @param numOfRows Int 한 페이지 결과 수
     * @param pageNo Int 현재 페이지 번호
     * @param commonParams Map<String, String> 공통 파라미터
     * @return ResponseWrapper<ThemeResponse>
     */
    @GET("Odii/themeBasedList")
    suspend fun getThemeBasedList(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @QueryMap commonParams: Map<String, String> = commonQueryParams,
    ): ResponseWrapper<ThemeResponse>

    /**
     * 관광지 위치 기반 정보 목록 조회
     * @param numOfRows Int 한 페이지 결과 수
     * @param pageNo Int 현재 페이지 번호
     * @param commonParams Map<String, String> 공통 파라미터
     * @param mapX Double GPS X좌표(WGS84 경도 좌표)
     * @param mapY Double GPS Y좌표(WGS84 위도 좌표)
     * @param radius Int 거리 반경(단위:m), Max값 20000m=20Km
     * @return ResponseWrapper<ThemeResponse>
     */
    @GET("Odii/themeLocationBasedList")
    suspend fun getThemeLocationBasedList(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @QueryMap commonParams: Map<String, String> = commonQueryParams,
        @Query("mapX") mapX: Double,
        @Query("mapY") mapY: Double,
        @Query("radius") radius: Int,
    ): ResponseWrapper<ThemeResponse>

    /**
     * 관광지 키워드 검색 목록 조회
     * @param numOfRows Int 한 페이지 결과 수
     * @param pageNo Int 현재 페이지 번호
     * @param commonParams Map<String, String> 공통 파라미터
     * @param keyword String 키워드
     * @return ResponseWrapper<ThemeResponse>
     */
    @GET("Odii/themeSearchList")
    suspend fun getThemeSearchList(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @QueryMap commonParams: Map<String, String> = commonQueryParams,
        @Query("keyword") keyword: String,
    ): ResponseWrapper<ThemeResponse>

    /**
     * 이야기 기본 정보 목록 조회
     * @param numOfRows Int 한 페이지 결과 수
     * @param pageNo Int 현재 페이지 번호
     * @param commonParams Map<String, String> 공통 파라미터
     * @param tid Int 관광지 ID
     * @param tlid Int 관광지 언어 ID
     * @return ResponseWrapper<StoryResponse>
     */
    @GET("Odii/storyBasedList")
    suspend fun getStoryBasedList(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @QueryMap commonParams: Map<String, String> = commonQueryParams,
        @Query("tid") tid: Int,
        @Query("tlid") tlid: Int,
    ): ResponseWrapper<StoryResponse>

    /**
     * 이야기 위치 기반 정보 목록 조회
     * @param numOfRows Int 한 페이지 결과 수
     * @param pageNo Int 현재 페이지 번호
     * @param commonParams Map<String, String> 공통 파라미터
     * @param mapX Double GPS X좌표(WGS84 경도 좌표)
     * @param mapY Double GPS Y좌표(WGS84 위도 좌표)
     * @param radius Int 거리 반경(단위:m), Max값 20000m=20Km
     * @return ResponseWrapper<StoryResponse>
     */
    @GET("Odii/storyLocationBasedList")
    suspend fun getStoryLocationBasedList(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @QueryMap commonParams: Map<String, String> = commonQueryParams,
        @Query("mapX") mapX: Double,
        @Query("mapY") mapY: Double,
        @Query("radius") radius: Int,
    ): ResponseWrapper<StoryResponse>

    /**
     * 이야기 키워드 검색 목록 조회
     * @param numOfRows Int 한 페이지 결과 수
     * @param pageNo Int 현재 페이지 번호
     * @param commonParams Map<String, String> 공통 파라미터
     * @param keyword String 키워드
     * @return ResponseWrapper<StoryResponse>
     */
    @GET("Odii/storySearchList")
    suspend fun getStorySearchList(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @QueryMap commonParams: Map<String, String> = commonQueryParams,
        @Query("keyword") keyword: String,
    ): ResponseWrapper<StoryResponse>
}