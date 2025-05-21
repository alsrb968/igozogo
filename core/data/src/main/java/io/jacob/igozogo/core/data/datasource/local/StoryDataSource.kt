package io.jacob.igozogo.core.data.datasource.local

import io.jacob.igozogo.core.data.db.StoryDao
import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import javax.inject.Inject

interface StoryDataSource {
    suspend fun getStories(): List<StoryEntity>
    suspend fun getStoriesByThemeId(themeId: Int): List<StoryEntity>
    suspend fun deleteStories()
}

class StoryDataSourceImpl @Inject constructor(
    private val dao: StoryDao
) : StoryDataSource {
    override suspend fun getStories(): List<StoryEntity> {
        return dao.getStories()
    }

    override suspend fun getStoriesByThemeId(themeId: Int): List<StoryEntity> {
        return dao.getStoriesByThemeId(themeId)
    }

    override suspend fun deleteStories() {
        return dao.deleteStories()
    }
}