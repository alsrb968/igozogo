package io.jacob.igozogo.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jacob.igozogo.core.domain.usecase.GetCategoriesUseCase
import io.jacob.igozogo.core.domain.usecase.GetRecentSearchesUseCase
import io.jacob.igozogo.core.domain.usecase.SearchKeywordUseCase
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.SearchResult
import io.jacob.igozogo.core.model.Story
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    getCategoriesUseCase: GetCategoriesUseCase,
    getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val searchKeywordUseCase: SearchKeywordUseCase,
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isFocused = MutableStateFlow(false)

    @OptIn(FlowPreview::class)
    private val searchResults: Flow<SearchResult> = _searchQuery
        .debounce(500)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isNotEmpty()) {
                searchKeywordUseCase(query)
                    .catch { e ->
                        _effect.tryEmit(SearchEffect.ShowToast(e.message ?: "검색 중 오류가 발생했습니다."))
                        emit(SearchResult())
                    }
            } else {
                flowOf(SearchResult())
            }
        }

    val state: StateFlow<SearchState> = combine(
        getCategoriesUseCase().catch { e ->
            _effect.tryEmit(SearchEffect.ShowToast(e.message ?: "카테고리를 불러오는데 실패했습니다."))
            emit(emptyList())
        },
        getRecentSearchesUseCase().catch { e ->
            _effect.tryEmit(SearchEffect.ShowToast(e.message ?: "최근 검색어를 불러오는데 실패했습니다."))
            emit(emptyList())
        },
        _searchQuery,
        _isFocused,
        searchResults
    ) { categories, recentSearches, query, isFocused, searchResult ->
        when {
            isFocused && query.isEmpty() ->
                SearchState.RecentSearchesDisplay(recentSearches)

            query.isNotEmpty() ->
                SearchState.Success(
                    places = searchResult.places,
                    stories = searchResult.stories,
                )

            else ->
                SearchState.CategoriesDisplay(categories)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchState.Loading
    )

    private val _action = MutableSharedFlow<SearchAction>(extraBufferCapacity = 1)

    private val _effect = MutableSharedFlow<SearchEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<SearchEffect> = _effect.asSharedFlow()

    init {
        handleActions()
    }

    private fun handleActions() = viewModelScope.launch {
        _action.collectLatest { action ->
            when (action) {
                is SearchAction.Search -> search(action.query)
                is SearchAction.UpdateFocus -> updateFocus(action.isFocused)
                is SearchAction.Clear -> clear()
            }
        }
    }

    fun sendAction(action: SearchAction) = viewModelScope.launch {
        _action.emit(action)
    }

    private fun search(query: String) = viewModelScope.launch {
        _searchQuery.value = query
    }

    private fun updateFocus(isFocused: Boolean) = viewModelScope.launch {
        _isFocused.value = isFocused
    }

    private fun clear() = viewModelScope.launch {
        _searchQuery.value = ""
        _isFocused.value = false
    }
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

sealed interface SearchAction {
    data class Search(val query: String) : SearchAction
    data class UpdateFocus(val isFocused: Boolean) : SearchAction
    data object Clear : SearchAction
}

sealed interface SearchEffect {
    data class ShowToast(val message: String) : SearchEffect
    data class NavigateToPlaceDetails(val placeId: String) : SearchEffect
    data class NavigateToStoryDetails(val storyId: String) : SearchEffect
}