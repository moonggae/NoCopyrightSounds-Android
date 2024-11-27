package com.ccc.ncs.database.model.reference

import androidx.room.Entity
import androidx.room.Index
import java.util.UUID


@Entity(
    tableName = "music_mood_cross_ref",
    primaryKeys = ["musicId", "moodId"],
    indices = [
        Index("musicId"),
        Index("moodId")
    ]
)
data class MusicMoodCrossRef(
    val musicId: UUID,
    val moodId: Int
)