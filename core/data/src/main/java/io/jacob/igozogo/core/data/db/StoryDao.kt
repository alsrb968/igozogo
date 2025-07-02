package io.jacob.igozogo.core.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.jacob.igozogo.core.data.model.local.odii.StoryEntity

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryEntity>)

    @Query(
        """
        SELECT *
        FROM story_table
        """
    )
    fun getStoriesPagingSource(): PagingSource<Int, StoryEntity>

    @Query(
        """
        SELECT *
        FROM story_table
        WHERE imageUrl IS NOT NULL AND imageUrl != ''
        ORDER BY RANDOM()
        LIMIT :size
        """
    )
    suspend fun getStories(size: Int): List<StoryEntity>

    @Query(
        """
        SELECT *
        FROM story_table
        WHERE themeId = :themeId AND themeLangId = :themeLangId
        """
    )
    fun getStoriesByThemePagingSource(
        themeId: Int, themeLangId: Int
    ): PagingSource<Int, StoryEntity>

    @Query(
        """
        SELECT *
        FROM story_table
        WHERE themeId = :themeId AND themeLangId = :themeLangId
        """
    )
    suspend fun getStoriesByTheme(themeId: Int, themeLangId: Int): List<StoryEntity>

    @Query(
        """
        SELECT *
        FROM story_table
        WHERE mapX BETWEEN :mapX - :radiusDeg AND :mapX + :radiusDeg
            AND mapY BETWEEN :mapY - :radiusDeg AND :mapY + :radiusDeg
        ORDER BY ((mapX - :mapX) * (mapX - :mapX) + (mapY - :mapY) * (mapY - :mapY)) ASC
        """
    )
    fun getStoriesByLocationPagingSource(
        mapX: Double, mapY: Double, radiusDeg: Double
    ): PagingSource<Int, StoryEntity>

    @Query(
        """
        SELECT *
        FROM story_table
        WHERE mapX BETWEEN :mapX - :radiusDeg AND :mapX + :radiusDeg
            AND mapY BETWEEN :mapY - :radiusDeg AND :mapY + :radiusDeg
        ORDER BY ((mapX - :mapX) * (mapX - :mapX) + (mapY - :mapY) * (mapY - :mapY)) ASC
        LIMIT :size
        """
    )
    suspend fun getStoriesByLocation(
        mapX: Double, mapY: Double, radiusDeg: Double,
        size: Int
    ): List<StoryEntity>

    @Query(
        """
        SELECT *
        FROM story_table
        WHERE title LIKE '%' || :keyword || '%'
            OR audioTitle LIKE '%' || :keyword || '%'
            OR script LIKE '%' || :keyword || '%'
        """
    )
    fun getStoriesByKeywordPagingSource(keyword: String): PagingSource<Int, StoryEntity>

    @Query(
        """
        SELECT *
        FROM story_table
        WHERE title LIKE '%' || :keyword || '%'
            OR audioTitle LIKE '%' || :keyword || '%'
            OR script LIKE '%' || :keyword || '%'
        LIMIT :size
        """
    )
    suspend fun getStoriesByKeyword(keyword: String, size: Int): List<StoryEntity>

    @Query(
        """
        SELECT *
        FROM story_table
        WHERE storyId = :storyId AND storyLangId = :storyLangId
        """
    )
    suspend fun getStoryById(storyId: Int, storyLangId: Int): StoryEntity?

    @Query(
        """
        DELETE
        FROM story_table
        """
    )
    suspend fun deleteStories()
}