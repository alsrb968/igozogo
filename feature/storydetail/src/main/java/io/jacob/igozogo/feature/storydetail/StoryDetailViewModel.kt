package io.jacob.igozogo.feature.storydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.usecase.GetStoryByIdUseCase
import kotlinx.coroutines.flow.*

@HiltViewModel(assistedFactory = StoryDetailViewModel.Factory::class)
class StoryDetailViewModel @AssistedInject constructor(
    getStoryByIdUseCase: GetStoryByIdUseCase,
    @Assisted("storyId") storyId: Int,
    @Assisted("storyLangId") storyLangId: Int
) : ViewModel() {
    val state: StateFlow<StoryDetailState> = flow {
        emit(getStoryByIdUseCase(storyId, storyLangId))
    }.map { story ->
        if (story != null) {
            StoryDetailState.Success(story)
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
    data class Success(val story: Story) : StoryDetailState
}