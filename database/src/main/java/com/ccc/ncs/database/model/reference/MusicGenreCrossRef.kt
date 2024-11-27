package com.ccc.ncs.database.model.reference

import androidx.room.Entity
import androidx.room.Index
import java.util.UUID


@Entity(
    tableName = "music_genre_cross_ref",
    primaryKeys = [ "musicId", "genreId" ],
    indices = [
        Index("musicId"),
        Index("genreId")
    ]
)
data class MusicGenreCrossRef(
    val musicId: UUID,
    val genreId: Int
)