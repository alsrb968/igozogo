package io.jacob.igozogo.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.jacob.igozogo.core.data.api.OdiiApi
import io.jacob.igozogo.core.data.api.VisitKoreaClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideOdiiApi(): OdiiApi {
        return VisitKoreaClient.retrofit.create(OdiiApi::class.java)
    }
}