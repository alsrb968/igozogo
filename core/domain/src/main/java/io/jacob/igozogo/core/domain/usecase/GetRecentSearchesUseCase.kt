package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.repository.RecentSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentSearchesUseCase @Inject constructor(
    private val recentSearchRepository: RecentSearchRepository
) {
    operator fun invoke(limit: Int = 10): Flow<List<String>> {
        return recentSearchRepository.getRecentSearches(limit)
    }
}