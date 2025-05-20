package io.jacob.igozogo.core.data.model.odii

import com.google.gson.annotations.SerializedName

/**
 * 이야기 응답
 * @property themeId Int 관광지 ID
 * @property themeLangId Int 관광지 언어 ID
 * @property storyId Int 이야기 ID
 * @property storyLangId Int 이야기 언어 ID
 * @property title String 관광지 이름
 * @property mapX Double 경도(X)
 * @property mapY Double 위도(Y)
 * @property audioTitle String 오디오 제목
 * @property script String 대본
 * @property playTime Int 재생 시간
 * @property audioUrl String 오디오 파일 URL
 * @property langCode String 언어
 * @property imageUrl String 관광지 이미지 URL
 * @property createdTime String 등록일
 * @property modifiedTime String 수정일
 * @constructor
 */
data class StoryResponse(
    @SerializedName("tid") val themeId: Int,
    @SerializedName("tlid") val themeLangId: Int,
    @SerializedName("stid") val storyId: Int,
    @SerializedName("stlid") val storyLangId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("mapX") val mapX: Double,
    @SerializedName("mapY") val mapY: Double,
    @SerializedName("audioTitle") val audioTitle: String,
    @SerializedName("script") val script: String,
    @SerializedName("playTime") val playTime: Int,
    @SerializedName("audioUrl") val audioUrl: String,
    @SerializedName("langCode") val langCode: String,
    @SerializedName("ImageUrl") val imageUrl: String,
    @SerializedName("createdtime") val createdTime: String,
    @SerializedName("modifiedtime") val modifiedTime: String,
)
