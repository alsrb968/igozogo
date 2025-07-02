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
        WHERE imageUrl IS NOT NULL AND imageUrl != ''
        """
    )
    fun getThemesPagingSource(): PagingSource<Int, ThemeEntity>

    @Query(
        """
        
        SELECT *
        FROM theme_table
        WHERE imageUrl IS NOT NULL AND imageUrl != ''
        ORDER BY RANDOM()
        LIMIT :size
        """
    )
    suspend fun getThemes(size: Int): List<ThemeEntity>

    @Query(
        """
        SELECT DISTINCT themeCategory
        FROM theme_table
        """
    )
    fun getThemeCategoriesPagingSource(): PagingSource<Int, String>

    @Query(
        """
        SELECT DISTINCT themeCategory
        FROM theme_table
        """
    )
    suspend fun getThemeCategories(): List<String>

    @Query(
        """
        SELECT *
        FROM theme_table
        WHERE themeCategory = :category
        """
    )
    fun getThemesByCategoryPagingSource(category: String): PagingSource<Int, ThemeEntity>

    @Query(
        """
        SELECT *
        FROM theme_table
        WHERE themeCategory = :category
        LIMIT :size
        """
    )
    suspend fun getThemesByCategory(category: String, size: Int): List<ThemeEntity>

    @Query(
        """
        SELECT *
        FROM theme_table
        WHERE mapX BETWEEN :mapX - :radiusDeg AND :mapX + :radiusDeg
            AND mapY BETWEEN :mapY - :radiusDeg AND :mapY + :radiusDeg
        ORDER BY ((mapX - :mapX) * (mapX - :mapX) + (mapY - :mapY) * (mapY - :mapY)) ASC
        """
    )
    fun getThemesByLocationPagingSource(
        mapX: Double,
        mapY: Double,
        radiusDeg: Double
    ): PagingSource<Int, ThemeEntity>

    @Query(
        """
        SELECT *
        FROM theme_table
        WHERE mapX BETWEEN :mapX - :radiusDeg AND :mapX + :radiusDeg
            AND mapY BETWEEN :mapY - :radiusDeg AND :mapY + :radiusDeg
        ORDER BY ((mapX - :mapX) * (mapX - :mapX) + (mapY - :mapY) * (mapY - :mapY)) ASC
        LIMIT :size
        """
    )
    suspend fun getThemesByLocation(
        mapX: Double,
        mapY: Double,
        radiusDeg: Double,
        size: Int
    ): List<ThemeEntity>

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
    fun getThemesByKeywordPagingSource(keyword: String): PagingSource<Int, ThemeEntity>

    @Query(
        """
        SELECT *
        FROM theme_table
        WHERE title LIKE '%' || :keyword || '%'
            OR themeCategory LIKE '%' || :keyword || '%'
            OR addr1 LIKE '%' || :keyword || '%'
            OR addr2 LIKE '%' || :keyword || '%'
        LIMIT :size
        """
    )
    suspend fun getThemesByKeyword(keyword: String, size: Int): List<ThemeEntity>

    @Query(
        """
        SELECT *
        FROM theme_table
        WHERE themeId = :themeId AND themeLangId = :themeLangId
        """
    )
    suspend fun getThemeById(themeId: Int, themeLangId: Int): ThemeEntity?

    @Query(
        """
        SELECT COUNT(*)
        FROM theme_table
        """
    )
    suspend fun getThemesCount(): Int

    @Query(
        """
        DELETE
        FROM theme_table
        """
    )
    suspend fun deleteThemes()
}