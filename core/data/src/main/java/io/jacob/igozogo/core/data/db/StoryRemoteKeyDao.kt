package io.jacob.igozogo.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.jacob.igozogo.core.data.model.local.odii.StoryRemoteKey

@Dao
interface StoryRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKeys(keys: List<StoryRemoteKey>)

    @Query(
        """
        SELECT *
        FROM story_remote_key_table
        WHERE id = :id AND queryType = :queryType
        """
    )
    suspend fun getRemoteKey(id: Int, queryType: String): StoryRemoteKey?

    @Query(
        """
        DELETE FROM story_remote_key_table
        WHERE queryType = :queryType
        """
    )
    suspend fun deleteRemoteKeysByQueryType(queryType: String)

    @Query(
        """
        DELETE
        FROM story_remote_key_table
        """
    )
    suspend fun deleteRemoteKeys()
}