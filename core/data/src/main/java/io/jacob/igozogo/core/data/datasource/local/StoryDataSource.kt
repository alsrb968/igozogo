package io.jacob.igozogo.core.data.datasource.local

import androidx.paging.PagingSource
import io.jacob.igozogo.core.data.db.StoryDao
import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import javax.inject.Inject

interface StoryDataSource {
    suspend fun insertStories(stories: List<StoryEntity>)
    fun getStories(): PagingSource<Int, StoryEntity>
    fun getStoriesByTheme(themeId: Int, themeLangId: Int): PagingSource<Int, StoryEntity>
    fun searchStories(keyword: String): PagingSource<Int, StoryEntity>
    suspend fun deleteStories()
}

class StoryDataSourceImpl @Inject constructor(
    private val storyDao: StoryDao
) : StoryDataSource {
    override suspend fun insertStories(stories: List<StoryEntity>) {
        return storyDao.insertStories(stories)
    }

    override fun getStories(): PagingSource<Int, StoryEntity> {
        return storyDao.getStories()
    }

    override fun getStoriesByTheme(themeId: Int, themeLangId: Int): PagingSource<Int, StoryEntity> {
        return storyDao.getStoriesByTheme(themeId, themeLangId)
    }

    override fun searchStories(keyword: String): PagingSource<Int, StoryEntity> {
        return storyDao.searchStories(keyword)
    }

    override suspend fun deleteStories() {
        return storyDao.deleteStories()
    }
}