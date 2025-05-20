package io.jacob.igozogo.core.data.model.odii

import com.google.gson.annotations.SerializedName

data class Items<T>(
    @SerializedName("item") val item: List<T>,
)
