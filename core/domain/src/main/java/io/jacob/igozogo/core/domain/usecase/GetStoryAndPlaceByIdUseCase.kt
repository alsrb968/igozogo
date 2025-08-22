package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.repository.PlaceRepository
import io.jacob.igozogo.core.domain.repository.StoryRepository
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.Story
import javax.inject.Inject

class GetStoryAndPlaceByIdUseCase @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val storyRepository: StoryRepository,
) {
    suspend operator fun invoke(storyId: Int, storyLangId: Int): Result<Pair<Place, Story>> {
        return runCatching {
            val story = storyRepository.getStoryById(storyId, storyLangId)
                ?: throw NullPointerException("story is null")
            val place = placeRepository.getPlaceById(story.placeId, story.placeLangId)
                ?: throw NullPointerException("place is null")
            place to story
        }
    }
}