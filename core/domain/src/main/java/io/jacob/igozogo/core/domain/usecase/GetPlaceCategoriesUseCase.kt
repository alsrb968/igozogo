package io.jacob.igozogo.core.domain.usecase

import androidx.paging.PagingData
import io.jacob.igozogo.core.domain.repository.OdiiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlaceCategoriesUseCase @Inject constructor(
    private val repository: OdiiRepository
) {
    operator fun invoke(): Flow<PagingData<String>> {
        return repository.getPlaceCategories()
    }
}