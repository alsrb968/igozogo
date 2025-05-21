package io.jacob.igozogo.core.data.repository

import androidx.paging.*
import io.jacob.igozogo.core.data.datasource.local.StoryDataSource
import io.jacob.igozogo.core.data.datasource.local.ThemeDataSource
import io.jacob.igozogo.core.data.datasource.remote.OdiiDataSource
import io.jacob.igozogo.core.data.mapper.toStory
import io.jacob.igozogo.core.data.mapper.toTheme
import io.jacob.igozogo.core.data.mapper.toThemeEntity
import io.jacob.igozogo.core.data.mediator.Query
import io.jacob.igozogo.core.data.mediator.StoryRemoteMediator
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.model.Theme
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
    override suspend fun syncThemes() {
        val themes = odiiDataSource.getThemeBasedList(
            numOfRows = 100,
            pageNo = 1,
        )
        themeDataSource.insertThemes(themes.toThemeEntity())
    }

    override fun getThemes(): Flow<List<Theme>> {
        return themeDataSource.getThemes().map { it.toTheme() }
    }

    override fun getThemeCategories(): Flow<List<String>> {
        return themeDataSource.getThemeCategories()
    }

    override fun getThemesByCategory(category: String): Flow<List<Theme>> {
        return themeDataSource.getThemesByCategory(category).map { it.toTheme() }
    }

    override fun getThemesByKeyword(keyword: String): Flow<List<Theme>> {
        return themeDataSource.getThemesByKeyword(keyword).map { it.toTheme() }
    }

    override fun getStoriesByTheme(
        themeId: Int,
        themeLangId: Int,
        pageSize: Int
    ): Flow<PagingData<Story>> {
        val query = Query.Theme(themeId, themeLangId)

        return Pager(
            config = PagingConfig(pageSize = pageSize),
            remoteMediator = storyRemoteMediatorFactory.create(query),
            pagingSourceFactory = {
                storyDataSource.getStoriesByTheme(themeId, themeLangId)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toStory() }
        }
    }

//    override fun getStoriesByLocation(
//        mapX: Double,
//        mapY: Double,
//        radius: Int,
//    ): Flow<List<Story>> {
//        return flow {
//            odiiDataSource.getStoryLocationBasedList(
//                mapX = mapX,
//                mapY = mapY,
//                radius = radius,
//            ).map { it.toStory() }
//        }
//    }

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