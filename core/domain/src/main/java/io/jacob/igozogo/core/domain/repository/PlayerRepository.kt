package io.jacob.igozogo.core.domain.repository

import io.jacob.igozogo.core.domain.model.PlayerProgress
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.util.PlaybackState
import io.jacob.igozogo.core.domain.util.RepeatMode
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun play(story: Story)
    fun play(stories: List<Story>, indexToPlay: Int? = null)
    fun playIndex(index: Int)
    fun pause()
    fun resume()
    fun stop()
    fun next()
    fun previous()
    fun seekTo(position: Long)
    fun setShuffle(isShuffle: Boolean)
    fun setRepeat(repeatMode: RepeatMode)
    fun addTrack(story: Story, index: Int? = null)
    fun addTrack(stories: List<Story>, index: Int? = null)
    fun removeTrack(index: Int)
    fun clearPlayList()
    fun release()

    val nowPlaying: Flow<Story?>
    val playlist: Flow<List<Story>>
    val indexOfList: Flow<Int>
    val playerProgress: Flow<PlayerProgress>
    val playbackState: Flow<PlaybackState>
    val isPlaying: Flow<Boolean>
    val isShuffle: Flow<Boolean>
    val repeatMode: Flow<RepeatMode>
}