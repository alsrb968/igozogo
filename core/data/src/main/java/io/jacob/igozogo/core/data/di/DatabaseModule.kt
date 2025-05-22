package io.jacob.igozogo.core.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.jacob.igozogo.core.data.db.StoryDao
import io.jacob.igozogo.core.data.db.StoryRemoteKeyDao
import io.jacob.igozogo.core.data.db.ThemeDao
import io.jacob.igozogo.core.data.db.VisitKoreaDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): VisitKoreaDatabase {
        return VisitKoreaDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideThemeDao(
        database: VisitKoreaDatabase
    ): ThemeDao {
        return database.themeDao()
    }

    @Provides
    @Singleton
    fun provideStoryDao(
        database: VisitKoreaDatabase
    ): StoryDao {
        return database.storyDao()
    }

    @Provides
    @Singleton
    fun provideStoryRemoteKeyDao(
        database: VisitKoreaDatabase
    ): StoryRemoteKeyDao {
        return database.storyRemoteKeyDao()
    }
}