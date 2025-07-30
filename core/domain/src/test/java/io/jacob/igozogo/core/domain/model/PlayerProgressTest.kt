package io.jacob.igozogo.core.domain.model

import io.jacob.igozogo.core.domain.testPlayerProgress
import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerProgressTest {
    @Test
    fun `Given playerProgress, When get it, Then return correct members`() {
        // Given
        val playerProgress = testPlayerProgress

        // When
        val positionRatio = playerProgress.positionRatio
        val bufferedRatio = playerProgress.bufferedRatio
        val formattedPosition = playerProgress.formattedPosition
        val formattedDuration = playerProgress.formattedDuration

        // Then
        assertEquals(0.5f, positionRatio)
        assertEquals(0.7f, bufferedRatio)
        assertEquals("00:05", formattedPosition)
        assertEquals("00:10", formattedDuration)
    }
}