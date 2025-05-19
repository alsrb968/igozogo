package io.jacob.igozogo.core.data.model.odii

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("response") val response: ResponseHeaderBody
)
