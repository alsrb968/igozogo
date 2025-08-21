package io.jacob.igozogo.core.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.jacob.igozogo.core.data.db.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): VisitKoreaDatabase {
        return Room.databaseBuilder(
            context,
            VisitKoreaDatabase::class.java,
            "VisitKorea.db"
        ).build()
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

    @Provides
    @Singleton
    fun provideRecentSearchDao(
        database: VisitKoreaDatabase
    ): RecentSearchDao {
        return database.recentSearchDao()
    }
}