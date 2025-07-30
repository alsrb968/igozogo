package io.jacob.igozogo.core.data.mapper

import io.jacob.igozogo.core.data.*
import org.junit.Assert.assertEquals
import org.junit.Test

class OdiiMapperTest {
    @Test
    fun `Given ThemeResponse, When toThemeEntity is called, Then return ThemeEntity`() {
        // Given
        val response = testThemeResponses.first()

        // When
        val entity = response.toThemeEntity()

        // Then
        assertEquals(testThemeEntities.first(), entity)
    }

    @Test
    fun `Given List of ThemeResponse, When toThemeEntity is called, Then return List of ThemeEntity`() {
        // Given
        val responses = testThemeResponses

        // When
        val entities = responses.toThemeEntity()

        // Then
        assertEquals(testThemeEntities, entities)
    }

    @Test
    fun `Given ThemeEntity, When toPlace is called, Then return Place`() {
        // Given
        val entity = testThemeEntities.first()

        // When
        val place = entity.toPlace()

        // Then
        assertEquals(testPlace, place)
    }

    @Test
    fun `Given List of ThemeEntity, When toPlace is called, Then return List of Place`() {
        // Given
        val entities = testThemeEntities

        // When
        val places = entities.toPlace()

        // Then
        assertEquals(testPlaces, places)
    }

    @Test
    fun `Given StoryResponse, When toStoryEntity is called, Then return StoryEntity`() {
        // Given
        val response = testStoryResponses.first()

        // When
        val entity = response.toStoryEntity()

        // Then
        assertEquals(testStoryEntities.first(), entity)
    }

    @Test
    fun `Given List of StoryResponse, When toStoryEntity is called, Then return List of StoryEntity`() {
        // Given
        val responses = testStoryResponses

        // When
        val entities = responses.toStoryEntity()

        // Then
        assertEquals(testStoryEntities, entities)
    }

    @Test
    fun `Given StoryEntity, When toStory is called, Then return Story`() {
        // Given
        val entity = testStoryEntities.first()

        // When
        val story = entity.toStory()

        // Then
        assertEquals(testStory, story)
    }

    @Test
    fun `Given List of StoryEntity, When toStory is called, Then return List of Story`() {
        // Given
        val entities = testStoryEntities

        // When
        val stories = entities.toStory()

        // Then
        assertEquals(testStories, stories)
    }

    @Test
    fun `Given StoryEntity, When toStoryRemoteKey is called, Then return StoryRemoteKey`() {
        // Given
        val entity = testStoryEntities.first()

        // When
        val remoteKey = entity.toStoryRemoteKey(
            query = "testQuery",
            prevPage = 1,
            nextPage = 2
        )

        // Then
        assertEquals(entity.storyLangId, remoteKey.id)
        assertEquals("testQuery", remoteKey.queryType)
        assertEquals(1, remoteKey.prevPage)
        assertEquals(2, remoteKey.nextPage)
    }

    @Test
    fun `Given List of StoryEntity, When toStoryRemoteKey is called, Then return List of StoryRemoteKey`() {
        // Given
        val entities = testStoryEntities

        // When
        val remoteKeys = entities.toStoryRemoteKey(
            query = "testQuery",
            prevPage = 1,
            nextPage = 2
        )

        // Then
        assertEquals(entities.size, remoteKeys.size)
        remoteKeys.forEachIndexed { index, remoteKey ->
            assertEquals(entities[index].storyLangId, remoteKey.id)
            assertEquals("testQuery", remoteKey.queryType)
            assertEquals(1, remoteKey.prevPage)
            assertEquals(2, remoteKey.nextPage)
        }
    }
}