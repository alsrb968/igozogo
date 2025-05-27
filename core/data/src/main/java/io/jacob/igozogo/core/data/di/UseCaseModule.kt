package io.jacob.igozogo.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.jacob.igozogo.core.domain.repository.OdiiRepository
import io.jacob.igozogo.core.domain.usecase.GetThemeCategoriesUseCase
import io.jacob.igozogo.core.domain.usecase.SyncThemesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideSyncThemesUseCase(
        repository: OdiiRepository
    ): SyncThemesUseCase {
        return SyncThemesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetThemeCategoriesUseCase(
        repository: OdiiRepository
    ): GetThemeCategoriesUseCase {
        return GetThemeCategoriesUseCase(repository)
    }
}