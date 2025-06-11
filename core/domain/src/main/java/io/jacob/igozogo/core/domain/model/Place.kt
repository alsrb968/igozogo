package io.jacob.igozogo.core.domain.model

import io.jacob.igozogo.core.domain.util.toHumanReadableDate

data class Place(
    val placeId: Int,
    val placeLangId: Int,
    val placeCategory: String,
    val address1: String,
    val address2: String,
    val title: String,
    val mapX: Double,
    val mapY: Double,
    val langCheck: String,
    val langCode: String,
    val imageUrl: String,
    val createdTime: String,
    val modifiedTime: String,
) {
    val humanReadableCreatedTime: String
        get() = createdTime.toHumanReadableDate()

    val humanReadableModifiedTime: String
        get() = modifiedTime.toHumanReadableDate()
}
