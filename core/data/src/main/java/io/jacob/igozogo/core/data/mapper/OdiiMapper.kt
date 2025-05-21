package io.jacob.igozogo.core.data.mapper

import io.jacob.igozogo.core.data.model.remote.odii.ThemeResponse
import io.jacob.igozogo.core.domain.model.Theme

fun ThemeResponse.toTheme(): Theme {
    return Theme(
        themeId = themeId,
        themeLangId = themeLangId,
        themeCategory = themeCategory,
        addr1 = addr1,
        addr2 = addr2,
        title = title,
        mapX = mapX,
        mapY = mapY,
        langCheck = langCheck,
        langCode = langCode,
        imageUrl = imageUrl,
        createdTime = createdTime,
        modifiedTime = modifiedTime,
    )
}