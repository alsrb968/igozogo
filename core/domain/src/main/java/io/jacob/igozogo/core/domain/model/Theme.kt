package io.jacob.igozogo.core.domain.model

data class Theme(
    val themeId: Int,
    val themeLangId: Int,
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
