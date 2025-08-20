package io.jacob.igozogo.core.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.jacob.igozogo.core.data.model.local.search.RecentSearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {
    @Query(
        """
        SELECT *
        FROM recent_search_table
        ORDER BY queriedDate DESC
        LIMIT :limit
        """
    )
    fun getRecentSearches(limit: Int): Flow<List<RecentSearchEntity>>

    @Upsert
    suspend fun insertOrReplaceRecentSearch(recentSearch: RecentSearchEntity)

    @Query(
        """
        DELETE
        FROM recent_search_table
        WHERE `query` = :query
        """
    )
    suspend fun deleteRecentSearch(query: String)

    @Query(
        """
        DELETE
        FROM recent_search_table
        """
    )
    suspend fun clearRecentSearches()

    @Query(
        """
        SELECT COUNT(*)
        FROM recent_search_table
        """
    )
    suspend fun getRecentSearchesCount(): Int
}