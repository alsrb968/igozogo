package io.jacob.igozogo.core.data.model.odii

import com.google.gson.annotations.SerializedName

/**
 * 관광지 응답
 * @property themeId Int 관광지 ID
 * @property themeLangId Int 관광지 언어 ID
 * @property themeCategory String 테마 유형
 * @property addr1 String 주소
 * @property addr2 String 주소 상세
 * @property title String 관광지 이름
 * @property mapX Double 경도(X)
 * @property mapY Double 위도(Y)
 * @property langCheck String 제공 언어
 * @property langCode String 언어
 * @property imageUrl String 관광지 이미지 URL
 * @property createdTime String 등록일
 * @property modifiedTime String 수정일
 * @constructor
 */
data class ThemeResponse(
    @SerializedName("tid") val themeId: Int,
    @SerializedName("tlid") val themeLangId: Int,
    @SerializedName("themeCategory") val themeCategory: String,
    @SerializedName("addr1") val addr1: String,
    @SerializedName("addr2") val addr2: String,
    @SerializedName("title") val title: String,
    @SerializedName("mapX") val mapX: Double,
    @SerializedName("mapY") val mapY: Double,
    @SerializedName("langCheck") val langCheck: String,
    @SerializedName("langCode") val langCode: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("createdtime") val createdTime: String,
    @SerializedName("modifiedtime") val modifiedTime: String,
)
