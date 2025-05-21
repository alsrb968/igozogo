package io.jacob.igozogo.core.data.model.remote

import com.google.gson.annotations.SerializedName

data class ResponseWrapper<T>(
    @SerializedName("response") val response: Response<T>,
)
