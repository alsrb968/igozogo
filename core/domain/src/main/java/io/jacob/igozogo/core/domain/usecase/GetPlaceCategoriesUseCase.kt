package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.repository.PlaceRepository
import javax.inject.Inject

class GetPlaceCategoriesUseCase @Inject constructor(
    private val repository: PlaceRepository
) {
    suspend operator fun invoke(): List<String> {
        return repository.getPlaceCategories()
    }
}