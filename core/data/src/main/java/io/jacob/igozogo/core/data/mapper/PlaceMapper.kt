package io.jacob.igozogo.core.data.mapper

import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity
import io.jacob.igozogo.core.data.model.remote.odii.ThemeResponse
import io.jacob.igozogo.core.data.util.toDateTimeString
import io.jacob.igozogo.core.data.util.toInstantAsSystemDefault
import io.jacob.igozogo.core.model.Place

fun ThemeResponse.toThemeEntity(): ThemeEntity {
    return ThemeEntity(
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
        createdTime = createdTime.toInstantAsSystemDefault(),
        modifiedTime = modifiedTime.toInstantAsSystemDefault(),
    )
}

fun List<ThemeResponse>.toThemeEntities(): List<ThemeEntity> {
    return map { it.toThemeEntity() }
}

fun ThemeEntity.toPlace(): Place {
    return Place(
        placeId = themeId,
        placeLangId = themeLangId,
        placeCategory = themeCategory,
        address1 = addr1,
        address2 = addr2,
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

fun List<ThemeEntity>.toPlaces(): List<Place> {
    return map { it.toPlace() }
}

fun Place.asThemeEntity(): ThemeEntity {
    return ThemeEntity(
        themeId = placeId,
        themeLangId = placeLangId,
        themeCategory = placeCategory,
        addr1 = address1,
        addr2 = address2,
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

fun List<Place>.asThemeEntities(): List<ThemeEntity> {
    return map { it.asThemeEntity() }
}

fun Place.toThemeResponse(): ThemeResponse {
    return ThemeResponse(
        themeId = placeId,
        themeLangId = placeLangId,
        themeCategory = placeCategory,
        addr1 = address1,
        addr2 = address2,
        title = title,
        mapX = mapX,
        mapY = mapY,
        langCheck = langCheck,
        langCode = langCode,
        imageUrl = imageUrl,
        createdTime = createdTime.toDateTimeString(),
        modifiedTime = modifiedTime.toDateTimeString(),
    )
}

fun List<Place>.toThemeResponses(): List<ThemeResponse> {
    return map { it.toThemeResponse() }
}