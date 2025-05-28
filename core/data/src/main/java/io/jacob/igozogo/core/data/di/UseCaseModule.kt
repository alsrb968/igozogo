package io.jacob.igozogo.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.jacob.igozogo.core.domain.repository.OdiiRepository
import io.jacob.igozogo.core.domain.usecase.GetPlaceCategoriesUseCase
import io.jacob.igozogo.core.domain.usecase.SyncPlacesUseCase
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
}