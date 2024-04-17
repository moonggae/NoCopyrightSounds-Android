package com.ccc.ncs.database.model.reference

import androidx.room.Entity
import java.util.UUID


@Entity(
    tableName = "music_mood_cross_ref",
    primaryKeys = ["musicId", "moodId"]
)
data class MusicMoodCrossRef(
    val musicId: UUID,
    val moodId: Int
)