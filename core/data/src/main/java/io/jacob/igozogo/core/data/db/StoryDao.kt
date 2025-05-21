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
        WHERE title LIKE '%' || :keyword || '%'
            OR audioTitle LIKE '%' || :keyword || '%'
            OR script LIKE '%' || :keyword || '%'
        """
    )
    fun searchStories(keyword: String): PagingSource<Int, StoryEntity>

    @Query(
        """
        DELETE
        FROM story_table
        """
    )
    suspend fun deleteStories()
}