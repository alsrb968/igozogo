package io.jacob.igozogo.core.data.datasource.local

import io.jacob.igozogo.core.data.db.ThemeDao
import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ThemeDataSource {
    suspend fun insertThemes(themes: List<ThemeEntity>)
    fun getThemes(): Flow<List<ThemeEntity>>
    fun getThemeCategories(): Flow<List<String>>
    fun getThemesByCategory(category: String): Flow<List<ThemeEntity>>
    fun getThemesByLocation(mapX: Double, mapY: Double, radius: Int): Flow<List<ThemeEntity>>
    fun getThemesByKeyword(keyword: String): Flow<List<ThemeEntity>>
    suspend fun deleteThemes()
}

class ThemeDataSourceImpl @Inject constructor(
    private val dao: ThemeDao
) : ThemeDataSource {
    override suspend fun insertThemes(themes: List<ThemeEntity>) {
        return dao.insertThemes(themes)
    }

    override fun getThemes(): Flow<List<ThemeEntity>> {
        return dao.getThemes()
    }

    override fun getThemeCategories(): Flow<List<String>> {
        return dao.getThemeCategories()
    }

    override fun getThemesByCategory(category: String): Flow<List<ThemeEntity>> {
        return dao.getThemesByCategory(category)
    }

    override fun getThemesByLocation(
        mapX: Double,
        mapY: Double,
        radius: Int
    ): Flow<List<ThemeEntity>> {
        return dao.getThemesByLocation(mapX, mapY, radius / 111000.0)
    }

    override fun getThemesByKeyword(keyword: String): Flow<List<ThemeEntity>> {
        return dao.getThemesByKeyword(keyword)
    }

    override suspend fun deleteThemes() {
        return dao.deleteThemes()
    }
}