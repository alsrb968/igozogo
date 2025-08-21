package io.jacob.igozogo.core.data.datasource.local

import io.jacob.igozogo.core.data.db.RecentSearchDao
import io.jacob.igozogo.core.data.model.local.search.RecentSearchEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RecentSearchDataSource {
    fun getRecentSearches(limit: Int = 10): Flow<List<RecentSearchEntity>>
    suspend fun upsertRecentSearch(recentSearch: RecentSearchEntity)
    suspend fun deleteRecentSearch(query: String)
    suspend fun clearRecentSearches()
    suspend fun getRecentSearchesCount(): Int
}

class RecentSearchDataSourceImpl @Inject constructor(
    private val dao: RecentSearchDao,
) : RecentSearchDataSource {
    override fun getRecentSearches(limit: Int): Flow<List<RecentSearchEntity>> {
        return dao.getRecentSearches(limit)
    }

    override suspend fun upsertRecentSearch(recentSearch: RecentSearchEntity) {
        dao.upsertRecentSearch(recentSearch)
    }

    override suspend fun deleteRecentSearch(query: String) {
        dao.deleteRecentSearch(query)
    }

    override suspend fun clearRecentSearches() {
        dao.clearRecentSearches()
    }

    override suspend fun getRecentSearchesCount(): Int {
        return dao.getRecentSearchesCount()
    }
}