package io.jacob.igozogo.core.data.mapper

import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import io.jacob.igozogo.core.data.model.local.odii.StoryRemoteKey
import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity
import io.jacob.igozogo.core.data.model.remote.odii.StoryResponse
import io.jacob.igozogo.core.data.model.remote.odii.ThemeResponse
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.model.Theme

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

fun ThemeEntity.toTheme(): Theme {
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

fun List<ThemeEntity>.toTheme(): List<Theme> {
    return map { it.toTheme() }
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

fun List<StoryEntity>.toStory(): List<Story> {
    return map { it.toStory() }
}

fun StoryEntity.toStoryRemoteKey(query: String, nextPage: Int?): StoryRemoteKey {
    return StoryRemoteKey(
        id = this.storyLangId,
        queryType = query,
        nextPage = nextPage,
    )
}

fun List<StoryEntity>.toStoryRemoteKey(query: String, nextPage: Int?): List<StoryRemoteKey> {
    return map { it.toStoryRemoteKey(query, nextPage) }
}