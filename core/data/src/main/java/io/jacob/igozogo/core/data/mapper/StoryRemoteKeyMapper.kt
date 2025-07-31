package io.jacob.igozogo.core.data.mapper

import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import io.jacob.igozogo.core.data.model.local.odii.StoryRemoteKey

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