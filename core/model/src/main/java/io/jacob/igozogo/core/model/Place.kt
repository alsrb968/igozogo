package io.jacob.igozogo.core.model

import kotlinx.datetime.Instant

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
    val createdTime: Instant,
    val modifiedTime: Instant,
) {
    val fullAddress: String
        get() = "$address1 $address2"
}
