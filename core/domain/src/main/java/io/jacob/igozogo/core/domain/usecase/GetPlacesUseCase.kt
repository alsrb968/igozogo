package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.repository.PlaceRepository
import javax.inject.Inject

class GetPlacesUseCase @Inject constructor(
    private val repository: PlaceRepository
) {
    suspend operator fun invoke(): List<Place> {
        return repository.getPlaces()
    }
}