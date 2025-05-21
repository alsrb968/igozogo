package io.jacob.igozogo.core.data.datasource.local

import io.jacob.igozogo.core.data.db.ThemeDao
import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity
import javax.inject.Inject

interface ThemeDataSource {
    suspend fun insertThemes(themes: List<ThemeEntity>)
    suspend fun getThemes(): List<ThemeEntity>
    suspend fun getThemeCategories(): List<String>
    suspend fun getThemesByCategory(category: String): List<ThemeEntity>
    suspend fun getCount(): Int
    suspend fun deleteThemes()
}

class ThemeDataSourceImpl @Inject constructor(
    private val dao: ThemeDao
) : ThemeDataSource {
    override suspend fun insertThemes(themes: List<ThemeEntity>) {
        return dao.insertThemes(themes)
    }

    override suspend fun getThemes(): List<ThemeEntity> {
        return dao.getThemes()
    }

    override suspend fun getThemeCategories(): List<String> {
        return dao.getThemeCategories()
    }

    override suspend fun getThemesByCategory(category: String): List<ThemeEntity> {
        return dao.getThemesByCategory(category)
    }

    override suspend fun getCount(): Int {
        return dao.getCount()
    }

    override suspend fun deleteThemes() {
        return dao.deleteThemes()
    }
}