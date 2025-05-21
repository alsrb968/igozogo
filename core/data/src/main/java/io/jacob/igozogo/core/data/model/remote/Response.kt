package io.jacob.igozogo.core.data.model.remote

import com.google.gson.annotations.SerializedName

data class Response<T>(
    @SerializedName("header") val header: Header,
    @SerializedName("body") val body: Body<T>,
)
