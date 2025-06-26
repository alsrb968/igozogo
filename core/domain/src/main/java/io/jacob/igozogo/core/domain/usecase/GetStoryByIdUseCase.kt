package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.repository.StoryRepository
import javax.inject.Inject

class GetStoryByIdUseCase @Inject constructor(
    private val repository: StoryRepository
) {
    suspend operator fun invoke(storyId: Int, storyLangId: Int): Story? {
        return repository.getStoryById(storyId, storyLangId)
    }
}