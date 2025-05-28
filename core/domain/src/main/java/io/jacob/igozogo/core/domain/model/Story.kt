package io.jacob.igozogo.core.domain.model

data class Story(
    val placeId: Int,
    val placeLangId: Int,
    val storyId: Int,
    val storyLangId: Int,
    val title: String,
    val mapX: Double,
    val mapY: Double,
    val audioTitle: String,
    val script: String,
    val playTime: Int,
    val audioUrl: String,
    val langCode: String,
    val imageUrl: String,
    val createdTime: String,
    val modifiedTime: String,
)
