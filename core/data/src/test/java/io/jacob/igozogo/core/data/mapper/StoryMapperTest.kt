package io.jacob.igozogo.core.data.mapper

import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import io.jacob.igozogo.core.data.model.remote.odii.StoryResponse
import io.jacob.igozogo.core.data.util.toDateTimeString
import io.jacob.igozogo.core.model.Story
import io.jacob.igozogo.core.testing.data.storyTestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StoryMapperTest {

    private val testStory = storyTestData.first()
    
    private val testStoryResponse = testStory.toStoryResponse()
    
    private val testStoryEntity = testStory.asStoryEntity()

    @Test
    fun `Given StoryResponse with valid data, When toStoryEntity is called, Then return correct StoryEntity`() {
        // Given
        val response = testStoryResponse

        // When
        val entity = response.toStoryEntity()

        // Then
        assertEquals(testStory.placeId, entity.themeId)
        assertEquals(testStory.placeLangId, entity.themeLangId)
        assertEquals(testStory.storyId, entity.storyId)
        assertEquals(testStory.storyLangId, entity.storyLangId)
        assertEquals(testStory.title, entity.title)
        assertEquals(testStory.mapX, entity.mapX, 0.0)
        assertEquals(testStory.mapY, entity.mapY, 0.0)
        assertEquals(testStory.audioTitle, entity.audioTitle)
        assertEquals(testStory.script, entity.script)
        assertEquals(testStory.playTime, entity.playTime) // should be converted to milliseconds
        assertEquals(testStory.audioUrl, entity.audioUrl)
        assertEquals(testStory.langCode, entity.langCode)
        assertEquals(testStory.imageUrl, entity.imageUrl)
        assertEquals(testStory.createdTime, entity.createdTime)
        assertEquals(testStory.modifiedTime, entity.modifiedTime)
    }

    @Test
    fun `Given StoryResponse with playTime in seconds, When toStoryEntity is called, Then return StoryEntity with playTime in milliseconds`() {
        // Given
        val response = testStoryResponse.copy(playTime = 120) // 120 seconds

        // When
        val entity = response.toStoryEntity()

        // Then
        assertEquals(120000L, entity.playTime) // should be 120000 milliseconds
    }

    @Test
    fun `Given StoryResponse with zero playTime, When toStoryEntity is called, Then return StoryEntity with zero playTime`() {
        // Given
        val response = testStoryResponse.copy(playTime = 0)

        // When
        val entity = response.toStoryEntity()

        // Then
        assertEquals(0L, entity.playTime)
    }

    @Test
    fun `Given StoryResponse with empty strings, When toStoryEntity is called, Then return StoryEntity with empty strings`() {
        // Given
        val response = testStoryResponse.copy(
            title = "",
            audioTitle = "",
            script = "",
            audioUrl = "",
            langCode = "",
            imageUrl = ""
        )

        // When
        val entity = response.toStoryEntity()

        // Then
        assertEquals("", entity.title)
        assertEquals("", entity.audioTitle)
        assertEquals("", entity.script)
        assertEquals("", entity.audioUrl)
        assertEquals("", entity.langCode)
        assertEquals("", entity.imageUrl)
    }

    @Test
    fun `Given empty List of StoryResponse, When toStoryEntities is called, Then return empty List of StoryEntity`() {
        // Given
        val responses = emptyList<StoryResponse>()

        // When
        val entities = responses.toStoryEntities()

        // Then
        assertTrue(entities.isEmpty())
    }

    @Test
    fun `Given List of StoryResponse, When toStoryEntities is called, Then return List of StoryEntity with same size`() {
        // Given
        val testStories = storyTestData.take(2)
        val responses = testStories.toStoryResponses()

        // When
        val entities = responses.toStoryEntities()

        // Then
        assertEquals(responses.size, entities.size)
        assertEquals(testStories[0].storyId, entities[0].storyId)
        assertEquals(testStories[1].storyId, entities[1].storyId)
    }

    @Test
    fun `Given StoryEntity with valid data, When toStory is called, Then return correct Story`() {
        // Given
        val entity = testStoryEntity

        // When
        val story = entity.toStory()

        // Then
        assertEquals(testStory.placeId, story.placeId)
        assertEquals(testStory.placeLangId, story.placeLangId)
        assertEquals(testStory.storyId, story.storyId)
        assertEquals(testStory.storyLangId, story.storyLangId)
        assertEquals(testStory.title, story.title)
        assertEquals(testStory.mapX, story.mapX, 0.0)
        assertEquals(testStory.mapY, story.mapY, 0.0)
        assertEquals(testStory.audioTitle, story.audioTitle)
        assertEquals(testStory.script, story.script)
        assertEquals(testStory.playTime, story.playTime)
        assertEquals(testStory.audioUrl, story.audioUrl)
        assertEquals(testStory.langCode, story.langCode)
        assertEquals(testStory.imageUrl, story.imageUrl)
        assertEquals(testStory.createdTime, story.createdTime)
        assertEquals(testStory.modifiedTime, story.modifiedTime)
    }

    @Test
    fun `Given empty List of StoryEntity, When toStories is called, Then return empty List of Story`() {
        // Given
        val entities = emptyList<StoryEntity>()

        // When
        val stories = entities.toStories()

        // Then
        assertTrue(stories.isEmpty())
    }

    @Test
    fun `Given List of StoryEntity, When toStories is called, Then return List of Story with same size`() {
        // Given
        val testStories = storyTestData.take(2)
        val entities = testStories.asStoryEntities()

        // When
        val stories = entities.toStories()

        // Then
        assertEquals(entities.size, stories.size)
        assertEquals(testStories[0].storyId, stories[0].storyId)
        assertEquals(testStories[1].storyId, stories[1].storyId)
    }

    @Test
    fun `Given Story with valid data, When toStoryEntity is called, Then return correct StoryEntity`() {
        // Given
        val story = testStory

        // When
        val entity = story.asStoryEntity()

        // Then
        assertEquals(story.placeId, entity.themeId)
        assertEquals(story.placeLangId, entity.themeLangId)
        assertEquals(story.storyId, entity.storyId)
        assertEquals(story.storyLangId, entity.storyLangId)
        assertEquals(story.title, entity.title)
        assertEquals(story.mapX, entity.mapX, 0.0)
        assertEquals(story.mapY, entity.mapY, 0.0)
        assertEquals(story.audioTitle, entity.audioTitle)
        assertEquals(story.script, entity.script)
        assertEquals(story.playTime, entity.playTime)
        assertEquals(story.audioUrl, entity.audioUrl)
        assertEquals(story.langCode, entity.langCode)
        assertEquals(story.imageUrl, entity.imageUrl)
        assertEquals(story.createdTime, entity.createdTime)
        assertEquals(story.modifiedTime, entity.modifiedTime)
    }

    @Test
    fun `Given empty List of Story, When toStoryEntities is called, Then return empty List of StoryEntity`() {
        // Given
        val stories = emptyList<Story>()

        // When
        val entities = stories.asStoryEntities()

        // Then
        assertTrue(entities.isEmpty())
    }

    @Test
    fun `Given List of Story, When toStoryEntities is called, Then return List of StoryEntity with same size`() {
        // Given
        val stories = storyTestData.take(2)

        // When
        val entities = stories.asStoryEntities()

        // Then
        assertEquals(stories.size, entities.size)
        assertEquals(stories[0].storyId, entities[0].storyId)
        assertEquals(stories[1].storyId, entities[1].storyId)
    }

    @Test
    fun `Given Story with valid data, When toStoryResponse is called, Then return correct StoryResponse`() {
        // Given
        val story = testStory

        // When
        val response = story.toStoryResponse()

        // Then
        assertEquals(story.placeId, response.themeId)
        assertEquals(story.placeLangId, response.themeLangId)
        assertEquals(story.storyId, response.storyId)
        assertEquals(story.storyLangId, response.storyLangId)
        assertEquals(story.title, response.title)
        assertEquals(story.mapX, response.mapX, 0.0)
        assertEquals(story.mapY, response.mapY, 0.0)
        assertEquals(story.audioTitle, response.audioTitle)
        assertEquals(story.script, response.script)
        assertEquals((story.playTime / 1000).toInt(), response.playTime) // should be converted back to seconds
        assertEquals(story.audioUrl, response.audioUrl)
        assertEquals(story.langCode, response.langCode)
        assertEquals(story.imageUrl, response.imageUrl)
        assertEquals(story.createdTime.toDateTimeString(), response.createdTime)
        assertEquals(story.modifiedTime.toDateTimeString(), response.modifiedTime)
    }

    @Test
    fun `Given Story with playTime in milliseconds, When toStoryResponse is called, Then return StoryResponse with playTime in seconds`() {
        // Given
        val story = testStory.copy(playTime = 300000L) // 300 seconds in milliseconds

        // When
        val response = story.toStoryResponse()

        // Then
        assertEquals(300, response.playTime) // should be 300 seconds
    }

    @Test
    fun `Given Story with playTime not divisible by 1000, When toStoryResponse is called, Then return StoryResponse with truncated seconds`() {
        // Given
        val story = testStory.copy(playTime = 300500L) // 300.5 seconds in milliseconds

        // When
        val response = story.toStoryResponse()

        // Then
        assertEquals(300, response.playTime) // should be truncated to 300 seconds
    }

    @Test
    fun `Given empty List of Story, When toStoryResponses is called, Then return empty List of StoryResponse`() {
        // Given
        val stories = emptyList<Story>()

        // When
        val responses = stories.toStoryResponses()

        // Then
        assertTrue(responses.isEmpty())
    }

    @Test
    fun `Given List of Story, When toStoryResponses is called, Then return List of StoryResponse with same size`() {
        // Given
        val stories = storyTestData.take(2)

        // When
        val responses = stories.toStoryResponses()

        // Then
        assertEquals(stories.size, responses.size)
        assertEquals(stories[0].storyId, responses[0].storyId)
        assertEquals(stories[1].storyId, responses[1].storyId)
    }

    @Test
    fun `Given StoryResponse to StoryEntity to Story roundtrip, When all conversions applied, Then preserve data integrity except playTime conversion`() {
        // Given
        val originalResponse = testStoryResponse

        // When
        val entity = originalResponse.toStoryEntity()
        val story = entity.toStory()
        val backToEntity = story.asStoryEntity()
        val backToResponse = story.toStoryResponse()

        // Then
        assertEquals(originalResponse.storyId, story.storyId)
        assertEquals(originalResponse.title, story.title)
        assertEquals(originalResponse.playTime * 1000L, story.playTime) // playTime should be converted to milliseconds
        assertEquals(entity.storyId, backToEntity.storyId)
        assertEquals(entity.title, backToEntity.title)
        assertEquals(originalResponse.storyId, backToResponse.storyId)
        assertEquals(originalResponse.title, backToResponse.title)
        assertEquals(originalResponse.playTime, backToResponse.playTime) // playTime should be converted back to seconds
    }

    @Test
    fun `Given Story with negative coordinates, When toStoryResponse is called, Then return StoryResponse with negative coordinates`() {
        // Given
        val story = testStory.copy(mapX = -126.8539117, mapY = -35.1529468)

        // When
        val response = story.toStoryResponse()

        // Then
        assertEquals(-126.8539117, response.mapX, 0.0)
        assertEquals(-35.1529468, response.mapY, 0.0)
    }

    @Test
    fun `Given Story with testing data, When toStoryEntity is called, Then return correct mapping`() {
        // Given
        val story = storyTestData.first()

        // When
        val entity = story.asStoryEntity()

        // Then
        assertEquals(story.placeId, entity.themeId)
        assertEquals(story.placeLangId, entity.themeLangId)
        assertEquals(story.storyId, entity.storyId)
        assertEquals(story.storyLangId, entity.storyLangId)
        assertEquals(story.title, entity.title)
        assertEquals(story.playTime, entity.playTime)
    }

    @Test
    fun `Given List of testing stories, When toStoryEntities is called, Then return correct size and mapping`() {
        // Given
        val stories = storyTestData

        // When
        val entities = stories.asStoryEntities()

        // Then
        assertEquals(stories.size, entities.size)
        stories.forEachIndexed { index, story ->
            assertEquals(story.storyId, entities[index].storyId)
            assertEquals(story.title, entities[index].title)
            assertEquals(story.playTime, entities[index].playTime)
        }
    }

    @Test
    fun `Given StoryResponse with large playTime value, When toStoryEntity is called, Then return StoryEntity with correct milliseconds conversion`() {
        // Given
        val response = testStoryResponse.copy(playTime = Int.MAX_VALUE)

        // When
        val entity = response.toStoryEntity()

        // Then
        assertEquals(Int.MAX_VALUE * 1000L, entity.playTime)
    }

    @Test
    fun `Given Story with very large playTime, When toStoryResponse is called, Then return StoryResponse with correct seconds conversion`() {
        // Given
        val story = testStory.copy(playTime = Long.MAX_VALUE)

        // When
        val response = story.toStoryResponse()

        // Then
        assertEquals((Long.MAX_VALUE / 1000).toInt(), response.playTime)
    }
}