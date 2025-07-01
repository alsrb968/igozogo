package io.jacob.igozogo.core.data.datasource.player

import androidx.media3.common.*
import androidx.media3.exoplayer.ExoPlayer
import io.jacob.igozogo.core.domain.model.PlayerProgress
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

interface PlayerDataSource {
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
    fun setRepeat(repeatMode: Int)
    fun addTrack(url: String)
    fun addTrack(urls: List<String>)
    fun removeTrack(index: Int)
    fun clearPlayList()
    fun release()

    val playlist: Flow<List<String>>
    val track: Flow<String?>
    val playerProgress: Flow<PlayerProgress>
    val playbackState: Flow<Int>
    val isPlaying: Flow<Boolean>
    val isShuffle: Flow<Boolean>
    val repeatMode: Flow<Int>
}

class PlayerDataSourceImpl @Inject constructor(
    val player: ExoPlayer
) : PlayerDataSource {
    private val _playlist = MutableStateFlow<List<String>>(emptyList())
    private val _track = MutableStateFlow<String?>(null)
    private val _playbackState = MutableStateFlow(Player.STATE_IDLE)
    private val _isPlaying = MutableStateFlow(false)
    private val _isShuffle = MutableStateFlow(false)
    private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)

    private val listener = object : Player.Listener {
        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            Timber.d(
                "timeline.periodCount=%d, timeline.windowCount=%d, reason: %s"
                    .format(timeline.periodCount, timeline.windowCount, reason)
            )

            when (reason) {
                Player.TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED -> {
                    Timber.d("TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED")
                }

                Player.TIMELINE_CHANGE_REASON_SOURCE_UPDATE -> {
                    Timber.d("TIMELINE_CHANGE_REASON_SOURCE_UPDATE")
                }
            }

            val items = mutableListOf<MediaItem>()
            for (i in 0 until player.mediaItemCount) {
                items.add(player.getMediaItemAt(i))
            }
            _playlist.value = items.mapNotNull { item ->
                item.localConfiguration?.uri?.path
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            Timber.d("onMediaItemTransition uri: ${mediaItem?.localConfiguration?.uri}, reason: $reason, mediaId: ${mediaItem?.mediaId}")
            when (reason) {
                Player.MEDIA_ITEM_TRANSITION_REASON_REPEAT -> {
                    Timber.d("MEDIA_ITEM_TRANSITION_REASON_REPEAT")
                }

                Player.MEDIA_ITEM_TRANSITION_REASON_AUTO -> {
                    Timber.d("MEDIA_ITEM_TRANSITION_REASON_AUTO")
                }

                Player.MEDIA_ITEM_TRANSITION_REASON_SEEK -> {
                    Timber.d("MEDIA_ITEM_TRANSITION_REASON_SEEK")
                }

                Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED -> {
                    Timber.d("MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED")
                }
            }

            _track.value = mediaItem?.localConfiguration?.uri?.path
        }

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            Timber.d(
                "title=%s, artist=%s, albumTitle=%s, albumArtist=%s, displayTitle=%s, subTitle=%s, description=%s, durationMs=%d, artworkData=%s, artworkDatType=%d, artworkUri=%s, trackNumber=%d, totalTrackCount=%d"
                    .format(
                        mediaMetadata.title,
                        mediaMetadata.artist,
                        mediaMetadata.albumTitle,
                        mediaMetadata.albumArtist,
                        mediaMetadata.displayTitle,
                        mediaMetadata.subtitle,
                        mediaMetadata.description,
                        mediaMetadata.durationMs,
                        mediaMetadata.artworkData,
                        mediaMetadata.artworkDataType,
                        mediaMetadata.artworkUri,
                        mediaMetadata.trackNumber,
                        mediaMetadata.totalTrackCount
                    )
            )
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            _playbackState.value = playbackState
            when (playbackState) {
                Player.STATE_IDLE -> Timber.d("STATE_IDLE")
                Player.STATE_BUFFERING -> Timber.d("STATE_BUFFERING")
                Player.STATE_READY -> Timber.d("STATE_READY")
                Player.STATE_ENDED -> Timber.d("STATE_ENDED")
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            Timber.d("isPlaying: $isPlaying")
            _isPlaying.value = isPlaying
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            _repeatMode.value = repeatMode
            when (repeatMode) {
                Player.REPEAT_MODE_OFF -> Timber.d("REPEAT_MODE_OFF")
                Player.REPEAT_MODE_ONE -> Timber.d("REPEAT_MODE_ONE")
                Player.REPEAT_MODE_ALL -> Timber.d("REPEAT_MODE_ALL")
            }
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            Timber.d("shuffleModeEnabled: $shuffleModeEnabled")
            _isShuffle.value = shuffleModeEnabled
        }

        override fun onPlayerError(error: PlaybackException) {
            Timber.e("error: $error")
        }
    }

    init {
        player.addListener(listener)
    }

    override fun play(url: String) {
        player.playWhenReady = true
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
    }

    override fun play(urls: List<String>, indexToPlay: Int?) {
        player.playWhenReady = true
        player.setMediaItems(urls.map { MediaItem.fromUri(it) })
        indexToPlay?.let { player.seekToDefaultPosition(it) }
        player.prepare()
    }

    override fun playIndex(index: Int) {
        if (index in 0 until player.mediaItemCount) {
            player.playWhenReady = true
            player.seekToDefaultPosition(index)
        }
    }

    override fun pause() {
        if (player.isPlaying) {
            player.pause()
        }
    }

    override fun resume() {
        if (!player.isPlaying) {
            player.play()
        }
    }

    override fun stop() {
        player.stop()
        player.clearMediaItems()
    }

    override fun next() {
        if (player.hasNextMediaItem()) {
            player.playWhenReady = true
            player.seekToNextMediaItem()
        }
    }

    override fun previous() {
        if (player.hasPreviousMediaItem()) {
            player.playWhenReady = true
            player.seekToPreviousMediaItem()
        }
    }

    override fun seekTo(position: Long) {
        player.seekTo(position)
    }

    override fun setShuffle(isShuffle: Boolean) {
        player.shuffleModeEnabled = isShuffle
    }

    override fun setRepeat(repeatMode: Int) {
        player.repeatMode = repeatMode
    }

    override fun addTrack(url: String) {
        player.addMediaItem(MediaItem.fromUri(url))
    }

    override fun addTrack(urls: List<String>) {
        player.addMediaItems(urls.map { MediaItem.fromUri(it) })
    }

    override fun removeTrack(index: Int) {
        player.removeMediaItem(index)
    }

    override fun clearPlayList() {
        player.clearMediaItems()
    }

    override fun release() {
        player.release()
        player.removeListener(listener)
    }

    override val playlist: Flow<List<String>>
        get() = _playlist

    override val track: Flow<String?>
        get() = _track

    override val playerProgress: Flow<PlayerProgress>
        get() = flow {
            while (true) {
                emit(
                    PlayerProgress(
                        position = player.currentPosition,
                        buffered = player.bufferedPosition,
                        duration = player.duration.coerceAtLeast(1L),
                        bufferedPercentage = player.bufferedPercentage,
                    )
                )
                delay(500L)
            }
        }

    override val playbackState: Flow<Int>
        get() = _playbackState

    override val isPlaying: Flow<Boolean>
        get() = _isPlaying

    override val isShuffle: Flow<Boolean>
        get() = _isShuffle

    override val repeatMode: Flow<Int>
        get() = _repeatMode
}