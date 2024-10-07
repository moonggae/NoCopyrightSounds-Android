package com.ccc.ncs.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.MusicStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@Entity("music")
data class MusicEntity(
    @PrimaryKey
    val id: UUID,
    val title: String,
    val artists: List<Artist>,
    val releaseDate: String,
    val dataUrl: String,
    val coverThumbnailUrl: String,
    val coverUrl: String,
    val detailUrl: String,
    val status: String,
    val localUri: String? = null
)

fun MusicEntity.asModel(
    genres: Set<Genre>,
    moods: Set<Mood>
) = Music(
    id = id,
    title = title,
    artists = artists,
    releaseDate = LocalDate.parse(releaseDate),
    dataUrl = dataUrl,
    coverThumbnailUrl = coverThumbnailUrl,
    coverUrl = coverUrl,
    detailUrl = detailUrl,
    genres = genres,
    moods = moods,
    versions = setOf(),
    status = status.toMusicStatus(localUri)
)

fun String.toMusicStatus(localUri: String?): MusicStatus {
    return when (this) {
        "Downloading" -> MusicStatus.Downloading
        "Downloaded" -> if (localUri != null) MusicStatus.Downloaded(localUri) else MusicStatus.None
        "PartiallyCached" -> MusicStatus.PartiallyCached
        "FullyCached" -> MusicStatus.FullyCached
        else -> MusicStatus.None
    }
}

fun Music.asEntity() = MusicEntity(
    id = id,
    title = title,
    artists = artists,
    releaseDate = releaseDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
    dataUrl = dataUrl,
    coverThumbnailUrl = coverThumbnailUrl,
    coverUrl = coverUrl,
    detailUrl = detailUrl,
    status = status.toStatusString(),
    localUri = if (status is MusicStatus.Downloaded) (status as MusicStatus.Downloaded).localUri else null
)

fun MusicStatus.toStatusString(): String {
    return when (this) {
        is MusicStatus.None -> "None"
        is MusicStatus.Downloading -> "Downloading"
        is MusicStatus.Downloaded -> "Downloaded"
        is MusicStatus.PartiallyCached -> "PartiallyCached"
        is MusicStatus.FullyCached -> "FullyCached"
    }
}