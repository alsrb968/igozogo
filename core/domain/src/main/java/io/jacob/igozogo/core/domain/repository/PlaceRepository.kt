package io.jacob.igozogo.core.domain.repository

import androidx.paging.PagingData
import io.jacob.igozogo.core.model.Place
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
    suspend fun syncPlaces(size: Int = 100)
    fun getPlacesPaging(pageSize: Int = 20): Flow<PagingData<Place>>
    suspend fun getPlaces(size: Int = 20): List<Place>
    fun getPlaceCategoriesPaging(pageSize: Int = 20): Flow<PagingData<String>>
    suspend fun getPlaceCategories(): List<String>
    fun getPlacesByCategoryPaging(
        category: String,
        pageSize: Int = 20,
    ): Flow<PagingData<Place>>

    suspend fun getPlacesByCategory(category: String, size: Int = 20): List<Place>

    fun getPlacesByLocationPaging(
        mapX: Double, mapY: Double, radius: Int,
        pageSize: Int = 20,
    ): Flow<PagingData<Place>>

    suspend fun getPlacesByLocation(
        mapX: Double, mapY: Double, radius: Int,
        size: Int = 20,
    ): List<Place>

    fun getPlacesByKeywordPaging(
        keyword: String,
        pageSize: Int = 20,
    ): Flow<PagingData<Place>>

    suspend fun getPlacesByKeyword(
        keyword: String,
        size: Int = 20,
    ): List<Place>

    suspend fun getPlaceById(placeId: Int, placeLangId: Int): Place?
    suspend fun getPlacesCount(): Int
}