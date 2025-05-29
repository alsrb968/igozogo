package io.jacob.igozogo.core.domain.model

data class Place(
    val placeId: Int,
    val placeLangId: Int,
    val placeCategory: String,
    val addr1: String,
    val addr2: String,
    val title: String,
    val mapX: Double,
    val mapY: Double,
    val langCheck: String,
    val langCode: String,
    val imageUrl: String,
    val createdTime: String,
    val modifiedTime: String,
)
