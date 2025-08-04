package io.jacob.igozogo.core.data.mapper

import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import io.jacob.igozogo.core.data.model.remote.odii.StoryResponse
import io.jacob.igozogo.core.data.util.toDateTimeString
import io.jacob.igozogo.core.data.util.toInstantAsSystemDefault
import io.jacob.igozogo.core.model.Story

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
        playTime = playTime * 1000L,
        audioUrl = audioUrl,
        langCode = langCode,
        imageUrl = imageUrl,
        createdTime = createdTime.toInstantAsSystemDefault(),
        modifiedTime = modifiedTime.toInstantAsSystemDefault(),
    )
}

fun List<StoryResponse>.toStoryEntities(): List<StoryEntity> {
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

fun List<StoryEntity>.toStories(): List<Story> {
    return map { it.toStory() }
}

fun Story.asStoryEntity(): StoryEntity {
    return StoryEntity(
        themeId = placeId,
        themeLangId = placeLangId,
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

fun List<Story>.asStoryEntities(): List<StoryEntity> {
    return map { it.asStoryEntity() }
}

fun Story.toStoryResponse(): StoryResponse {
    return StoryResponse(
        themeId = placeId,
        themeLangId = placeLangId,
        storyId = storyId,
        storyLangId = storyLangId,
        title = title,
        mapX = mapX,
        mapY = mapY,
        audioTitle = audioTitle,
        script = script,
        playTime = (playTime / 1000).toInt(),
        audioUrl = audioUrl,
        langCode = langCode,
        imageUrl = imageUrl,
        createdTime = createdTime.toDateTimeString(),
        modifiedTime = modifiedTime.toDateTimeString(),
    )
}

fun List<Story>.toStoryResponses(): List<StoryResponse> {
    return map { it.toStoryResponse() }
}