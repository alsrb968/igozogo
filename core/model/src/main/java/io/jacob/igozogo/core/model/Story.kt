package io.jacob.igozogo.core.model

import kotlinx.datetime.Instant

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
    val playTime: Long,
    val audioUrl: String,
    val langCode: String,
    val imageUrl: String,
    val createdTime: Instant,
    val modifiedTime: Instant,
)
