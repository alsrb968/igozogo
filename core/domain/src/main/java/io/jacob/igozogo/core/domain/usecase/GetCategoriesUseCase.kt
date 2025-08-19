package io.jacob.igozogo.core.domain.usecase

import io.jacob.igozogo.core.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val placeRepository: PlaceRepository
) {
    operator fun invoke(): Flow<List<String>> = flow {
        emit(placeRepository.getPlaceCategories())
    }
}