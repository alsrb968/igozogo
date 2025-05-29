package io.jacob.igozogo.core.domain.repository

import androidx.paging.PagingData
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import kotlinx.coroutines.flow.Flow

interface OdiiRepository {
    suspend fun syncPlaces(size: Int = 100)

    fun getPlaces(pageSize: Int = 20): Flow<PagingData<Place>>

    fun getPlaceCategories(pageSize: Int = 20): Flow<PagingData<String>>

    fun getPlacesByCategory(
        category: String,
        pageSize: Int = 20,
    ): Flow<PagingData<Place>>

    fun getPlacesByLocation(
        mapX: Double, mapY: Double, radius: Int,
        pageSize: Int = 20,
    ): Flow<PagingData<Place>>

    fun getPlacesByKeyword(
        keyword: String,
        pageSize: Int = 20,
    ): Flow<PagingData<Place>>

    suspend fun getPlacesCount(): Int

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