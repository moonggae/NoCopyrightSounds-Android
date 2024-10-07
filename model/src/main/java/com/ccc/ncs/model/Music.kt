package com.ccc.ncs.model

import java.time.LocalDate
import java.util.UUID

data class Music(
    val id: UUID,
    val title: String,
    val artists: List<Artist>,
    val releaseDate: LocalDate,
    val dataUrl: String,
    val coverThumbnailUrl: String,
    val coverUrl: String,
    val detailUrl: String,
    val genres: Set<Genre>,
    val moods: Set<Mood>,
    val versions: Set<Version>,
    val status: MusicStatus = MusicStatus.None
)

sealed class MusicStatus {
    data object None : MusicStatus()
    data object Downloading: MusicStatus()
    data class Downloaded(val localUri: String): MusicStatus()
    data object PartiallyCached: MusicStatus()
    data object FullyCached: MusicStatus()
}

val Music.artistText: String
    get() = artists.joinToString { it.name }