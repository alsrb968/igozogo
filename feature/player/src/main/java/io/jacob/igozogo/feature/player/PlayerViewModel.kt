package io.jacob.igozogo.feature.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.PlayerProgress
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.repository.PlayerRepository
import io.jacob.igozogo.core.domain.util.RepeatMode
import io.jacob.igozogo.core.domain.util.combine
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
) : ViewModel() {
    val state: StateFlow<PlayerState> = combine(
        playerRepository.nowPlaying,
        playerRepository.playlist,
        playerRepository.indexOfList,
        playerRepository.playerProgress,
        playerRepository.isPlaying,
        playerRepository.isShuffle,
        playerRepository.repeatMode
    ) { nowPlaying, playlist, indexOfList, progress, isPlaying, isShuffle, repeatMode ->
        if (nowPlaying == null) {
            PlayerState.Idle
        } else {
            PlayerState.Ready(
                nowPlaying = nowPlaying,
                playlist = playlist,
                indexOfList = indexOfList,
                progress = progress,
                isPlaying = isPlaying,
                isShuffle = isShuffle,
                repeatMode = repeatMode
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlayerState.Idle
    )

    private val _action = MutableSharedFlow<PlayerAction>(extraBufferCapacity = 1)

    private val _effect = MutableSharedFlow<PlayerEffect>(extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    private val _isShowPlayer = MutableStateFlow(false)
    val isShowPlayer = _isShowPlayer.asStateFlow()

    init {
        handleActions()
    }

    private fun handleActions() = viewModelScope.launch {
        _action.collect { action ->
            when (action) {
                is PlayerAction.PlayOrPause -> playOrPause()
                is PlayerAction.Next -> {}
                is PlayerAction.Previous -> {}
                is PlayerAction.Shuffle -> {}
                is PlayerAction.Repeat -> {}
                is PlayerAction.PlayIndex -> {}
                is PlayerAction.SeekTo -> {}
                is PlayerAction.ClickPlayer -> {}
                is PlayerAction.ClickPlace -> {}
                is PlayerAction.ClickStory -> {}
            }
        }
    }

    fun sendAction(action: PlayerAction) = viewModelScope.launch {
        _action.emit(action)
    }

    private fun playOrPause() = viewModelScope.launch {
        val ready = state.value as? PlayerState.Ready ?: return@launch
        if (ready.isPlaying) {
            playerRepository.pause()
        } else {
            playerRepository.resume()
        }
    }
}

sealed interface PlayerState {
    data object Idle : PlayerState
    data class Ready(
        val nowPlaying: Story,
        val playlist: List<Story>,
        val indexOfList: Int,
        val progress: PlayerProgress,
        val isPlaying: Boolean,
        val isShuffle: Boolean,
        val repeatMode: RepeatMode
    ) : PlayerState
}

sealed interface PlayerAction {
    data object PlayOrPause : PlayerAction
    data object Next : PlayerAction
    data object Previous : PlayerAction
    data object Shuffle : PlayerAction
    data object Repeat : PlayerAction
    data class PlayIndex(val index: Int) : PlayerAction
    data class SeekTo(val position: Long) : PlayerAction
    data object ClickPlayer : PlayerAction
    data class ClickPlace(val place: Place) : PlayerAction
    data class ClickStory(val story: Story) : PlayerAction
}

sealed interface PlayerEffect {
    data object NavigateToPlayer : PlayerEffect
    data class NavigateToPlaceDetail(val place: Place) : PlayerEffect
    data class NavigateToStoryDetail(val story: Story) : PlayerEffect
}