package io.jacob.igozogo.core.data.mapper

import io.jacob.igozogo.core.data.model.remote.ResponseWrapper

fun <T> ResponseWrapper<T>.toResponses(): List<T> {
    return this.response.body.items.item
}