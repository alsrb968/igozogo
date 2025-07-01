package io.jacob.igozogo.core.domain.repository

import io.jacob.igozogo.core.domain.model.PlayerProgress
import io.jacob.igozogo.core.domain.util.PlaybackState
import io.jacob.igozogo.core.domain.util.RepeatMode
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun play(url: String)
    fun play(urls: List<String>, indexToPlay: Int? = null)
    fun playIndex(index: Int)
    fun pause()
    fun resume()
    fun stop()
    fun next()
    fun previous()
    fun seekTo(position: Long)
    fun setShuffle(isShuffle: Boolean)
    fun setRepeat(repeatMode: RepeatMode)
    fun addTrack(url: String)
    fun addTrack(urls: List<String>)
    fun removeTrack(index: Int)
    fun clearPlayList()
    fun release()

    val playlist: Flow<List<String>>
    val track: Flow<String?>
    val playerProgress: Flow<PlayerProgress>
    val playbackState: Flow<PlaybackState>
    val isPlaying: Flow<Boolean>
    val isShuffle: Flow<Boolean>
    val repeatMode: Flow<RepeatMode>
}