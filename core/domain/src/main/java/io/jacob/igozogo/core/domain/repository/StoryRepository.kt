package io.jacob.igozogo.core.domain.repository

import androidx.paging.PagingData
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    suspend fun getStories(size: Int = 20): List<Story>

    fun getStoriesByPlacePaging(
        place: Place,
        pageSize: Int = 20,
    ): Flow<PagingData<Story>>

    suspend fun getStoriesByPlace(place: Place): List<Story>

    fun getStoriesByLocationPaging(
        mapX: Double, mapY: Double, radius: Int,
        pageSize: Int = 20,
    ): Flow<PagingData<Story>>

    suspend fun getStoriesByLocation(
        mapX: Double, mapY: Double, radius: Int,
        size: Int = 20,
    ): List<Story>

    fun getStoriesByKeywordPaging(
        keyword: String,
        pageSize: Int = 20,
    ): Flow<PagingData<Story>>

    suspend fun getStoriesByKeyword(
        keyword: String,
        size: Int = 20,
    ): List<Story>

    suspend fun getStoryById(storyId: Int, storyLangId: Int): Story?
}