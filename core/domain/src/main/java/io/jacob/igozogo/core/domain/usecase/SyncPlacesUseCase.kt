package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.repository.PlaceRepository
import timber.log.Timber
import javax.inject.Inject

class SyncPlacesUseCase @Inject constructor(
    private val repository: PlaceRepository
) {
    suspend operator fun invoke(): Boolean {
        val size = 2079
        if (repository.getPlacesCount() < size) {
            Timber.i("Syncing places...")
            repository.syncPlaces(size)
            return true
        }
        return false
    }
}