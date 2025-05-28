package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.repository.OdiiRepository
import timber.log.Timber
import javax.inject.Inject

class SyncPlacesUseCase @Inject constructor(
    private val repository: OdiiRepository
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