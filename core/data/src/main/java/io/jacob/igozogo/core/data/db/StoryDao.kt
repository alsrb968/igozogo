package io.jacob.igozogo.core.data.db

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
        FROM storyTable
        """
    )
    suspend fun getStories(): List<StoryEntity>

    @Query(
        """
        SELECT *
        FROM storyTable
        WHERE themeId = :themeId
        """
    )
    suspend fun getStoriesByThemeId(themeId: Int): List<StoryEntity>

    @Query(
        """
        DELETE
        FROM storyTable
        """
    )
    suspend fun deleteStories()
}