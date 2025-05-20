package io.jacob.igozogo.core.data.model.odii

import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("resultCode") val resultCode: String,
    @SerializedName("resultMsg") val resultMsg: String
)
