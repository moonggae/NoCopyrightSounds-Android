package com.ccc.ncs.model

import java.time.LocalDate
import java.util.UUID

data class Music(
    val id: UUID,
    val title: String,
    val artist: String,
    val releaseDate: LocalDate,
    val dataUrl: String,
    val coverThumbnailUrl: String,
    val coverUrl: String,
    val detailUrl: String,
    val artistDetailUrl: String,
    val genres: Set<Genre>,
    val moods: Set<Mood>,
    val versions: Set<Version>
)