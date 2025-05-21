package io.jacob.igozogo.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ThemeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThemes(themes: List<ThemeEntity>)

    @Query(
        """
        SELECT *
        FROM theme_table
        """
    )
    fun getThemes(): Flow<List<ThemeEntity>>

    @Query(
        """
        SELECT DISTINCT themeCategory
        FROM theme_table
        """
    )
    fun getThemeCategories(): Flow<List<String>>

    @Query(
        """
        SELECT *
        FROM theme_table
        WHERE themeCategory = :category
        """
    )
    fun getThemesByCategory(category: String): Flow<List<ThemeEntity>>

    @Query(
        """
        SELECT *
        FROM theme_table
        WHERE title LIKE '%' || :keyword || '%'
            OR themeCategory LIKE '%' || :keyword || '%'
        """
    )
    fun getThemesByKeyword(keyword: String): Flow<List<ThemeEntity>>

    @Query(
        """
        DELETE
        FROM theme_table
        """
    )
    suspend fun deleteThemes()
}