package io.jacob.igozogo.core.data.datasource.local

import androidx.paging.PagingSource
import io.jacob.igozogo.core.data.db.ThemeDao
import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity
import javax.inject.Inject

interface ThemeDataSource {
    suspend fun insertThemes(themes: List<ThemeEntity>)
    fun getThemesPagingSource(): PagingSource<Int, ThemeEntity>
    suspend fun getThemes(size: Int): List<ThemeEntity>
    fun getThemeCategoriesPagingSource(): PagingSource<Int, String>
    suspend fun getThemeCategories(): List<String>
    fun getThemesByCategoryPagingSource(category: String): PagingSource<Int, ThemeEntity>
    suspend fun getThemesByCategory(category: String, size: Int): List<ThemeEntity>
    fun getThemesByLocationPagingSource(
        mapX: Double, mapY: Double, radius: Int
    ): PagingSource<Int, ThemeEntity>

    suspend fun getThemesByLocation(
        mapX: Double, mapY: Double, radius: Int,
        size: Int
    ): List<ThemeEntity>

    fun getThemesByKeywordPagingSource(keyword: String): PagingSource<Int, ThemeEntity>
    suspend fun getThemesByKeyword(keyword: String, size: Int): List<ThemeEntity>
    suspend fun getThemeById(themeId: Int, themeLangId: Int): ThemeEntity?
    suspend fun getThemesCount(): Int
    suspend fun deleteThemes()
}

class ThemeDataSourceImpl @Inject constructor(
    private val dao: ThemeDao
) : ThemeDataSource {
    override suspend fun insertThemes(themes: List<ThemeEntity>) {
        return dao.insertThemes(themes)
    }

    override fun getThemesPagingSource(): PagingSource<Int, ThemeEntity> {
        return dao.getThemesPagingSource()
    }

    override suspend fun getThemes(size: Int): List<ThemeEntity> {
        return dao.getThemes(size)
    }

    override fun getThemeCategoriesPagingSource(): PagingSource<Int, String> {
        return dao.getThemeCategoriesPagingSource()
    }

    override suspend fun getThemeCategories(): List<String> {
        return dao.getThemeCategories()
    }

    override fun getThemesByCategoryPagingSource(category: String): PagingSource<Int, ThemeEntity> {
        return dao.getThemesByCategoryPagingSource(category)
    }

    override suspend fun getThemesByCategory(category: String, size: Int): List<ThemeEntity> {
        return dao.getThemesByCategory(category, size)
    }

    override fun getThemesByLocationPagingSource(
        mapX: Double, mapY: Double, radius: Int
    ): PagingSource<Int, ThemeEntity> {
        return dao.getThemesByLocationPagingSource(mapX, mapY, radius / METERS_PER_DEGREE)
    }

    override suspend fun getThemesByLocation(
        mapX: Double, mapY: Double, radius: Int,
        size: Int
    ): List<ThemeEntity> {
        return dao.getThemesByLocation(mapX, mapY, radius / METERS_PER_DEGREE, size)
    }

    override fun getThemesByKeywordPagingSource(keyword: String): PagingSource<Int, ThemeEntity> {
        return dao.getThemesByKeywordPagingSource(keyword)
    }

    override suspend fun getThemesByKeyword(keyword: String, size: Int): List<ThemeEntity> {
        return dao.getThemesByKeyword(keyword, size)
    }

    override suspend fun getThemeById(themeId: Int, themeLangId: Int): ThemeEntity? {
        return dao.getThemeById(themeId, themeLangId)
    }

    override suspend fun getThemesCount(): Int {
        return dao.getThemesCount()
    }

    override suspend fun deleteThemes() {
        return dao.deleteThemes()
    }

    companion object {
        private const val METERS_PER_DEGREE = 111000.0
    }
}