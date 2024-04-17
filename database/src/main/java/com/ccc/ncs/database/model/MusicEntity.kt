package com.ccc.ncs.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity("music")
data class MusicEntity(
    @PrimaryKey
    val id: UUID,
    val title: String,
    val artist: String,
    val releaseDate: String,
    val dataUrl: String,
    val coverThumbnailUrl: String,
    val coverUrl: String,
    val detailUrl: String,
    val artistDetailUrl: String
)