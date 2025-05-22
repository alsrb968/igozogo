package io.jacob.igozogo.core.data.model.local.odii

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "theme_table")
data class ThemeEntity(
    val themeId: Int,
    @PrimaryKey val themeLangId: Int,
    val themeCategory: String,
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
