package io.jacob.igozogo.core.domain.repository

import androidx.paging.PagingData
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    fun getStoriesByPlace(
        place: Place,
        pageSize: Int = 20,
    ): Flow<PagingData<Story>>

    fun getStoriesByLocation(
        mapX: Double,
        mapY: Double,
        radius: Int,
        pageSize: Int = 20,
    ): Flow<PagingData<Story>>

    fun getStoriesByKeyword(
        keyword: String,
        pageSize: Int = 20,
    ): Flow<PagingData<Story>>
}