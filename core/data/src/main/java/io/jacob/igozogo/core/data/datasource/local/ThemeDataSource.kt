package io.jacob.igozogo.core.data.datasource.local

import androidx.paging.PagingSource
import io.jacob.igozogo.core.data.db.ThemeDao
import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity
import javax.inject.Inject

interface ThemeDataSource {
    suspend fun insertThemes(themes: List<ThemeEntity>)
    fun getThemes(): PagingSource<Int, ThemeEntity>
    fun getThemeCategories(): PagingSource<Int, String>
    fun getThemesByCategory(category: String): PagingSource<Int, ThemeEntity>
    fun getThemesByLocation(mapX: Double, mapY: Double, radius: Int): PagingSource<Int, ThemeEntity>
    fun getThemesByKeyword(keyword: String): PagingSource<Int, ThemeEntity>
    suspend fun deleteThemes()
}

class ThemeDataSourceImpl @Inject constructor(
    private val dao: ThemeDao
) : ThemeDataSource {
    override suspend fun insertThemes(themes: List<ThemeEntity>) {
        return dao.insertThemes(themes)
    }

    override fun getThemes(): PagingSource<Int, ThemeEntity> {
        return dao.getThemes()
    }

    override fun getThemeCategories(): PagingSource<Int, String> {
        return dao.getThemeCategories()
    }

    override fun getThemesByCategory(category: String): PagingSource<Int, ThemeEntity> {
        return dao.getThemesByCategory(category)
    }

    override fun getThemesByLocation(
        mapX: Double,
        mapY: Double,
        radius: Int
    ): PagingSource<Int, ThemeEntity> {
        return dao.getThemesByLocation(mapX, mapY, radius / 111000.0)
    }

    override fun getThemesByKeyword(keyword: String): PagingSource<Int, ThemeEntity> {
        return dao.getThemesByKeyword(keyword)
    }

    override suspend fun deleteThemes() {
        return dao.deleteThemes()
    }
}