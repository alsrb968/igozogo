package io.jacob.igozogo.core.data.model.local.odii

import androidx.room.Entity

@Entity(
    tableName = "story_remote_key_table",
    primaryKeys = ["id", "queryType"],
)
data class StoryRemoteKey(
    val id: Int,
    val queryType: String,
    val nextPage: Int?
)
