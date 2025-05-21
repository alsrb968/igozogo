package io.jacob.igozogo.core.data.datasource.local

import androidx.paging.PagingSource
import io.jacob.igozogo.core.data.db.StoryDao
import io.jacob.igozogo.core.data.db.StoryRemoteKeyDao
import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import io.jacob.igozogo.core.data.model.local.odii.StoryRemoteKey
import javax.inject.Inject

interface StoryDataSource {
    suspend fun insertStories(stories: List<StoryEntity>)
    fun getStories(): PagingSource<Int, StoryEntity>
    fun getStoriesByTheme(themeId: Int, themeLangId: Int): PagingSource<Int, StoryEntity>
    fun getStoriesByKeyword(keyword: String): PagingSource<Int, StoryEntity>
    suspend fun deleteStories()

    suspend fun insertRemoteKeys(keys: List<StoryRemoteKey>)
    suspend fun getRemoteKey(id: Int, queryType: String): StoryRemoteKey?
    suspend fun deleteRemoteKeysByQueryType(queryType: String)
    suspend fun deleteRemoteKeys()
}

class StoryDataSourceImpl @Inject constructor(
    private val storyDao: StoryDao,
    private val remoteKeyDao: StoryRemoteKeyDao,
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

    override fun getStoriesByKeyword(keyword: String): PagingSource<Int, StoryEntity> {
        return storyDao.getStoriesByKeyword(keyword)
    }

    override suspend fun deleteStories() {
        return storyDao.deleteStories()
    }

    override suspend fun insertRemoteKeys(keys: List<StoryRemoteKey>) {
        return remoteKeyDao.insertRemoteKeys(keys)
    }

    override suspend fun getRemoteKey(
        id: Int,
        queryType: String
    ): StoryRemoteKey? {
        return remoteKeyDao.getRemoteKey(id, queryType)
    }

    override suspend fun deleteRemoteKeysByQueryType(queryType: String) {
        return remoteKeyDao.deleteRemoteKeysByQueryType(queryType)
    }

    override suspend fun deleteRemoteKeys() {
        return remoteKeyDao.deleteRemoteKeys()
    }
}