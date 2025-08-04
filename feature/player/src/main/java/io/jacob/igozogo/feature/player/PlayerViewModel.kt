package io.jacob.igozogo.feature.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jacob.igozogo.core.domain.repository.PlaceRepository
import io.jacob.igozogo.core.domain.repository.PlayerRepository
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.PlayerProgress
import io.jacob.igozogo.core.model.RepeatMode
import io.jacob.igozogo.core.model.Story
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val placeRepository: PlaceRepository,
) : ViewModel() {

    private val content = combine(
        playerRepository.nowPlaying,
        playerRepository.playlist,
        playerRepository.indexOfList,
        playerRepository.playerProgress
    ) { nowPlaying, playlist, indexOfList, progress ->
        nowPlaying?.let { np ->
            PlayerContentState(
                nowPlaying = np,
                playlist = playlist,
                indexOfList = indexOfList,
                playerProgress = progress
            )
        }
    }

    private val meta = combine(
        playerRepository.isPlaying,
        playerRepository.isShuffle,
        playerRepository.repeatMode,
        playerRepository.playbackSpeed
    ) { isPlaying, isShuffle, repeatMode, playbackSpeed ->
        PlayerMetaState(
            isPlaying = isPlaying,
            isShuffle = isShuffle,
            repeatMode = repeatMode,
            playbackSpeed = playbackSpeed
        )
    }

    val state: StateFlow<PlayerState> = combine(
        content, meta
    ) { content, meta ->
        content?.let { it to meta }
    }.filterNotNull()
        .flatMapLatest { (content, meta) ->
            flow {
                emit(
                    placeRepository.getPlaceById(
                        placeId = content.nowPlaying.placeId,
                        placeLangId = content.nowPlaying.placeLangId
                    )?.let { place ->
                        PlayerState.Ready(
                            content = content,
                            meta = meta,
                            place = place
                        )
                    } ?: PlayerState.Idle
                )
            }
        }
        .catch { emit(PlayerState.Idle) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlayerState.Idle
        )

    private val _action = MutableSharedFlow<PlayerAction>(extraBufferCapacity = 1)

    private val _effect = MutableSharedFlow<PlayerEffect>(extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    init {
        handleActions()
    }

    private fun handleActions() = viewModelScope.launch {
        _action.collect { action ->
            when (action) {
                is PlayerAction.PlayOrPause -> playOrPause()
                is PlayerAction.Next -> next()
                is PlayerAction.Previous -> previous()
                is PlayerAction.Shuffle -> shuffle()
                is PlayerAction.Repeat -> repeat()
                is PlayerAction.PlayIndex -> playIndex(action.index)
                is PlayerAction.SeekTo -> seekTo(action.position)
                is PlayerAction.SeekBackward -> seekBackward()
                is PlayerAction.SeekForward -> seekForward()
                is PlayerAction.PlaybackSpeed -> playbackSpeed(action.speed)
                is PlayerAction.ClickPlace -> clickPlace(action.place)
                is PlayerAction.ClickStory -> clickStory(action.story)
                is PlayerAction.ExpandPlayer -> expandPlayer()
                is PlayerAction.CollapsePlayer -> collapsePlayer()
            }
        }
    }

    fun sendAction(action: PlayerAction) = viewModelScope.launch {
        _action.emit(action)
    }

    private fun playOrPause() = viewModelScope.launch {
        playerRepository.playOrPause()
    }

    private fun next() {
        playerRepository.next()
    }

    private fun previous() {
        playerRepository.previous()
    }

    private fun shuffle() = viewModelScope.launch {
        playerRepository.shuffle()
    }

    private fun repeat() = viewModelScope.launch {
        playerRepository.changeRepeat()
    }

    private fun playIndex(index: Int) {
        playerRepository.playIndex(index)
    }

    private fun seekTo(position: Long) {
        playerRepository.seekTo(position)
    }

    private fun seekBackward() {
        playerRepository.seekBackward()
    }

    private fun seekForward() {
        playerRepository.seekForward()
    }

    private fun playbackSpeed(speed: Float) {
        playerRepository.setPlaybackSpeed(speed)
    }

    private fun clickPlace(place: Place) = viewModelScope.launch {
        _effect.emit(PlayerEffect.NavigateToPlaceDetail(place))
    }

    private fun clickStory(story: Story) = viewModelScope.launch {
        _effect.emit(PlayerEffect.NavigateToStoryDetail(story))
    }

    private fun expandPlayer() = viewModelScope.launch {
        _effect.emit(PlayerEffect.ShowPlayerBottomSheet)
    }

    private fun collapsePlayer() = viewModelScope.launch {
        _effect.emit(PlayerEffect.HidePlayerBottomSheet)
    }
}

data class PlayerContentState(
    val nowPlaying: Story,
    val playlist: List<Story>,
    val indexOfList: Int,
    val playerProgress: PlayerProgress,
)

data class PlayerMetaState(
    val isPlaying: Boolean,
    val isShuffle: Boolean,
    val repeatMode: RepeatMode,
    val playbackSpeed: Float,
)

sealed interface PlayerState {
    data object Idle : PlayerState
    data class Ready(
        val content: PlayerContentState,
        val meta: PlayerMetaState,
        val place: Place,
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
    data object SeekBackward : PlayerAction
    data object SeekForward : PlayerAction
    data class PlaybackSpeed(val speed: Float) : PlayerAction
    data class ClickPlace(val place: Place) : PlayerAction
    data class ClickStory(val story: Story) : PlayerAction
    data object ExpandPlayer : PlayerAction
    data object CollapsePlayer : PlayerAction
}

sealed interface PlayerEffect {
    data class NavigateToPlaceDetail(val place: Place) : PlayerEffect
    data class NavigateToStoryDetail(val story: Story) : PlayerEffect
    data object ShowPlayerBottomSheet : PlayerEffect
    data object HidePlayerBottomSheet : PlayerEffect
}