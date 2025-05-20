package io.jacob.igozogo.core.data.model.remote

import com.google.gson.annotations.SerializedName

data class Items<T>(
    @SerializedName("item") val item: List<T>,
)
