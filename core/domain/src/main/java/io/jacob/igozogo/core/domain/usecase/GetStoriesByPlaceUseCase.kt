package io.jacob.igozogo.core.domain.usecase

import androidx.paging.PagingData
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.repository.OdiiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoriesByPlaceUseCase @Inject constructor(
    private val repository: OdiiRepository
) {
    operator fun invoke(place: Place): Flow<PagingData<Story>> {
        return repository.getStoriesByPlace(place)
    }
}