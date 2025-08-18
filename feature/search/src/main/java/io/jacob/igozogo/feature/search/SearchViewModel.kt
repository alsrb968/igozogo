package io.jacob.igozogo.feature.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jacob.igozogo.core.domain.usecase.GetCategoriesUseCase
import io.jacob.igozogo.core.domain.usecase.SearchKeywordUseCase
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.Story
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val searchKeywordUseCase: SearchKeywordUseCase,
) : ViewModel() {

}

sealed interface SearchState {
    data object Loading : SearchState
    data class CategoriesDisplay(val categories: List<String>) : SearchState
    data class RecentSearchesDisplay(val recentSearches: List<String>) : SearchState
    data class Success(
        val places: List<Place>,
        val stories: List<Story>
    ) : SearchState {
        val isEmpty: Boolean = places.isEmpty() && stories.isEmpty()
    }

    data class Error(val message: String) : SearchState
}