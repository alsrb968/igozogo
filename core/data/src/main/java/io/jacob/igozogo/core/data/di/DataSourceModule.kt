package io.jacob.igozogo.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.jacob.igozogo.core.data.api.OdiiApi
import io.jacob.igozogo.core.data.datasource.OdiiDataSource
import io.jacob.igozogo.core.data.datasource.OdiiDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideOdiiDataSource(
        api: OdiiApi,
    ): OdiiDataSource {
        return OdiiDataSourceImpl(
            api = api
        )
    }
}