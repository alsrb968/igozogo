package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.repository.PlaceRepository
import io.jacob.igozogo.core.domain.repository.StoryRepository
import io.jacob.igozogo.core.model.SearchResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchKeywordUseCase @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val storyRepository: StoryRepository,
) {
    operator fun invoke(
        keyword: String,
        size: Int = 20,
    ): Flow<SearchResult> = flow {
        val (places, stories) = coroutineScope {
            val placeDeferred = async { placeRepository.getPlacesByKeyword(keyword, size) }
            val storyDeferred = async { storyRepository.getStoriesByKeyword(keyword, size) }

            placeDeferred.await() to storyDeferred.await()
        }
        emit(SearchResult(places, stories))
    }
}