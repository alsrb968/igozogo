package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.repository.PlaceRepository
import io.jacob.igozogo.core.domain.repository.StoryRepository
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.Story
import timber.log.Timber
import javax.inject.Inject

sealed interface FeedSection {
    data class Categories(val categories: List<String>) : FeedSection
    data class Places(val places: List<Place>) : FeedSection
    data class Stories(val stories: List<Story>) : FeedSection
}

class SyncAndGetFeedsUseCase @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val storyRepository: StoryRepository,
) {
    suspend operator fun invoke(
        isSynced: () -> Unit
    ): List<FeedSection> {
        val size = 2079
        if (placeRepository.getPlacesCount() < size) {
            Timber.i("Syncing places...")
            placeRepository.syncPlaces(size)
            isSynced()
        }

        return listOf(
            FeedSection.Categories(placeRepository.getPlaceCategories()),
            FeedSection.Places(placeRepository.getPlaces()),
            FeedSection.Stories(storyRepository.getStories())
        )
    }
}