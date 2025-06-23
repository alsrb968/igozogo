package io.jacob.igozogo.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.jacob.igozogo.core.domain.repository.PlaceRepository
import io.jacob.igozogo.core.domain.repository.StoryRepository
import io.jacob.igozogo.core.domain.usecase.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideSyncPlacesUseCase(
        repository: PlaceRepository
    ): SyncPlacesUseCase {
        return SyncPlacesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetPlaceCategoriesUseCase(
        repository: PlaceRepository
    ): GetPlaceCategoriesUseCase {
        return GetPlaceCategoriesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetPlacesUseCase(
        repository: PlaceRepository
    ): GetPlacesUseCase {
        return GetPlacesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetPlaceByIdUseCase(
        repository: PlaceRepository
    ): GetPlaceByIdUseCase {
        return GetPlaceByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetStoriesByPlaceUseCase(
        repository: StoryRepository
    ): GetStoriesByPlaceUseCase {
        return GetStoriesByPlaceUseCase(repository)
    }
}