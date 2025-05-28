package io.jacob.igozogo.core.data.repository

import androidx.paging.*
import io.jacob.igozogo.core.data.datasource.local.StoryDataSource
import io.jacob.igozogo.core.data.datasource.local.ThemeDataSource
import io.jacob.igozogo.core.data.datasource.remote.OdiiDataSource
import io.jacob.igozogo.core.data.mapper.toPlace
import io.jacob.igozogo.core.data.mapper.toStory
import io.jacob.igozogo.core.data.mapper.toThemeEntity
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.repository.OdiiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class OdiiRepositoryImpl @Inject constructor(
    private val themeDataSource: ThemeDataSource,
    private val storyDataSource: StoryDataSource,
    private val odiiDataSource: OdiiDataSource,
    private val storyRemoteMediatorFactory: StoryRemoteMediator.Factory,
) : OdiiRepository {
    override suspend fun syncPlaces(size: Int) {
        odiiDataSource.getThemeBasedList(
            numOfRows = size,
            pageNo = 1,
        ).onSuccess { themes ->
            themeDataSource.insertThemes(themes.toThemeEntity())
        }.onFailure { e -> throw e }
    }

    override fun getPlaces(pageSize: Int): Flow<PagingData<Place>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = {
                themeDataSource.getThemes()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toPlace() }
        }
    }

    override fun getPlaceCategories(pageSize: Int): Flow<PagingData<String>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = {
                themeDataSource.getThemeCategories()
            }
        ).flow
    }

    override fun getPlacesByCategory(
        category: String,
        pageSize: Int,
    ): Flow<PagingData<Place>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = {
                themeDataSource.getThemesByCategory(category)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toPlace() }
        }
    }

    override fun getPlacesByLocation(
        mapX: Double,
        mapY: Double,
        radius: Int,
        pageSize: Int,
    ): Flow<PagingData<Place>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = {
                themeDataSource.getThemesByLocation(mapX, mapY, radius)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toPlace() }
        }
    }

    override fun getPlacesByKeyword(
        keyword: String,
        pageSize: Int,
    ): Flow<PagingData<Place>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = {
                themeDataSource.getThemesByKeyword(keyword)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toPlace() }
        }
    }

    override suspend fun getPlacesCount(): Int {
        return themeDataSource.getThemesCount()
    }

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