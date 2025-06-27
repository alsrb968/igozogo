package io.jacob.igozogo.feature.storydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.usecase.GetStoryAndPlaceByIdUseCase
import kotlinx.coroutines.flow.*

@HiltViewModel(assistedFactory = StoryDetailViewModel.Factory::class)
class StoryDetailViewModel @AssistedInject constructor(
    getStoryAndPlaceByIdUseCase: GetStoryAndPlaceByIdUseCase,
    @Assisted("storyId") storyId: Int,
    @Assisted("storyLangId") storyLangId: Int
) : ViewModel() {
    val state: StateFlow<StoryDetailState> = flow {
        emit(getStoryAndPlaceByIdUseCase(storyId, storyLangId))
    }.map { (place, story) ->
        if (place != null && story != null) {
            StoryDetailState.Success(place, story)
        } else {
            StoryDetailState.Error
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StoryDetailState.Loading
    )

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("storyId") storyId: Int,
            @Assisted("storyLangId") storyLangId: Int
        ): StoryDetailViewModel
    }
}

sealed interface StoryDetailState {
    object Loading : StoryDetailState
    object Error : StoryDetailState
    data class Success(val place: Place, val story: Story) : StoryDetailState
}