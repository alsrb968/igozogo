package io.jacob.igozogo.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import io.jacob.igozogo.core.data.datasource.local.ThemeDataSource
import io.jacob.igozogo.core.data.datasource.remote.OdiiDataSource
import io.jacob.igozogo.core.data.mapper.toPlace
import io.jacob.igozogo.core.data.mapper.toThemeEntity
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val themeDataSource: ThemeDataSource,
    private val odiiDataSource: OdiiDataSource,
) : PlaceRepository {
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

    override suspend fun getPlaceById(
        placeId: Int,
        placeLangId: Int
    ): Place? {
        return themeDataSource.getThemeById(placeId, placeLangId)?.toPlace()
    }

    override suspend fun getPlacesCount(): Int {
        return themeDataSource.getThemesCount()
    }
}