package io.jacob.igozogo.core.domain.usecase

import androidx.paging.PagingData
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.repository.OdiiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlacesUseCase @Inject constructor(
    private val repository: OdiiRepository
) {
    operator fun invoke(): Flow<PagingData<Place>> {
        return repository.getPlaces()
    }
}