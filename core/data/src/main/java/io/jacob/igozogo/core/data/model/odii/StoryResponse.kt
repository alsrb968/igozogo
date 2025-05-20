package io.jacob.igozogo.core.data.model.odii

import com.google.gson.annotations.SerializedName

data class StoryResponse(
    @SerializedName("tid") val tid: Int,
    @SerializedName("tlid") val tlid: Int,
    @SerializedName("stid") val stid: Int,
    @SerializedName("stlid") val stlid: Int,
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
