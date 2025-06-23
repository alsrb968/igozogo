package io.jacob.igozogo.core.data.datasource.local

import androidx.paging.PagingSource
import io.jacob.igozogo.core.data.db.StoryDao
import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import javax.inject.Inject

interface StoryDataSource {
    suspend fun insertStories(stories: List<StoryEntity>)
    fun getStoriesPagingSource(): PagingSource<Int, StoryEntity>
    suspend fun getStories(size: Int): List<StoryEntity>
    fun getStoriesByThemePagingSource(
        themeId: Int, themeLangId: Int
    ): PagingSource<Int, StoryEntity>

    suspend fun getStoriesByTheme(
        themeId: Int, themeLangId: Int
    ): List<StoryEntity>

    fun getStoriesByLocationPagingSource(
        mapX: Double, mapY: Double, radius: Int
    ): PagingSource<Int, StoryEntity>

    suspend fun getStoriesByLocation(
        mapX: Double, mapY: Double, radius: Int,
        size: Int
    ): List<StoryEntity>

    fun getStoriesByKeywordPagingSource(keyword: String): PagingSource<Int, StoryEntity>
    suspend fun getStoriesByKeyword(keyword: String, size: Int): List<StoryEntity>
    suspend fun deleteStories()
}

class StoryDataSourceImpl @Inject constructor(
    private val dao: StoryDao,
) : StoryDataSource {
    override suspend fun insertStories(stories: List<StoryEntity>) {
        return dao.insertStories(stories)
    }

    override fun getStoriesPagingSource(): PagingSource<Int, StoryEntity> {
        return dao.getStoriesPagingSource()
    }

    override suspend fun getStories(size: Int): List<StoryEntity> {
        return dao.getStories(size)
    }

    override fun getStoriesByThemePagingSource(
        themeId: Int, themeLangId: Int
    ): PagingSource<Int, StoryEntity> {
        return dao.getStoriesByThemePagingSource(themeId, themeLangId)
    }

    override suspend fun getStoriesByTheme(
        themeId: Int, themeLangId: Int
    ): List<StoryEntity> {
        return dao.getStoriesByTheme(themeId, themeLangId)
    }

    override fun getStoriesByLocationPagingSource(
        mapX: Double, mapY: Double, radius: Int
    ): PagingSource<Int, StoryEntity> {
        return dao.getStoriesByLocationPagingSource(mapX, mapY, radius / METERS_PER_DEGREE)
    }

    override suspend fun getStoriesByLocation(
        mapX: Double, mapY: Double, radius: Int,
        size: Int
    ): List<StoryEntity> {
        return dao.getStoriesByLocation(mapX, mapY, radius / METERS_PER_DEGREE, size)
    }

    override fun getStoriesByKeywordPagingSource(keyword: String): PagingSource<Int, StoryEntity> {
        return dao.getStoriesByKeywordPagingSource(keyword)
    }

    override suspend fun getStoriesByKeyword(keyword: String, size: Int): List<StoryEntity> {
        return dao.getStoriesByKeyword(keyword, size)
    }

    override suspend fun deleteStories() {
        return dao.deleteStories()
    }

    companion object {
        private const val METERS_PER_DEGREE = 111000.0
    }
}