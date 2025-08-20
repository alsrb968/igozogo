package io.jacob.igozogo.core.data.model.local.search

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(tableName = "recent_search_table")
data class RecentSearchEntity(
    @PrimaryKey val query: String,
    val queriedDate: Instant,
)
