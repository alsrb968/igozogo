package io.jacob.igozogo.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.jacob.igozogo.core.domain.repository.OdiiRepository
import io.jacob.igozogo.core.domain.usecase.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideSyncPlacesUseCase(
        repository: OdiiRepository
    ): SyncPlacesUseCase {
        return SyncPlacesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetPlaceCategoriesUseCase(
        repository: OdiiRepository
    ): GetPlaceCategoriesUseCase {
        return GetPlaceCategoriesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetPlacesUseCase(
        repository: OdiiRepository
    ): GetPlacesUseCase {
        return GetPlacesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetPlaceByIdUseCase(
        repository: OdiiRepository
    ): GetPlaceByIdUseCase {
        return GetPlaceByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetStoriesByPlaceUseCase(
        repository: OdiiRepository
    ): GetStoriesByPlaceUseCase {
        return GetStoriesByPlaceUseCase(repository)
    }
}