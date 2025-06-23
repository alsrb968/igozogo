package io.jacob.igozogo.core.domain.model

import io.jacob.igozogo.core.domain.util.toHumanReadableDate
import io.jacob.igozogo.core.domain.util.toHumanReadableTime

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
) {
    val humanReadablePlayTime: String
        get() = playTime.toHumanReadableTime()

    val humanReadableCreatedTime: String
        get() = createdTime.toHumanReadableDate()

    val humanReadableModifiedTime: String
        get() = modifiedTime.toHumanReadableDate()
}
