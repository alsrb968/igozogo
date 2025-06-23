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

    override fun getPlacesPaging(pageSize: Int): Flow<PagingData<Place>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = {
                themeDataSource.getThemesPagingSource()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toPlace() }
        }
    }

    override suspend fun getPlaces(size: Int): List<Place> {
        return themeDataSource.getThemes(size).toPlace()
    }

    override fun getPlaceCategoriesPaging(pageSize: Int): Flow<PagingData<String>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = {
                themeDataSource.getThemeCategoriesPagingSource()
            }
        ).flow
    }

    override suspend fun getPlaceCategories(): List<String> {
        return themeDataSource.getThemeCategories()
    }

    override fun getPlacesByCategoryPaging(
        category: String,
        pageSize: Int,
    ): Flow<PagingData<Place>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = {
                themeDataSource.getThemesByCategoryPagingSource(category)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toPlace() }
        }
    }

    override suspend fun getPlacesByCategory(
        category: String,
        size: Int
    ): List<Place> {
        return themeDataSource.getThemesByCategory(category, size).toPlace()
    }

    override fun getPlacesByLocationPaging(
        mapX: Double, mapY: Double, radius: Int,
        pageSize: Int,
    ): Flow<PagingData<Place>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = {
                themeDataSource.getThemesByLocationPagingSource(mapX, mapY, radius)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toPlace() }
        }
    }

    override suspend fun getPlacesByLocation(
        mapX: Double, mapY: Double, radius: Int,
        size: Int
    ): List<Place> {
        return themeDataSource.getThemesByLocation(mapX, mapY, radius, size).toPlace()
    }

    override fun getPlacesByKeywordPaging(
        keyword: String,
        pageSize: Int,
    ): Flow<PagingData<Place>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = {
                themeDataSource.getThemesByKeywordPagingSource(keyword)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toPlace() }
        }
    }

    override suspend fun getPlacesByKeyword(
        keyword: String,
        size: Int
    ): List<Place> {
        return themeDataSource.getThemesByKeyword(keyword, size).toPlace()
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