package io.jacob.igozogo.core.domain.usecase

import androidx.paging.PagingData
import io.jacob.igozogo.core.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlaceCategoriesUseCase @Inject constructor(
    private val repository: PlaceRepository
) {
    operator fun invoke(): Flow<PagingData<String>> {
        return repository.getPlaceCategoriesPaging()
    }
}