package io.jacob.igozogo.feature.placedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.usecase.GetPlaceByIdUseCase
import io.jacob.igozogo.core.domain.usecase.GetStoriesByPlaceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PlaceDetailViewModel.Factory::class)
class PlaceDetailViewModel @AssistedInject constructor(
    getPlaceByIdUseCase: GetPlaceByIdUseCase,
    getStoriesByPlaceUseCase: GetStoriesByPlaceUseCase,
    @Assisted("placeId") val placeId: Int,
    @Assisted("placeLangId") val placeLangId: Int,
) : ViewModel() {

    private val _state = MutableStateFlow<PlaceDetailUiState>(PlaceDetailUiState.Loading)
    val state: StateFlow<PlaceDetailUiState> = _state

    init {
        viewModelScope.launch {
            try {
                getPlaceByIdUseCase(placeId, placeLangId)?.let { place ->
                    val stories = getStoriesByPlaceUseCase(place).cachedIn(viewModelScope)
                    _state.value = PlaceDetailUiState.Success(place, stories)
                } ?: run {
                    _state.value = PlaceDetailUiState.Error
                }
            } catch (_: Exception) {
                _state.value = PlaceDetailUiState.Error
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("placeId") placeId: Int,
            @Assisted("placeLangId") placeLangId: Int
        ): PlaceDetailViewModel
    }
}

sealed interface PlaceDetailUiState {
    data object Loading : PlaceDetailUiState
    data object Error : PlaceDetailUiState
    data class Success(val place: Place, val stories: Flow<PagingData<Story>>) : PlaceDetailUiState
}