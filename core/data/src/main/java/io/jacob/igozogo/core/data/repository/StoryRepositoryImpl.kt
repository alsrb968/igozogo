package io.jacob.igozogo.core.data.repository

import androidx.paging.*
import io.jacob.igozogo.core.data.datasource.local.StoryDataSource
import io.jacob.igozogo.core.data.datasource.remote.OdiiDataSource
import io.jacob.igozogo.core.data.mapper.toStory
import io.jacob.igozogo.core.data.mapper.toStoryEntity
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class StoryRepositoryImpl @Inject constructor(
    private val storyDataSource: StoryDataSource,
    private val storyRemoteMediatorFactory: StoryRemoteMediator.Factory,
    private val odiiDataSource: OdiiDataSource,
) : StoryRepository {
    override suspend fun getStories(size: Int): List<Story> {
        return storyDataSource.getStories(size).toStory()
    }

    override fun getStoriesByPlacePaging(
        place: Place,
        pageSize: Int
    ): Flow<PagingData<Story>> {
        val query = Query.Place(place.placeId, place.placeLangId)

        return Pager(
            config = PagingConfig(pageSize = pageSize),
            remoteMediator = storyRemoteMediatorFactory.create(query),
            pagingSourceFactory = {
                storyDataSource.getStoriesByThemePagingSource(place.placeId, place.placeLangId)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toStory() }
        }
    }

    override suspend fun getStoriesByPlace(place: Place): List<Story> {
        val cached = storyDataSource.getStoriesByTheme(place.placeId, place.placeLangId)

        if (cached.isNotEmpty()) return cached.toStory()

        odiiDataSource.getStoryBasedList(
            numOfRows = 100,
            pageNo = 1,
            themeId = place.placeId,
            themeLangId = place.placeLangId
        ).fold(
            onSuccess = { responses ->
                val entities = responses.toStoryEntity()
                storyDataSource.insertStories(entities)
                return entities.toStory()
            },
            onFailure = { throw it }
        )
    }

    override fun getStoriesByLocationPaging(
        mapX: Double, mapY: Double, radius: Int,
        pageSize: Int
    ): Flow<PagingData<Story>> {
        val query = Query.Location(mapX, mapY, radius)

        return Pager(
            config = PagingConfig(pageSize = pageSize),
            remoteMediator = storyRemoteMediatorFactory.create(query),
            pagingSourceFactory = {
                storyDataSource.getStoriesByLocationPagingSource(mapX, mapY, radius)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toStory() }
        }
    }

    override suspend fun getStoriesByLocation(
        mapX: Double, mapY: Double, radius: Int,
        size: Int
    ): List<Story> {
        val cached = storyDataSource.getStoriesByLocation(mapX, mapY, radius, size)

        if (cached.isNotEmpty()) return cached.toStory()

        odiiDataSource.getStoryLocationBasedList(
            numOfRows = 100,
            pageNo = 1,
            mapX = mapX,
            mapY = mapY,
            radius = radius
        ).fold(
            onSuccess = { responses ->
                val entities = responses.toStoryEntity()
                storyDataSource.insertStories(entities)
                return entities.toStory()
            },
            onFailure = { throw it }
        )
    }

    override fun getStoriesByKeywordPaging(
        keyword: String,
        pageSize: Int
    ): Flow<PagingData<Story>> {
        val query = Query.Keyword(keyword)

        return Pager(
            config = PagingConfig(pageSize = pageSize),
            remoteMediator = storyRemoteMediatorFactory.create(query),
            pagingSourceFactory = {
                storyDataSource.getStoriesByKeywordPagingSource(keyword)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toStory() }
        }
    }

    override suspend fun getStoriesByKeyword(
        keyword: String,
        size: Int
    ): List<Story> {
        val cached = storyDataSource.getStoriesByKeyword(keyword, size)

        if (cached.isNotEmpty()) return cached.toStory()

        odiiDataSource.getStorySearchList(
            numOfRows = 100,
            pageNo = 1,
            keyword = keyword
        ).fold(
            onSuccess = { responses ->
                val entities = responses.toStoryEntity()
                storyDataSource.insertStories(entities)
                return entities.toStory()
            },
            onFailure = { throw it }
        )
    }
}