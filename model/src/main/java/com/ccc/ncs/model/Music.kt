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
    val versions: Set<Version>
)

val Music.artistText: String
    get() = artists.joinToString { it.name }