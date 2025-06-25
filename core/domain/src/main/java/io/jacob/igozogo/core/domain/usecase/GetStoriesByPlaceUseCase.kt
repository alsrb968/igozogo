package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.repository.StoryRepository
import javax.inject.Inject

class GetStoriesByPlaceUseCase @Inject constructor(
    private val repository: StoryRepository
) {
    suspend operator fun invoke(place: Place): List<Story> {
        return repository.getStoriesByPlace(place)
    }
}