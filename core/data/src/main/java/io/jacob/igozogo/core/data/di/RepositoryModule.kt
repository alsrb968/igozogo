package io.jacob.igozogo.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.jacob.igozogo.core.data.datasource.local.StoryDataSource
import io.jacob.igozogo.core.data.datasource.local.ThemeDataSource
import io.jacob.igozogo.core.data.datasource.remote.OdiiDataSource
import io.jacob.igozogo.core.data.mediator.StoryRemoteMediator
import io.jacob.igozogo.core.data.repository.OdiiRepositoryImpl
import io.jacob.igozogo.core.domain.repository.OdiiRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideOdiiRepository(
        themeDataSource: ThemeDataSource,
        storyDataSource: StoryDataSource,
        odiiDataSource: OdiiDataSource,
        storyRemoteMediatorFactory: StoryRemoteMediator.Factory,
    ): OdiiRepository {
        return OdiiRepositoryImpl(
            themeDataSource = themeDataSource,
            storyDataSource = storyDataSource,
            odiiDataSource = odiiDataSource,
            storyRemoteMediatorFactory = storyRemoteMediatorFactory,
        )
    }
}