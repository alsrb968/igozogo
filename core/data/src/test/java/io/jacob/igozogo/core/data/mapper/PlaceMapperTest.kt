package io.jacob.igozogo.core.data.mapper

import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity
import io.jacob.igozogo.core.data.model.remote.odii.ThemeResponse
import io.jacob.igozogo.core.data.util.toDateTimeString
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.testing.data.placeTestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlaceMapperTest {

    private val testPlace = placeTestData.first()
    
    private val testThemeResponse = testPlace.toThemeResponse()
    
    private val testThemeEntity = testPlace.asThemeEntity()

    @Test
    fun `Given ThemeResponse with valid data, When toThemeEntity is called, Then return correct ThemeEntity`() {
        // Given
        val response = testThemeResponse

        // When
        val entity = response.toThemeEntity()

        // Then
        assertEquals(testPlace.placeId, entity.themeId)
        assertEquals(testPlace.placeLangId, entity.themeLangId)
        assertEquals(testPlace.placeCategory, entity.themeCategory)
        assertEquals(testPlace.address1, entity.addr1)
        assertEquals(testPlace.address2, entity.addr2)
        assertEquals(testPlace.title, entity.title)
        assertEquals(testPlace.mapX, entity.mapX, 0.0)
        assertEquals(testPlace.mapY, entity.mapY, 0.0)
        assertEquals(testPlace.langCheck, entity.langCheck)
        assertEquals(testPlace.langCode, entity.langCode)
        assertEquals(testPlace.imageUrl, entity.imageUrl)
        assertEquals(testPlace.createdTime, entity.createdTime)
        assertEquals(testPlace.modifiedTime, entity.modifiedTime)
    }

    @Test
    fun `Given ThemeResponse with empty strings, When toThemeEntity is called, Then return ThemeEntity with empty strings`() {
        // Given
        val response = testThemeResponse.copy(
            themeCategory = "",
            addr1 = "",
            addr2 = "",
            title = "",
            langCheck = "",
            langCode = "",
            imageUrl = ""
        )

        // When
        val entity = response.toThemeEntity()

        // Then
        assertEquals("", entity.themeCategory)
        assertEquals("", entity.addr1)
        assertEquals("", entity.addr2)
        assertEquals("", entity.title)
        assertEquals("", entity.langCheck)
        assertEquals("", entity.langCode)
        assertEquals("", entity.imageUrl)
    }

    @Test
    fun `Given ThemeResponse with zero coordinates, When toThemeEntity is called, Then return ThemeEntity with zero coordinates`() {
        // Given
        val response = testThemeResponse.copy(mapX = 0.0, mapY = 0.0)

        // When
        val entity = response.toThemeEntity()

        // Then
        assertEquals(0.0, entity.mapX, 0.0)
        assertEquals(0.0, entity.mapY, 0.0)
    }

    @Test
    fun `Given empty List of ThemeResponse, When toThemeEntities is called, Then return empty List of ThemeEntity`() {
        // Given
        val responses = emptyList<ThemeResponse>()

        // When
        val entities = responses.toThemeEntities()

        // Then
        assertTrue(entities.isEmpty())
    }

    @Test
    fun `Given List of ThemeResponse, When toThemeEntities is called, Then return List of ThemeEntity with same size`() {
        // Given
        val testPlaces = placeTestData.take(2)
        val responses = testPlaces.toThemeResponses()

        // When
        val entities = responses.toThemeEntities()

        // Then
        assertEquals(responses.size, entities.size)
        assertEquals(testPlaces[0].placeId, entities[0].themeId)
        assertEquals(testPlaces[1].placeId, entities[1].themeId)
    }

    @Test
    fun `Given ThemeEntity with valid data, When toPlace is called, Then return correct Place`() {
        // Given
        val entity = testThemeEntity

        // When
        val place = entity.toPlace()

        // Then
        assertEquals(testPlace.placeId, place.placeId)
        assertEquals(testPlace.placeLangId, place.placeLangId)
        assertEquals(testPlace.placeCategory, place.placeCategory)
        assertEquals(testPlace.address1, place.address1)
        assertEquals(testPlace.address2, place.address2)
        assertEquals(testPlace.title, place.title)
        assertEquals(testPlace.mapX, place.mapX, 0.0)
        assertEquals(testPlace.mapY, place.mapY, 0.0)
        assertEquals(testPlace.langCheck, place.langCheck)
        assertEquals(testPlace.langCode, place.langCode)
        assertEquals(testPlace.imageUrl, place.imageUrl)
        assertEquals(testPlace.createdTime, place.createdTime)
        assertEquals(testPlace.modifiedTime, place.modifiedTime)
    }

    @Test
    fun `Given empty List of ThemeEntity, When toPlaces is called, Then return empty List of Place`() {
        // Given
        val entities = emptyList<ThemeEntity>()

        // When
        val places = entities.toPlaces()

        // Then
        assertTrue(places.isEmpty())
    }

    @Test
    fun `Given List of ThemeEntity, When toPlaces is called, Then return List of Place with same size`() {
        // Given
        val testPlaces = placeTestData.take(2)
        val entities = testPlaces.asThemeEntities()

        // When
        val places = entities.toPlaces()

        // Then
        assertEquals(entities.size, places.size)
        assertEquals(testPlaces[0].placeId, places[0].placeId)
        assertEquals(testPlaces[1].placeId, places[1].placeId)
    }

    @Test
    fun `Given Place with valid data, When toThemeEntity is called, Then return correct ThemeEntity`() {
        // Given
        val place = testPlace

        // When
        val entity = place.asThemeEntity()

        // Then
        assertEquals(place.placeId, entity.themeId)
        assertEquals(place.placeLangId, entity.themeLangId)
        assertEquals(place.placeCategory, entity.themeCategory)
        assertEquals(place.address1, entity.addr1)
        assertEquals(place.address2, entity.addr2)
        assertEquals(place.title, entity.title)
        assertEquals(place.mapX, entity.mapX, 0.0)
        assertEquals(place.mapY, entity.mapY, 0.0)
        assertEquals(place.langCheck, entity.langCheck)
        assertEquals(place.langCode, entity.langCode)
        assertEquals(place.imageUrl, entity.imageUrl)
        assertEquals(place.createdTime, entity.createdTime)
        assertEquals(place.modifiedTime, entity.modifiedTime)
    }

    @Test
    fun `Given empty List of Place, When toThemeEntities is called, Then return empty List of ThemeEntity`() {
        // Given
        val places = emptyList<Place>()

        // When
        val entities = places.asThemeEntities()

        // Then
        assertTrue(entities.isEmpty())
    }

    @Test
    fun `Given List of Place, When toThemeEntities is called, Then return List of ThemeEntity with same size`() {
        // Given
        val places = placeTestData.take(2)

        // When
        val entities = places.asThemeEntities()

        // Then
        assertEquals(places.size, entities.size)
        assertEquals(places[0].placeId, entities[0].themeId)
        assertEquals(places[1].placeId, entities[1].themeId)
    }

    @Test
    fun `Given Place with valid data, When toThemeResponse is called, Then return correct ThemeResponse`() {
        // Given
        val place = testPlace

        // When
        val response = place.toThemeResponse()

        // Then
        assertEquals(place.placeId, response.themeId)
        assertEquals(place.placeLangId, response.themeLangId)
        assertEquals(place.placeCategory, response.themeCategory)
        assertEquals(place.address1, response.addr1)
        assertEquals(place.address2, response.addr2)
        assertEquals(place.title, response.title)
        assertEquals(place.mapX, response.mapX, 0.0)
        assertEquals(place.mapY, response.mapY, 0.0)
        assertEquals(place.langCheck, response.langCheck)
        assertEquals(place.langCode, response.langCode)
        assertEquals(place.imageUrl, response.imageUrl)
        assertEquals(place.createdTime.toDateTimeString(), response.createdTime)
        assertEquals(place.modifiedTime.toDateTimeString(), response.modifiedTime)
    }

    @Test
    fun `Given Place with negative coordinates, When toThemeResponse is called, Then return ThemeResponse with negative coordinates`() {
        // Given
        val place = testPlace.copy(mapX = -126.905507, mapY = -36.306984)

        // When
        val response = place.toThemeResponse()

        // Then
        assertEquals(-126.905507, response.mapX, 0.0)
        assertEquals(-36.306984, response.mapY, 0.0)
    }

    @Test
    fun `Given empty List of Place, When toThemeResponses is called, Then return empty List of ThemeResponse`() {
        // Given
        val places = emptyList<Place>()

        // When
        val responses = places.toThemeResponses()

        // Then
        assertTrue(responses.isEmpty())
    }

    @Test
    fun `Given List of Place, When toThemeResponses is called, Then return List of ThemeResponse with same size`() {
        // Given
        val places = placeTestData.take(2)

        // When
        val responses = places.toThemeResponses()

        // Then
        assertEquals(places.size, responses.size)
        assertEquals(places[0].placeId, responses[0].themeId)
        assertEquals(places[1].placeId, responses[1].themeId)
    }

    @Test
    fun `Given Place with large coordinate values, When toThemeResponse is called, Then return ThemeResponse with same values`() {
        // Given
        val place = testPlace.copy(mapX = 999.999999, mapY = 999.999999)

        // When
        val response = place.toThemeResponse()

        // Then
        assertEquals(999.999999, response.mapX, 0.0)
        assertEquals(999.999999, response.mapY, 0.0)
    }

    @Test
    fun `Given ThemeResponse to ThemeEntity to Place roundtrip, When all conversions applied, Then preserve data integrity`() {
        // Given
        val originalResponse = testThemeResponse

        // When
        val entity = originalResponse.toThemeEntity()
        val place = entity.toPlace()
        val backToEntity = place.asThemeEntity()
        val backToResponse = place.toThemeResponse()

        // Then
        assertEquals(originalResponse.themeId, place.placeId)
        assertEquals(originalResponse.title, place.title)
        assertEquals(entity.themeId, backToEntity.themeId)
        assertEquals(entity.title, backToEntity.title)
        assertEquals(originalResponse.themeId, backToResponse.themeId)
        assertEquals(originalResponse.title, backToResponse.title)
    }

    @Test
    fun `Given Place with testing data, When toThemeEntity is called, Then return correct mapping`() {
        // Given
        val place = placeTestData.first()

        // When
        val entity = place.asThemeEntity()

        // Then
        assertEquals(place.placeId, entity.themeId)
        assertEquals(place.placeLangId, entity.themeLangId)
        assertEquals(place.placeCategory, entity.themeCategory)
        assertEquals(place.address1, entity.addr1)
        assertEquals(place.address2, entity.addr2)
        assertEquals(place.title, entity.title)
    }

    @Test
    fun `Given List of testing places, When toThemeEntities is called, Then return correct size and mapping`() {
        // Given
        val places = placeTestData

        // When
        val entities = places.asThemeEntities()

        // Then
        assertEquals(places.size, entities.size)
        places.forEachIndexed { index, place ->
            assertEquals(place.placeId, entities[index].themeId)
            assertEquals(place.title, entities[index].title)
        }
    }
}