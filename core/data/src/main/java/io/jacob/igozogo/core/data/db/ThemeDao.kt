package io.jacob.igozogo.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity

@Dao
interface ThemeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThemes(themes: List<ThemeEntity>)

    @Query(
        """
        SELECT *
        FROM themeTable
        """
    )
    suspend fun getThemes(): List<ThemeEntity>

    @Query(
        """
        SELECT DISTINCT themeCategory
        FROM themeTable
        """
    )
    suspend fun getThemeCategories(): List<String>

    @Query(
        """
        SELECT *
        FROM themeTable
        WHERE themeCategory = :category
        """
    )
    suspend fun getThemesByCategory(category: String): List<ThemeEntity>

    @Query(
        """
        SELECT COUNT(*)
        FROM themeTable
        """
    )
    suspend fun getCount(): Int

    @Query(
        """
        DELETE
        FROM themeTable
        """
    )
    suspend fun deleteThemes()
}