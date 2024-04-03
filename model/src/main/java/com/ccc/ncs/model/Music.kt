package com.ccc.ncs.model

import java.time.LocalDate

data class Music(
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