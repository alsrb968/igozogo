package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.repository.PlaceRepository
import javax.inject.Inject

class GetPlaceByIdUseCase @Inject constructor(
    private val repository: PlaceRepository
) {
    suspend operator fun invoke(placeId: Int, placeLangId: Int): Place? {
        return repository.getPlaceById(placeId, placeLangId)
    }
}