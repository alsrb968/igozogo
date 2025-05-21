package io.jacob.igozogo.core.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.jacob.igozogo.core.data.api.OdiiApi
import io.jacob.igozogo.core.data.db.VisitKoreaDatabase
import io.jacob.igozogo.core.data.mapper.toStoryEntity
import io.jacob.igozogo.core.data.mapper.toStoryRemoteKey
import io.jacob.igozogo.core.data.model.local.odii.StoryEntity

sealed interface QueryType {
    data class Theme(val themeId: Int, val themeLangId: Int) : QueryType {
        override fun toString() = "theme:$themeId:$themeLangId"
    }

    data class Location(val mapX: Double, val mapY: Double, val radius: Int) : QueryType {
        override fun toString() = "location:$mapX:$mapY:$radius"
    }

    data class Search(val keyword: String) : QueryType {
        override fun toString() = "search:$keyword"
    }
}

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator @AssistedInject constructor(
    private val api: OdiiApi,
    private val db: VisitKoreaDatabase,
    @Assisted private val query: QueryType,
) : RemoteMediator<Int, StoryEntity>() {

    private val storyDao = db.storyDao()
    private val remoteKeyDao = db.storyRemoteKeyDao()

    @AssistedFactory
    interface Factory {
        fun create(query: QueryType): StoryRemoteMediator
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1

            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                val remoteKey = remoteKeyDao.getRemoteKey(
                    id = lastItem.storyLangId,
                    queryType = query.toString(),
                )
                remoteKey?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        try {
            val size = state.config.pageSize

            val responses = when (query) {
                is QueryType.Theme ->
                    api.getStoryBasedList(
                        numOfRows = size,
                        pageNo = page,
                        themeId = query.themeId,
                        themeLangId = query.themeLangId,
                    )

                is QueryType.Location ->
                    api.getStoryLocationBasedList(
                        numOfRows = size,
                        pageNo = page,
                        mapX = query.mapX,
                        mapY = query.mapY,
                        radius = query.radius,
                    )

                is QueryType.Search ->
                    api.getStorySearchList(
                        numOfRows = size,
                        pageNo = page,
                        keyword = query.keyword,
                    )
            }.response.body.items.item
            val endOfPaginationReached = responses.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.deleteRemoteKeysByQueryType(query.toString())
                }

                val entities = responses.toStoryEntity()
                val keys = entities.toStoryRemoteKey(query = query.toString(), nextPage = page + 1)

                storyDao.insertStories(entities)
                remoteKeyDao.insertRemoteKeys(keys)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}