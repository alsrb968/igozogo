package io.jacob.igozogo.core.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.jacob.igozogo.core.data.datasource.local.StoryDataSource
import io.jacob.igozogo.core.data.datasource.remote.OdiiDataSource
import io.jacob.igozogo.core.data.db.VisitKoreaDatabase
import io.jacob.igozogo.core.data.mapper.toStoryEntity
import io.jacob.igozogo.core.data.mapper.toStoryRemoteKey
import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import io.jacob.igozogo.core.data.model.local.odii.StoryRemoteKey
import io.jacob.igozogo.core.data.model.remote.odii.StoryResponse

sealed interface Query {
    data class Theme(val themeId: Int, val themeLangId: Int) : Query {
        override fun toString() = "theme:$themeId:$themeLangId"
    }

    data class Location(val mapX: Double, val mapY: Double, val radius: Int) : Query {
        override fun toString() = "location:$mapX:$mapY:$radius"
    }

    data class Keyword(val keyword: String) : Query {
        override fun toString() = "keyword:$keyword"
    }
}

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator @AssistedInject constructor(
    private val db: VisitKoreaDatabase,
    private val local: StoryDataSource,
    private val remote: OdiiDataSource,
    @Assisted("query") private val query: Query,
) : RemoteMediator<Int, StoryEntity>() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("query") query: Query): StoryRemoteMediator
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextPage?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                remoteKey?.prevPage ?: return MediatorResult.Success(endOfPaginationReached = true)
            }

            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                remoteKey?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        try {
            val size = state.config.pageSize

            val responses = getStoryResponses(size, page)
            val endOfPaginationReached = responses.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    local.deleteRemoteKeysByQueryType(query.toString())
                }

                val entities = responses.toStoryEntity()
                val keys = entities.toStoryRemoteKey(
                    query = query.toString(),
                    prevPage = if (page == 1) null else page - 1,
                    nextPage = if (endOfPaginationReached) null else page + 1,
                )

                local.insertStories(entities)
                local.insertRemoteKeys(keys)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, StoryEntity>
    ): StoryRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.storyLangId?.let { id ->
                local.getRemoteKey(id, query.toString())
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, StoryEntity>
    ): StoryRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { story ->
            local.getRemoteKey(story.storyLangId, query.toString())
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, StoryEntity>
    ): StoryRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { story ->
            local.getRemoteKey(story.storyLangId, query.toString())
        }
    }

    private suspend fun getStoryResponses(
        size: Int, page: Int,
    ): List<StoryResponse> {
        return when (query) {
            is Query.Theme ->
                remote.getStoryBasedList(
                    numOfRows = size,
                    pageNo = page,
                    themeId = query.themeId,
                    themeLangId = query.themeLangId,
                )

            is Query.Location ->
                remote.getStoryLocationBasedList(
                    numOfRows = size,
                    pageNo = page,
                    mapX = query.mapX,
                    mapY = query.mapY,
                    radius = query.radius,
                )

            is Query.Keyword ->
                remote.getStorySearchList(
                    numOfRows = size,
                    pageNo = page,
                    keyword = query.keyword,
                )
        }
    }
}