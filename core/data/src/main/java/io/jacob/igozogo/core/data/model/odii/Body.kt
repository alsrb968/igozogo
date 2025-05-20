package io.jacob.igozogo.core.data.model.odii

import com.google.gson.annotations.SerializedName

data class Body<T>(
    @SerializedName("items") val items: Items<T>,
    @SerializedName("numOfRows") val numOfRows: Int,
    @SerializedName("pageNo") val pageNo: Int,
    @SerializedName("totalCount") val totalCount: Int,
)
