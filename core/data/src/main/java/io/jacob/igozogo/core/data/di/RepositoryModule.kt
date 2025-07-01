package io.jacob.igozogo.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.jacob.igozogo.core.data.datasource.local.StoryDataSource
import io.jacob.igozogo.core.data.datasource.local.ThemeDataSource
import io.jacob.igozogo.core.data.datasource.player.PlayerDataSource
import io.jacob.igozogo.core.data.datasource.remote.OdiiDataSource
import io.jacob.igozogo.core.data.repository.PlaceRepositoryImpl
import io.jacob.igozogo.core.data.repository.PlayerRepositoryImpl
import io.jacob.igozogo.core.data.repository.StoryRemoteMediator
import io.jacob.igozogo.core.data.repository.StoryRepositoryImpl
import io.jacob.igozogo.core.domain.repository.PlaceRepository
import io.jacob.igozogo.core.domain.repository.PlayerRepository
import io.jacob.igozogo.core.domain.repository.StoryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providePlaceRepository(
        themeDataSource: ThemeDataSource,
        odiiDataSource: OdiiDataSource,
    ): PlaceRepository {
        return PlaceRepositoryImpl(
            themeDataSource = themeDataSource,
            odiiDataSource = odiiDataSource,
        )
    }

    @Provides
    @Singleton
    fun provideStoryRepository(
        storyDataSource: StoryDataSource,
        storyRemoteMediatorFactory: StoryRemoteMediator.Factory,
        odiiDataSource: OdiiDataSource,
    ): StoryRepository {
        return StoryRepositoryImpl(
            storyDataSource = storyDataSource,
            storyRemoteMediatorFactory = storyRemoteMediatorFactory,
            odiiDataSource = odiiDataSource,
        )
    }

    @Provides
    @Singleton
    fun providePlayerRepository(
        playerDataSource: PlayerDataSource,
    ): PlayerRepository {
        return PlayerRepositoryImpl(
            playerDataSource = playerDataSource,
        )
    }
}