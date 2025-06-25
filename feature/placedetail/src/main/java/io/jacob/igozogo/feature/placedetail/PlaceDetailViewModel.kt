package io.jacob.igozogo.feature.placedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.usecase.GetPlaceAndStoriesByIdUseCase
import kotlinx.coroutines.flow.*

@HiltViewModel(assistedFactory = PlaceDetailViewModel.Factory::class)
class PlaceDetailViewModel @AssistedInject constructor(
    getPlaceAndStoriesByIdUseCase: GetPlaceAndStoriesByIdUseCase,
    @Assisted("placeId") val placeId: Int,
    @Assisted("placeLangId") val placeLangId: Int,
) : ViewModel() {
    val state: StateFlow<PlaceDetailState> = flow {
        emit(getPlaceAndStoriesByIdUseCase(placeId, placeLangId))
    }.map { result ->
        result.fold(
            onSuccess = { (place, stories) ->
                PlaceDetailState.Success(place, stories)
            },
            onFailure = {
                PlaceDetailState.Error
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlaceDetailState.Loading
    )

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("placeId") placeId: Int,
            @Assisted("placeLangId") placeLangId: Int
        ): PlaceDetailViewModel
    }
}

sealed interface PlaceDetailState {
    data object Loading : PlaceDetailState
    data object Error : PlaceDetailState
    data class Success(val place: Place, val stories: List<Story>) : PlaceDetailState
}