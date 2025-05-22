package io.jacob.igozogo.core.domain.repository

import androidx.paging.PagingData
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.model.Theme
import kotlinx.coroutines.flow.Flow

interface OdiiRepository {
    suspend fun syncThemes()
    fun getThemes(): Flow<List<Theme>>
    fun getThemeCategories(): Flow<List<String>>
    fun getThemesByCategory(category: String): Flow<List<Theme>>
    fun getThemesByLocation(mapX: Double, mapY: Double, radius: Int): Flow<List<Theme>>
    fun getThemesByKeyword(keyword: String): Flow<List<Theme>>

    fun getStoriesByTheme(
        themeId: Int,
        themeLangId: Int,
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