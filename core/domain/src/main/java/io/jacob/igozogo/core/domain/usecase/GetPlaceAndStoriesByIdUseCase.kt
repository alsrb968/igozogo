package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.repository.PlaceRepository
import io.jacob.igozogo.core.domain.repository.StoryRepository
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.Story
import javax.inject.Inject

class GetPlaceAndStoriesByIdUseCase @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val storyRepository: StoryRepository,
) {
    suspend operator fun invoke(placeId: Int, placeLangId: Int): Result<Pair<Place, List<Story>>> {
        return runCatching {
            placeRepository.getPlaceById(placeId, placeLangId)?.let { place ->
                val stories = storyRepository.getStoriesByPlace(place)
                place to stories
            } ?: throw NullPointerException("place is null")
        }
    }
}