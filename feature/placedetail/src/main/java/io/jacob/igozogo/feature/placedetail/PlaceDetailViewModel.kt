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
import io.jacob.igozogo.core.domain.usecase.GetStoriesByPlaceUseCase
import kotlinx.coroutines.flow.*

@HiltViewModel(assistedFactory = PlaceDetailViewModel.Factory::class)
class PlaceDetailViewModel @AssistedInject constructor(
    getStoriesByPlaceUseCase: GetStoriesByPlaceUseCase,
    @Assisted val place: Place,
) : ViewModel() {
    val state: StateFlow<PlaceDetailUiState> =
        getStoriesByPlaceUseCase(place)
            .cachedIn(viewModelScope)
            .map<PagingData<Story>, PlaceDetailUiState> { stories ->
                PlaceDetailUiState.Success(stories)
            }
            .catch { emit(PlaceDetailUiState.Error) }
            .onStart { emit(PlaceDetailUiState.Loading) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = PlaceDetailUiState.Loading
            )

    @AssistedFactory
    interface Factory {
        fun create(place: Place): PlaceDetailViewModel
    }
}

sealed interface PlaceDetailUiState {
    data object Loading : PlaceDetailUiState
    data object Error : PlaceDetailUiState
    data class Success(val stories: PagingData<Story>) : PlaceDetailUiState
}