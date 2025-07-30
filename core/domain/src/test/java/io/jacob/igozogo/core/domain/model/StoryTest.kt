package io.jacob.igozogo.core.domain.model

import io.jacob.igozogo.core.domain.testStory
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class StoryTest {
    @Test
    fun `Given story, When get humanReadableCreatedTime, Then return human readable created time`() {
        // Given
        val story = testStory

        // When
        Locale.setDefault(Locale.KOREAN)
        val resultKorea = story.humanReadableCreatedTime
        Locale.setDefault(Locale.ENGLISH)
        val resultEnglish = story.humanReadableCreatedTime

        // Then
        assertEquals("2023년 7월 25일", resultKorea)
        assertEquals("July 25, 2023", resultEnglish)
    }

    @Test
    fun `Given story, When get humanReadableModifiedTime, Then return human readable modified time`() {
        // Given
        val story = testStory

        // When
        Locale.setDefault(Locale.KOREAN)
        val resultKorea = story.humanReadableModifiedTime
        Locale.setDefault(Locale.ENGLISH)
        val resultEnglish = story.humanReadableModifiedTime

        // Then
        assertEquals("2025년 6월 9일", resultKorea)
        assertEquals("June 9, 2025", resultEnglish)
    }
}