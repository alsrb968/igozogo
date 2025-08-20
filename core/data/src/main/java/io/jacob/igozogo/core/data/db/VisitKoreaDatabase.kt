package io.jacob.igozogo.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.jacob.igozogo.core.data.db.converter.Converters
import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import io.jacob.igozogo.core.data.model.local.odii.StoryRemoteKey
import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity
import io.jacob.igozogo.core.data.model.local.search.RecentSearchEntity

@Database(
    entities = [
        ThemeEntity::class,
        StoryEntity::class,
        StoryRemoteKey::class,
        RecentSearchEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class VisitKoreaDatabase : RoomDatabase() {
    abstract fun themeDao(): ThemeDao
    abstract fun storyDao(): StoryDao
    abstract fun storyRemoteKeyDao(): StoryRemoteKeyDao
    abstract fun recentSearchDao(): RecentSearchDao
}