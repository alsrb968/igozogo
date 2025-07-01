package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.repository.PlaceRepository
import io.jacob.igozogo.core.domain.repository.StoryRepository
import javax.inject.Inject

class GetStoryAndPlaceByIdUseCase @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val storyRepository: StoryRepository,
) {
    suspend operator fun invoke(storyId: Int, storyLangId: Int): Pair<Place?, Story?> {
        val story = storyRepository.getStoryById(storyId, storyLangId)
        val place = story?.let {
            placeRepository.getPlaceById(it.placeId, it.placeLangId)
        }
        return Pair(place, story)
    }
}