package io.jacob.igozogo.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.jacob.igozogo.core.data.api.OdiiApi
import io.jacob.igozogo.core.data.datasource.local.*
import io.jacob.igozogo.core.data.datasource.remote.OdiiDataSource
import io.jacob.igozogo.core.data.datasource.remote.OdiiDataSourceImpl
import io.jacob.igozogo.core.data.db.StoryDao
import io.jacob.igozogo.core.data.db.StoryRemoteKeyDao
import io.jacob.igozogo.core.data.db.ThemeDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideThemeDataSource(
        dao: ThemeDao
    ): ThemeDataSource {
        return ThemeDataSourceImpl(
            dao = dao
        )
    }

    @Provides
    @Singleton
    fun provideStoryDataSource(
        dao: StoryDao,
    ): StoryDataSource {
        return StoryDataSourceImpl(
            dao = dao
        )
    }

    @Provides
    @Singleton
    fun provideStoryRemoteKeyDataSource(
        dao: StoryRemoteKeyDao
    ): StoryRemoteKeyDataSource {
        return StoryRemoteKeyDataSourceImpl(
            dao = dao
        )
    }

    @Provides
    @Singleton
    fun provideOdiiDataSource(
        api: OdiiApi
    ): OdiiDataSource {
        return OdiiDataSourceImpl(
            api = api
        )
    }
}