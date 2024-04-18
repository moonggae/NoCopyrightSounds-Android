package com.ccc.ncs.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

fun MusicEntity.asModel(
    genres: Set<Genre>,
    moods: Set<Mood>
) = Music(
    id = id,
    title = title,
    artist = artist,
    releaseDate = LocalDate.parse(releaseDate),
    dataUrl = dataUrl,
    coverThumbnailUrl = coverThumbnailUrl,
    coverUrl = coverUrl,
    detailUrl = detailUrl,
    artistDetailUrl = artistDetailUrl,
    genres = genres,
    moods = moods,
    versions = setOf()
)

fun Music.asEntity() = MusicEntity(
    id = id,
    title = title,
    artist = artist,
    releaseDate = releaseDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
    dataUrl = dataUrl,
    coverThumbnailUrl = coverThumbnailUrl,
    coverUrl = coverUrl,
    detailUrl = detailUrl,
    artistDetailUrl = artistDetailUrl,
)