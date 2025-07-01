package io.jacob.igozogo.feature.player

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.repository.PlayerRepository
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    fun play(story: Story) {
        playerRepository.play(story.audioUrl)
    }
}

sealed interface PlayerState {
    data object Idle : PlayerState
    data class Ready(
        val indexOfList: Int,
        val story: Story,
        val playlist: List<Story>,
        val isPlaying: Boolean
    )
}