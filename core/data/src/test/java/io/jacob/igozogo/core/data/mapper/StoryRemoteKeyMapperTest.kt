package io.jacob.igozogo.core.data.mapper

import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import io.jacob.igozogo.core.data.util.toInstantAsSystemDefault
import io.jacob.igozogo.core.testing.data.storyTestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StoryRemoteKeyMapperTest {

    private val testStoryEntity = storyTestData.first().asStoryEntity()

    @Test
    fun `Given StoryEntity with valid data, When toStoryRemoteKey is called, Then return correct StoryRemoteKey`() {
        // Given
        val entity = testStoryEntity
        val query = "testQuery"
        val prevPage = 1
        val nextPage = 2

        // When
        val remoteKey = entity.toStoryRemoteKey(query, prevPage, nextPage)

        // Then
        assertEquals(entity.storyLangId, remoteKey.id)
        assertEquals(query, remoteKey.queryType)
        assertEquals(prevPage, remoteKey.prevPage)
        assertEquals(nextPage, remoteKey.nextPage)
    }

    @Test
    fun `Given StoryEntity with empty query, When toStoryRemoteKey is called, Then return StoryRemoteKey with empty query`() {
        // Given
        val entity = testStoryEntity
        val query = ""
        val prevPage = 1
        val nextPage = 2

        // When
        val remoteKey = entity.toStoryRemoteKey(query, prevPage, nextPage)

        // Then
        assertEquals(entity.storyLangId, remoteKey.id)
        assertEquals("", remoteKey.queryType)
        assertEquals(prevPage, remoteKey.prevPage)
        assertEquals(nextPage, remoteKey.nextPage)
    }

    @Test
    fun `Given StoryEntity with null prevPage, When toStoryRemoteKey is called, Then return StoryRemoteKey with null prevPage`() {
        // Given
        val entity = testStoryEntity
        val query = "testQuery"
        val prevPage = null
        val nextPage = 2

        // When
        val remoteKey = entity.toStoryRemoteKey(query, prevPage, nextPage)

        // Then
        assertEquals(entity.storyLangId, remoteKey.id)
        assertEquals(query, remoteKey.queryType)
        assertNull(remoteKey.prevPage)
        assertEquals(nextPage, remoteKey.nextPage)
    }

    @Test
    fun `Given StoryEntity with null nextPage, When toStoryRemoteKey is called, Then return StoryRemoteKey with null nextPage`() {
        // Given
        val entity = testStoryEntity
        val query = "testQuery"
        val prevPage = 1
        val nextPage = null

        // When
        val remoteKey = entity.toStoryRemoteKey(query, prevPage, nextPage)

        // Then
        assertEquals(entity.storyLangId, remoteKey.id)
        assertEquals(query, remoteKey.queryType)
        assertEquals(prevPage, remoteKey.prevPage)
        assertNull(remoteKey.nextPage)
    }

    @Test
    fun `Given StoryEntity with both null pages, When toStoryRemoteKey is called, Then return StoryRemoteKey with both null pages`() {
        // Given
        val entity = testStoryEntity
        val query = "testQuery"
        val prevPage = null
        val nextPage = null

        // When
        val remoteKey = entity.toStoryRemoteKey(query, prevPage, nextPage)

        // Then
        assertEquals(entity.storyLangId, remoteKey.id)
        assertEquals(query, remoteKey.queryType)
        assertNull(remoteKey.prevPage)
        assertNull(remoteKey.nextPage)
    }

    @Test
    fun `Given StoryEntity with zero storyLangId, When toStoryRemoteKey is called, Then return StoryRemoteKey with zero id`() {
        // Given
        val entity = testStoryEntity.copy(storyLangId = 0)
        val query = "testQuery"
        val prevPage = 1
        val nextPage = 2

        // When
        val remoteKey = entity.toStoryRemoteKey(query, prevPage, nextPage)

        // Then
        assertEquals(0, remoteKey.id)
        assertEquals(query, remoteKey.queryType)
        assertEquals(prevPage, remoteKey.prevPage)
        assertEquals(nextPage, remoteKey.nextPage)
    }

    @Test
    fun `Given StoryEntity with negative page numbers, When toStoryRemoteKey is called, Then return StoryRemoteKey with negative page numbers`() {
        // Given
        val entity = testStoryEntity
        val query = "testQuery"
        val prevPage = -1
        val nextPage = -2

        // When
        val remoteKey = entity.toStoryRemoteKey(query, prevPage, nextPage)

        // Then
        assertEquals(entity.storyLangId, remoteKey.id)
        assertEquals(query, remoteKey.queryType)
        assertEquals(-1, remoteKey.prevPage)
        assertEquals(-2, remoteKey.nextPage)
    }

    @Test
    fun `Given empty List of StoryEntity, When toStoryRemoteKey is called, Then return empty List of StoryRemoteKey`() {
        // Given
        val entities = emptyList<StoryEntity>()
        val query = "testQuery"
        val prevPage = 1
        val nextPage = 2

        // When
        val remoteKeys = entities.toStoryRemoteKey(query, prevPage, nextPage)

        // Then
        assertTrue(remoteKeys.isEmpty())
    }

    @Test
    fun `Given single StoryEntity List, When toStoryRemoteKey is called, Then return single StoryRemoteKey List`() {
        // Given
        val entities = listOf(testStoryEntity)
        val query = "testQuery"
        val prevPage = 1
        val nextPage = 2

        // When
        val remoteKeys = entities.toStoryRemoteKey(query, prevPage, nextPage)

        // Then
        assertEquals(1, remoteKeys.size)
        assertEquals(testStoryEntity.storyLangId, remoteKeys[0].id)
        assertEquals(query, remoteKeys[0].queryType)
        assertEquals(prevPage, remoteKeys[0].prevPage)
        assertEquals(nextPage, remoteKeys[0].nextPage)
    }

    @Test
    fun `Given multiple StoryEntity List, When toStoryRemoteKey is called, Then return multiple StoryRemoteKey List with same parameters`() {
        // Given
        val entities = storyTestData.take(3).asStoryEntities()
        val query = "testQuery"
        val prevPage = 1
        val nextPage = 2

        // When
        val remoteKeys = entities.toStoryRemoteKey(query, prevPage, nextPage)

        // Then
        assertEquals(entities.size, remoteKeys.size)
        remoteKeys.forEachIndexed { index, remoteKey ->
            assertEquals(entities[index].storyLangId, remoteKey.id)
            assertEquals(query, remoteKey.queryType)
            assertEquals(prevPage, remoteKey.prevPage)
            assertEquals(nextPage, remoteKey.nextPage)
        }
    }

    @Test
    fun `Given StoryEntity List with different storyLangIds, When toStoryRemoteKey is called, Then return StoryRemoteKey List with different ids`() {
        // Given
        val entities = listOf(
            testStoryEntity.copy(storyLangId = 1),
            testStoryEntity.copy(storyLangId = 2),
            testStoryEntity.copy(storyLangId = 3)
        )
        val query = "testQuery"
        val prevPage = 1
        val nextPage = 2

        // When
        val remoteKeys = entities.toStoryRemoteKey(query, prevPage, nextPage)

        // Then
        assertEquals(3, remoteKeys.size)
        assertEquals(1, remoteKeys[0].id)
        assertEquals(2, remoteKeys[1].id)
        assertEquals(3, remoteKeys[2].id)
        // All should have same query and page parameters
        remoteKeys.forEach { remoteKey ->
            assertEquals(query, remoteKey.queryType)
            assertEquals(prevPage, remoteKey.prevPage)
            assertEquals(nextPage, remoteKey.nextPage)
        }
    }

    @Test
    fun `Given StoryEntity List with long query string, When toStoryRemoteKey is called, Then return StoryRemoteKey List with same long query`() {
        // Given
        val entities = listOf(testStoryEntity)
        val longQuery = "a".repeat(1000) // Very long query string
        val prevPage = 1
        val nextPage = 2

        // When
        val remoteKeys = entities.toStoryRemoteKey(longQuery, prevPage, nextPage)

        // Then
        assertEquals(1, remoteKeys.size)
        assertEquals(longQuery, remoteKeys[0].queryType)
        assertEquals(longQuery.length, remoteKeys[0].queryType.length)
    }

    @Test
    fun `Given StoryEntity List with special characters in query, When toStoryRemoteKey is called, Then return StoryRemoteKey List with same special characters`() {
        // Given
        val entities = listOf(testStoryEntity)
        val specialQuery = "한글 검색어 with @#$%^&*() symbols"
        val prevPage = 1
        val nextPage = 2

        // When
        val remoteKeys = entities.toStoryRemoteKey(specialQuery, prevPage, nextPage)

        // Then
        assertEquals(1, remoteKeys.size)
        assertEquals(specialQuery, remoteKeys[0].queryType)
    }

    @Test
    fun `Given StoryEntity List with maximum integer page values, When toStoryRemoteKey is called, Then return StoryRemoteKey List with same maximum values`() {
        // Given
        val entities = listOf(testStoryEntity)
        val query = "testQuery"
        val prevPage = Int.MAX_VALUE
        val nextPage = Int.MAX_VALUE

        // When
        val remoteKeys = entities.toStoryRemoteKey(query, prevPage, nextPage)

        // Then
        assertEquals(1, remoteKeys.size)
        assertEquals(Int.MAX_VALUE, remoteKeys[0].prevPage)
        assertEquals(Int.MAX_VALUE, remoteKeys[0].nextPage)
    }

    @Test
    fun `Given testing story data, When toStoryRemoteKey is called, Then return correct StoryRemoteKey mapping`() {
        // Given
        val entity = storyTestData.first().asStoryEntity()
        val query = "testQuery"
        val prevPage = 1
        val nextPage = 2

        // When
        val remoteKey = entity.toStoryRemoteKey(query, prevPage, nextPage)

        // Then
        assertEquals(entity.storyLangId, remoteKey.id)
        assertEquals(query, remoteKey.queryType)
        assertEquals(prevPage, remoteKey.prevPage)
        assertEquals(nextPage, remoteKey.nextPage)
    }

    @Test
    fun `Given List of testing story entities, When toStoryRemoteKey is called, Then return correct size and mapping`() {
        // Given
        val entities = storyTestData.asStoryEntities()
        val query = "testQuery"
        val prevPage = 1
        val nextPage = 2

        // When
        val remoteKeys = entities.toStoryRemoteKey(query, prevPage, nextPage)

        // Then
        assertEquals(entities.size, remoteKeys.size)
        entities.forEachIndexed { index, entity ->
            assertEquals(entity.storyLangId, remoteKeys[index].id)
            assertEquals(query, remoteKeys[index].queryType)
            assertEquals(prevPage, remoteKeys[index].prevPage)
            assertEquals(nextPage, remoteKeys[index].nextPage)
        }
    }
}