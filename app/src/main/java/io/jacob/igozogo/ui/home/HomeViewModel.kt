package io.jacob.igozogo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jacob.igozogo.core.domain.usecase.GetThemeCategoriesUseCase
import io.jacob.igozogo.core.domain.usecase.SyncThemesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val syncThemesUseCase: SyncThemesUseCase,
    private val getThemeCategoriesUseCase: GetThemeCategoriesUseCase,
) : ViewModel() {

    fun syncThemes() {
        viewModelScope.launch {
            syncThemesUseCase()
        }
    }

    fun getThemeCategories(): Flow<PagingData<String>> {
        return getThemeCategoriesUseCase()
    }
}