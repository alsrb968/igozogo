package io.jacob.igozogo.core.data.repository

import io.jacob.igozogo.core.data.datasource.player.PlayerDataSource
import io.jacob.igozogo.core.domain.model.PlayerProgress
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.repository.PlayerRepository
import io.jacob.igozogo.core.domain.util.PlaybackState
import io.jacob.igozogo.core.domain.util.RepeatMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val playerDataSource: PlayerDataSource
) : PlayerRepository {
    override fun play(story: Story) {
        playerDataSource.play(story)
    }

    override fun play(stories: List<Story>, indexToPlay: Int?) {
        playerDataSource.play(stories, indexToPlay)
    }

    override fun playIndex(index: Int) {
        playerDataSource.playIndex(index)
    }

    override fun pause() {
        playerDataSource.pause()
    }

    override fun resume() {
        playerDataSource.resume()
    }

    override fun stop() {
        playerDataSource.stop()
    }

    override fun next() {
        playerDataSource.next()
    }

    override fun previous() {
        playerDataSource.previous()
    }

    override fun seekTo(position: Long) {
        playerDataSource.seekTo(position)
    }

    override fun setShuffle(isShuffle: Boolean) {
        playerDataSource.setShuffle(isShuffle)
    }

    override fun setRepeat(repeatMode: RepeatMode) {
        playerDataSource.setRepeat(repeatMode.value)
    }

    override fun addTrack(story: Story, index: Int?) {
        playerDataSource.addTrack(story, index)
    }

    override fun addTrack(stories: List<Story>, index: Int?) {
        playerDataSource.addTrack(stories, index)
    }

    override fun removeTrack(index: Int) {
        playerDataSource.removeTrack(index)
    }

    override fun clearPlayList() {
        playerDataSource.clearPlayList()
    }

    override fun release() {
        playerDataSource.release()
    }

    override val nowPlaying: Flow<Story?>
        get() = playerDataSource.nowPlaying

    override val playlist: Flow<List<Story>>
        get() = playerDataSource.playlist

    override val indexOfList: Flow<Int>
        get() = playerDataSource.indexOfList

    override val playerProgress: Flow<PlayerProgress>
        get() = playerDataSource.playerProgress

    override val playbackState: Flow<PlaybackState>
        get() = playerDataSource.playbackState.map { PlaybackState.fromValue(it) }

    override val isPlaying: Flow<Boolean>
        get() = playerDataSource.isPlaying

    override val isShuffle: Flow<Boolean>
        get() = playerDataSource.isShuffle

    override val repeatMode: Flow<RepeatMode>
        get() = playerDataSource.repeatMode.map { RepeatMode.fromValue(it) }
}