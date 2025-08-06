package io.jacob.igozogo.core.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class RoomDatabaseRule : TestWatcher() {
    lateinit var db: VisitKoreaDatabase
        private set

    override fun starting(description: Description) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, VisitKoreaDatabase::class.java)
            .build()
    }

    override fun finished(description: Description) {
        db.close()
    }
}