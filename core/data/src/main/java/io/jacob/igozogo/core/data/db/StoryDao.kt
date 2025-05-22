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
    fun getStories(): PagingSource<Int, StoryEntity>

    @Query(
        """
        SELECT *
        FROM story_table
        WHERE themeId = :themeId AND themeLangId = :themeLangId
        """
    )
    fun getStoriesByTheme(themeId: Int, themeLangId: Int): PagingSource<Int, StoryEntity>

    @Query(
        """
        SELECT *
        FROM story_table
        WHERE mapX BETWEEN :mapX - :radiusDeg AND :mapX + :radiusDeg
            AND mapY BETWEEN :mapY - :radiusDeg AND :mapY + :radiusDeg
        ORDER BY ((mapX - :mapX) * (mapX - :mapX) + (mapY - :mapY) * (mapY - :mapY)) ASC
        """
    )
    fun getStoriesByLocation(mapX: Double, mapY: Double, radiusDeg: Double): PagingSource<Int, StoryEntity>

    @Query(
        """
        SELECT *
        FROM story_table
        WHERE title LIKE '%' || :keyword || '%'
            OR audioTitle LIKE '%' || :keyword || '%'
            OR script LIKE '%' || :keyword || '%'
        """
    )
    fun getStoriesByKeyword(keyword: String): PagingSource<Int, StoryEntity>

    @Query(
        """
        DELETE
        FROM story_table
        """
    )
    suspend fun deleteStories()
}