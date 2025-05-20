package io.jacob.igozogo.core.data.model.odii

import com.google.gson.annotations.SerializedName

data class ThemeResponse(
    @SerializedName("tid") val tid: Int,
    @SerializedName("tlid") val title: Int,
    @SerializedName("themeCategory") val themeCategory: String,
    @SerializedName("addr1") val addr1: String,
    @SerializedName("addr2") val addr2: String,
    @SerializedName("title") val name: String,
    @SerializedName("mapX") val mapX: Double,
    @SerializedName("mapY") val mapY: Double,
    @SerializedName("langCheck") val langCheck: String,
    @SerializedName("langCode") val langCode: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("createdtime") val createdTime: String,
    @SerializedName("modifiedtime") val modifiedTime: String,
)
