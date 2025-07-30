package io.jacob.igozogo.core.domain.model

import io.jacob.igozogo.core.domain.testPlace
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class PlaceTest {
    @Test
    fun `Given place, When get fullAddress, Then return full address`() {
        // Given
        val place = testPlace

        // When
        val result = place.fullAddress

        // Then
        assertEquals("${place.address1} ${place.address2}", result)
    }

    @Test
    fun `Given place, When get humanReadableCreatedTime, Then return human readable created time`() {
        // Given
        val place = testPlace

        // When
        Locale.setDefault(Locale.KOREAN)
        val resultKorea = place.humanReadableCreatedTime
        Locale.setDefault(Locale.ENGLISH)
        val resultEnglish = place.humanReadableCreatedTime

        // Then
        assertEquals("2024년 1월 25일", resultKorea)
        assertEquals("January 25, 2024", resultEnglish)
    }

    @Test
    fun `Given place, When get humanReadableModifiedTime, Then return human readable modified time`() {
        // Given
        val place = testPlace

        // When
        Locale.setDefault(Locale.KOREAN)
        val resultKorea = place.humanReadableModifiedTime
        Locale.setDefault(Locale.ENGLISH)
        val resultEnglish = place.humanReadableModifiedTime

        // Then
        assertEquals("2024년 1월 25일", resultKorea)
        assertEquals("January 25, 2024", resultEnglish)
    }
}