package io.jacob.igozogo.core.data.db.converter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class Converters {
    @TypeConverter
    fun fromInstant(instant: Instant?): Long? {
        return instant?.epochSeconds
    }

    @TypeConverter
    fun toInstant(epochSeconds: Long?): Instant? {
        return epochSeconds?.let { Instant.fromEpochSeconds(it) }
    }
}