package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.repository.PlaceRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val placeRepository: PlaceRepository
) {
    suspend operator fun invoke(): List<String> {
        return placeRepository.getPlaceCategories()
    }
}