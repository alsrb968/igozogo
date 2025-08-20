package io.jacob.igozogo.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface RecentSearchRepository {
    fun getRecentSearches(limit: Int = 10): Flow<List<String>>
    suspend fun insertOrReplaceRecentSearch(query: String)
    suspend fun deleteRecentSearch(query: String)
    suspend fun clearRecentSearches()
    suspend fun getRecentSearchesCount(): Int
}