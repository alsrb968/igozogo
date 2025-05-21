package io.jacob.igozogo.core.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.jacob.igozogo.core.data.model.local.odii.StoryEntity
import io.jacob.igozogo.core.data.model.local.odii.StoryRemoteKey
import io.jacob.igozogo.core.data.model.local.odii.ThemeEntity

@Database(
    entities = [
        ThemeEntity::class,
        StoryEntity::class,
        StoryRemoteKey::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class VisitKoreaDatabase :RoomDatabase() {
    abstract fun themeDao(): ThemeDao
    abstract fun storyDao(): StoryDao
    abstract fun storyRemoteKeyDao(): StoryRemoteKeyDao

    companion object {
        private const val DB_NAME = "VisitKorea.db"

        fun getInstance(context: Context): VisitKoreaDatabase =
            Room.databaseBuilder(context, VisitKoreaDatabase::class.java, DB_NAME)
//                .fallbackToDestructiveMigration(true)
//                .allowMainThreadQueries()
                .build()
    }
}