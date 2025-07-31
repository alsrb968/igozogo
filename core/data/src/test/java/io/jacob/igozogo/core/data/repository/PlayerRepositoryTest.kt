package io.jacob.igozogo.core.data.repository

import androidx.media3.common.Player
import io.jacob.igozogo.core.data.datasource.player.PlayerDataSource
import io.jacob.igozogo.core.model.PlaybackState
import io.jacob.igozogo.core.model.RepeatMode
import io.jacob.igozogo.core.testing.data.storyTestData
import io.jacob.igozogo.core.testing.util.MainDispatcherRule
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class PlayerRepositoryTest {
    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val playerDataSource = mockk<PlayerDataSource>(relaxed = true)

    private val repository = PlayerRepositoryImpl(playerDataSource)

    @Test
    fun `Given story, When play is called, Then dataSource play is invoked`() {
        // Given
        val story = storyTestData.first()

        // When
        repository.play(story)

        // Then
        verify { playerDataSource.play(story) }
    }

    @Test
    fun `Given story list with index, When play is called, Then dataSource play is invoked with list and index`() {
        // Given
        val stories = storyTestData

        // When
        repository.play(stories, 0)

        // Then
        verify { playerDataSource.play(stories, 0) }
    }

    @Test
    fun `Given index, When playIndex is called, Then dataSource playIndex is invoked`() {
        // Given
        val index = 2

        // When
        repository.playIndex(index)

        // Then
        verify { playerDataSource.playIndex(index) }
    }

    @Test
    fun `Given player is not playing, When playOrPause is called, Then resume is invoked`() = runTest {
        // Given
        every { playerDataSource.isPlaying } returns flowOf(false)

        // When
        repository.playOrPause()

        // Then
        coVerify(exactly = 0) { playerDataSource.pause() }
        coVerify { playerDataSource.resume() }
    }

    @Test
    fun `Given player is playing, When playOrPause is called, Then pause is invoked`() = runTest {
        // Given
        every { playerDataSource.isPlaying } returns flowOf(true)

        // When
        repository.playOrPause()

        // Then
        coVerify { playerDataSource.pause() }
        coVerify(exactly = 0) { playerDataSource.resume() }
    }

    @Test
    fun `When stop is called, Then stop is invoked`() {
        // When
        repository.stop()

        // Then
        verify { playerDataSource.stop() }
    }

    @Test
    fun `When next is called, Then next is invoked`() {
        // When
        repository.next()

        // Then
        verify { playerDataSource.next() }
    }

    @Test
    fun `When previous is called, Then previous is invoked`() {
        // When
        repository.previous()

        // Then
        verify { playerDataSource.previous() }
    }

    @Test
    fun `Given position, When seekTo is called, Then seekTo is invoked`() {
        // Given
        val position = 1000L

        // When
        repository.seekTo(position)

        // Then
        verify { playerDataSource.seekTo(position) }
    }

    @Test
    fun `When seekBackward is called, Then seekBackward is invoked`() {
        // When
        repository.seekBackward()

        // Then
        verify { playerDataSource.seekBackward() }
    }

    @Test
    fun `When seekForward is called, Then seekForward is invoked`() {
        // When
        repository.seekForward()

        // Then
        verify { playerDataSource.seekForward() }
    }

    @Test
    fun `Given shuffle is off, When shuffle is called, Then setShuffle true is invoked`() = runTest {
        // Given
        every { playerDataSource.isShuffle } returns flowOf(false)
        coJustRun { playerDataSource.setShuffle(any()) }

        // When
        repository.shuffle()

        // Then
        coVerify { playerDataSource.setShuffle(true) }
    }

    @Test
    fun `Given shuffle is on, When shuffle is called, Then setShuffle false is invoked`() = runTest {
        // Given
        every { playerDataSource.isShuffle } returns flowOf(true)
        coJustRun { playerDataSource.setShuffle(any()) }

        // When
        repository.shuffle()

        // Then
        coVerify { playerDataSource.setShuffle(false) }
    }

    @Test
    fun `Given repeat mode is OFF, When changeRepeat is called, Then setRepeat ONE is invoked`() = runTest {
        // Given
        every { playerDataSource.repeatMode } returns flowOf(Player.REPEAT_MODE_OFF)
        coJustRun { playerDataSource.setRepeat(any()) }

        // When
        repository.changeRepeat()

        // Then
        coVerify { playerDataSource.setRepeat(Player.REPEAT_MODE_ONE) }
    }

    @Test
    fun `Given repeat mode is ONE, When changeRepeat is called, Then setRepeat ALL is invoked`() = runTest {
        // Given
        every { playerDataSource.repeatMode } returns flowOf(Player.REPEAT_MODE_ONE)
        coJustRun { playerDataSource.setRepeat(any()) }

        // When
        repository.changeRepeat()

        // Then
        coVerify { playerDataSource.setRepeat(Player.REPEAT_MODE_ALL) }
    }

    @Test
    fun `Given repeat mode is ALL, When changeRepeat is called, Then setRepeat OFF is invoked`() = runTest {
        // Given
        every { playerDataSource.repeatMode } returns flowOf(Player.REPEAT_MODE_ALL)
        coJustRun { playerDataSource.setRepeat(any()) }

        // When
        repository.changeRepeat()

        // Then
        coVerify { playerDataSource.setRepeat(Player.REPEAT_MODE_OFF) }
    }

    @Test
    fun `Given speed, When setPlaybackSpeed is called, Then dataSource setPlaybackSpeed is invoked`() {
        // Given
        val speed = 1.5f

        // When
        repository.setPlaybackSpeed(speed)

        // Then
        verify { playerDataSource.setPlaybackSpeed(speed) }
    }

    @Test
    fun `Given story and index, When addTrack is called, Then dataSource addTrack is invoked`() {
        // Given
        val story = storyTestData.first()
        val index = 2

        // When
        repository.addTrack(story, index)

        // Then
        verify { playerDataSource.addTrack(story, index) }
    }

    @Test
    fun `Given stories and index, When addTrack is called, Then dataSource addTrack is invoked`() {
        // Given
        val stories = storyTestData
        val index = 2

        // When
        repository.addTrack(stories, index)

        // Then
        verify { playerDataSource.addTrack(stories, index) }
    }

    @Test
    fun `Given index, When removeTrack is called, Then dataSource removeTrack is invoked`() {
        // Given
        val index = 2

        // When
        repository.removeTrack(index)

        // Then
        verify { playerDataSource.removeTrack(index) }
    }

    @Test
    fun `When clearPlayList is called, Then dataSource clearPlayList is invoked`() {
        // When
        repository.clearPlayList()

        // Then
        verify { playerDataSource.clearPlayList() }
    }

    @Test
    fun `When release is called, Then dataSource release is invoked`() {
        // When
        repository.release()

        // Then
        verify { playerDataSource.release() }
    }

    @Test
    fun `Given nowPlaying flow, When collected, Then emits expected story`() = runTest {
        // Given
        val story = storyTestData.first()
        every { playerDataSource.nowPlaying } returns flowOf(story)

        // When
        val result = repository.nowPlaying.first()

        // Then
        assertEquals(story, result)
        verify { playerDataSource.nowPlaying }
    }

    @Test
    fun `Given playbackState flow of 3, When collected, Then returns READY state`() = runTest {
        // Given
        val state = Player.STATE_READY
        every { playerDataSource.playbackState } returns flowOf(state)

        // When
        val result = repository.playbackState.first()

        // Then
        assertEquals(PlaybackState.READY, result)
        verify { playerDataSource.playbackState }
    }

    @Test
    fun `Given repeatMode flow of 2, When collected, Then returns ALL mode`() = runTest {
        // Given
        val mode = Player.REPEAT_MODE_ALL
        every { playerDataSource.repeatMode } returns flowOf(mode)

        // When
        val result = repository.repeatMode.first()

        // Then
        assertEquals(RepeatMode.ALL, result)
        verify { playerDataSource.repeatMode }
    }
}