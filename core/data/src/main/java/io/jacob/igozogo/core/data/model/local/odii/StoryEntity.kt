package io.jacob.igozogo.core.data.model.local.odii

import androidx.room.Entity

@Entity(
    tableName = "storyTable",
    primaryKeys = ["storyId", "storyLangId"]
)
data class StoryEntity(
    val themeId: Int,
    val themeLangId: Int,
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
