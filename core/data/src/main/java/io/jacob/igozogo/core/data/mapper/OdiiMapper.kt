package io.jacob.igozogo.core.data.mapper

import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import io.jacob.igozogo.core.data.model.local.odii.StoryRemoteKey
import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity
import io.jacob.igozogo.core.data.model.remote.ResponseWrapper
import io.jacob.igozogo.core.data.model.remote.odii.StoryResponse
import io.jacob.igozogo.core.data.model.remote.odii.ThemeResponse
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story

fun <T> ResponseWrapper<T>.toResponses(): List<T> {
    return this.response.body.items.item
}

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
        createdTime = createdTime,
        modifiedTime = modifiedTime,
    )
}

fun List<ThemeResponse>.toThemeEntity(): List<ThemeEntity> {
    return map { it.toThemeEntity() }
}

fun ThemeEntity.toPlace(): Place {
    return Place(
        placeId = themeId,
        placeLangId = themeLangId,
        placeCategory = themeCategory,
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

fun List<ThemeEntity>.toPlace(): List<Place> {
    return map { it.toPlace() }
}

fun StoryResponse.toStoryEntity(): StoryEntity {
    return StoryEntity(
        themeId = themeId,
        themeLangId = themeLangId,
        storyId = storyId,
        storyLangId = storyLangId,
        title = title,
        mapX = mapX,
        mapY = mapY,
        audioTitle = audioTitle,
        script = script,
        playTime = playTime,
        audioUrl = audioUrl,
        langCode = langCode,
        imageUrl = imageUrl,
        createdTime = createdTime,
        modifiedTime = modifiedTime,
    )
}

fun List<StoryResponse>.toStoryEntity(): List<StoryEntity> {
    return map { it.toStoryEntity() }
}

fun StoryEntity.toStory(): Story {
    return Story(
        placeId = themeId,
        placeLangId = themeLangId,
        storyId = storyId,
        storyLangId = storyLangId,
        title = title,
        mapX = mapX,
        mapY = mapY,
        audioTitle = audioTitle,
        script = script,
        playTime = playTime,
        audioUrl = audioUrl,
        langCode = langCode,
        imageUrl = imageUrl,
        createdTime = createdTime,
        modifiedTime = modifiedTime,
    )
}

fun List<StoryEntity>.toStory(): List<Story> {
    return map { it.toStory() }
}

fun StoryEntity.toStoryRemoteKey(
    query: String,
    prevPage: Int?,
    nextPage: Int?
): StoryRemoteKey {
    return StoryRemoteKey(
        id = this.storyLangId,
        queryType = query,
        prevPage = prevPage,
        nextPage = nextPage,
    )
}

fun List<StoryEntity>.toStoryRemoteKey(
    query: String,
    prevPage: Int?,
    nextPage: Int?
): List<StoryRemoteKey> {
    return map { it.toStoryRemoteKey(query, prevPage, nextPage) }
}