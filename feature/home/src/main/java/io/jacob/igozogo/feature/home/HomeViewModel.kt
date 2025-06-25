package io.jacob.igozogo.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.usecase.FeedSection
import io.jacob.igozogo.core.domain.usecase.SyncAndGetFeedsUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val syncAndGetFeedsUseCase: SyncAndGetFeedsUseCase
) : ViewModel() {
    val state: StateFlow<HomeState> = flow {
        emit(syncAndGetFeedsUseCase {
            viewModelScope.launch { _effect.emit(HomeEffect.Synced) }
        })
    }.map { feeds ->
        HomeState.Success(feeds)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeState.Loading
    )

    private val _action = MutableSharedFlow<HomeAction>(extraBufferCapacity = 1)

    private val _effect = MutableSharedFlow<HomeEffect>(extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    init {
        handleActions()
    }

    private fun handleActions() = viewModelScope.launch {
        _action.collectLatest { action ->
            when (action) {
                is HomeAction.ClickCategory -> clickCategory(action.category)
                is HomeAction.ClickPlace -> clickPlace(action.place)
                is HomeAction.ClickStory -> clickStory(action.story)
            }
        }
    }

    fun sendAction(action: HomeAction) = viewModelScope.launch {
        _action.emit(action)
    }

    private fun clickCategory(category: String) = viewModelScope.launch {
        _effect.emit(HomeEffect.NavigateToCategoryDetails(category))
    }

    private fun clickPlace(place: Place) = viewModelScope.launch {
        _effect.emit(HomeEffect.NavigateToPlaceDetails(place))
    }

    private fun clickStory(story: Story) = viewModelScope.launch {
        _effect.emit(HomeEffect.NavigateToStoryDetails(story))
    }
}

sealed interface HomeState {
    data object Loading : HomeState
    data object Error : HomeState
    data class Success(val feedSections: List<FeedSection>) : HomeState
}

sealed interface HomeAction {
    data class ClickCategory(val category: String) : HomeAction
    data class ClickPlace(val place: Place) : HomeAction
    data class ClickStory(val story: Story) : HomeAction
}

sealed interface HomeEffect {
    data object Synced : HomeEffect
    data class NavigateToCategoryDetails(val category: String) : HomeEffect
    data class NavigateToPlaceDetails(val place: Place) : HomeEffect
    data class NavigateToStoryDetails(val story: Story) : HomeEffect
}