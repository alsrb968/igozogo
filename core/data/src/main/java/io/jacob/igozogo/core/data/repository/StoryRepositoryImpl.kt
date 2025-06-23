package io.jacob.igozogo.core.data.repository

import androidx.paging.*
import io.jacob.igozogo.core.data.datasource.local.StoryDataSource
import io.jacob.igozogo.core.data.mapper.toStory
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
) : StoryRepository {
    override fun getStoriesByPlace(
        place: Place,
        pageSize: Int
    ): Flow<PagingData<Story>> {
        val query = Query.Place(place.placeId, place.placeLangId)

        return Pager(
            config = PagingConfig(pageSize = pageSize),
            remoteMediator = storyRemoteMediatorFactory.create(query),
            pagingSourceFactory = {
                storyDataSource.getStoriesByTheme(place.placeId, place.placeLangId)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toStory() }
        }
    }

    override fun getStoriesByLocation(
        mapX: Double,
        mapY: Double,
        radius: Int,
        pageSize: Int
    ): Flow<PagingData<Story>> {
        val query = Query.Location(mapX, mapY, radius)

        return Pager(
            config = PagingConfig(pageSize = pageSize),
            remoteMediator = storyRemoteMediatorFactory.create(query),
            pagingSourceFactory = {
                storyDataSource.getStoriesByLocation(mapX, mapY, radius)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toStory() }
        }
    }

    override fun getStoriesByKeyword(
        keyword: String,
        pageSize: Int
    ): Flow<PagingData<Story>> {
        val query = Query.Keyword(keyword)

        return Pager(
            config = PagingConfig(pageSize = pageSize),
            remoteMediator = storyRemoteMediatorFactory.create(query),
            pagingSourceFactory = {
                storyDataSource.getStoriesByKeyword(keyword)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toStory() }
        }
    }
}