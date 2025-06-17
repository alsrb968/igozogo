package io.jacob.igozogo.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.usecase.GetPlaceCategoriesUseCase
import io.jacob.igozogo.core.domain.usecase.GetPlacesUseCase
import io.jacob.igozogo.core.domain.usecase.SyncPlacesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiEffect {
    data object Synced : HomeUiEffect
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val syncPlacesUseCase: SyncPlacesUseCase,
    private val getPlaceCategoriesUseCase: GetPlaceCategoriesUseCase,
    private val getPlacesUseCase: GetPlacesUseCase,
) : ViewModel() {

    private val _effect = MutableSharedFlow<HomeUiEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<HomeUiEffect> = _effect

    init {
        viewModelScope.launch {
            val isSynced = syncPlacesUseCase()
            if (isSynced) {
                _effect.emit(HomeUiEffect.Synced)
            }
        }
    }

    fun getPlaceCategories(): Flow<PagingData<String>> {
        return getPlaceCategoriesUseCase().cachedIn(viewModelScope)
    }

    fun getPlaces(): Flow<PagingData<Place>> {
        return getPlacesUseCase().cachedIn(viewModelScope)
    }
}