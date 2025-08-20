package io.jacob.igozogo.core.data.repository

import io.jacob.igozogo.core.data.datasource.local.RecentSearchDataSource
import io.jacob.igozogo.core.data.model.local.search.RecentSearchEntity
import io.jacob.igozogo.core.domain.repository.RecentSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject

class RecentSearchRepositoryImpl @Inject constructor(
    private val recentSearchDataSource: RecentSearchDataSource
) : RecentSearchRepository {
    override fun getRecentSearches(limit: Int): Flow<List<String>> {
        return recentSearchDataSource.getRecentSearches(limit).map { recentSearches ->
            recentSearches.map { it.query }
        }
    }

    override suspend fun insertOrReplaceRecentSearch(query: String) {
        recentSearchDataSource.insertOrReplaceRecentSearch(
            RecentSearchEntity(
                query = query,
                queriedDate = Clock.System.now()
            )
        )
    }

    override suspend fun deleteRecentSearch(query: String) {
        recentSearchDataSource.deleteRecentSearch(query)
    }

    override suspend fun clearRecentSearches() {
        recentSearchDataSource.clearRecentSearches()
    }

    override suspend fun getRecentSearchesCount(): Int {
        return recentSearchDataSource.getRecentSearchesCount()
    }
}