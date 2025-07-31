package io.jacob.igozogo.core.data.model.local.odii

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(tableName = "story_table")
data class StoryEntity(
    val themeId: Int,
    val themeLangId: Int,
    val storyId: Int,
    @PrimaryKey val storyLangId: Int,
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
