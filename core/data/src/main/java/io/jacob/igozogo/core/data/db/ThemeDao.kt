package io.jacob.igozogo.core.data.db

import androidx.paging.PagingSource
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
        FROM theme_table
        """
    )
    fun getThemes(): PagingSource<Int, ThemeEntity>

    @Query(
        """
        SELECT DISTINCT themeCategory
        FROM theme_table
        """
    )
    fun getThemeCategories(): PagingSource<Int, String>

    @Query(
        """
        SELECT *
        FROM theme_table
        WHERE themeCategory = :category
        """
    )
    fun getThemesByCategory(category: String): PagingSource<Int, ThemeEntity>

    @Query(
        """
        SELECT *
        FROM theme_table
        WHERE mapX BETWEEN :mapX - :radiusDeg AND :mapX + :radiusDeg
            AND mapY BETWEEN :mapY - :radiusDeg AND :mapY + :radiusDeg
        ORDER BY ((mapX - :mapX) * (mapX - :mapX) + (mapY - :mapY) * (mapY - :mapY)) ASC
        """
    )
    fun getThemesByLocation(mapX: Double, mapY: Double, radiusDeg: Double): PagingSource<Int, ThemeEntity>

    @Query(
        """
        SELECT *
        FROM theme_table
        WHERE title LIKE '%' || :keyword || '%'
            OR themeCategory LIKE '%' || :keyword || '%'
            OR addr1 LIKE '%' || :keyword || '%'
            OR addr2 LIKE '%' || :keyword || '%'
        """
    )
    fun getThemesByKeyword(keyword: String): PagingSource<Int, ThemeEntity>

    @Query(
        """
        DELETE
        FROM theme_table
        """
    )
    suspend fun deleteThemes()
}