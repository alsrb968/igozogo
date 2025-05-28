package io.jacob.igozogo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jacob.igozogo.core.domain.usecase.GetPlaceCategoriesUseCase
import io.jacob.igozogo.core.domain.usecase.SyncPlacesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val syncPlacesUseCase: SyncPlacesUseCase,
    private val getPlaceCategoriesUseCase: GetPlaceCategoriesUseCase,
) : ViewModel() {

    init {
        viewModelScope.launch {
            syncPlacesUseCase()
        }
    }

    fun getPlaceCategories(): Flow<PagingData<String>> {
        return getPlaceCategoriesUseCase()
    }
}