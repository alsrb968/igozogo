package io.jacob.igozogo.core.data.model.remote

import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("resultCode") val resultCode: String,
    @SerializedName("resultMsg") val resultMsg: String
)
