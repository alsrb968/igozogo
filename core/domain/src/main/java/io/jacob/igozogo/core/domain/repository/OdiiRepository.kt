package io.jacob.igozogo.core.domain.repository

import androidx.paging.PagingData
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.model.Theme
import kotlinx.coroutines.flow.Flow

interface OdiiRepository {
    suspend fun syncThemes()

    fun getThemes(pageSize: Int = 20): Flow<PagingData<Theme>>

    fun getThemeCategories(pageSize: Int = 20): Flow<PagingData<String>>

    fun getThemesByCategory(
        category: String,
        pageSize: Int = 20,
    ): Flow<PagingData<Theme>>

    fun getThemesByLocation(
        mapX: Double, mapY: Double, radius: Int,
        pageSize: Int = 20,
    ): Flow<PagingData<Theme>>

    fun getThemesByKeyword(
        keyword: String,
        pageSize: Int = 20,
    ): Flow<PagingData<Theme>>

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