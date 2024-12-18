package com.ccc.ncs.network.model

import com.ccc.ncs.model.Music
import com.ccc.ncs.model.MusicStatus


fun Music.toDataClassString(): String {
    // Helper function to convert MusicStatus to a string representation
    fun MusicStatus.toStatusString(): String = when (this) {
        is MusicStatus.None -> "None"
        is MusicStatus.Downloading -> "Downloading"
        is MusicStatus.Downloaded -> "Downloaded(\"$localUri\")"
        is MusicStatus.PartiallyCached -> "PartiallyCached"
        is MusicStatus.FullyCached -> "FullyCached"
    }

    val artistStrings = artists.joinToString(", ") { artist ->
        """Artist(
            |        name = "${artist.name}",
            |        photoUrl = ${artist.photoUrl?.let { "\"$it\"" } ?: "null"},
            |        detailUrl = "${artist.detailUrl}",
            |        tags = "${artist.tags}"
            |    )""".trimMargin()
    }

    val genreStrings = genres.joinToString(", ") { genre ->
        """Genre(id = ${genre.id}, name = "${genre.name}")"""
    }

    val moodStrings = moods.joinToString(", ") { mood ->
        """Mood(id = ${mood.id}, name = "${mood.name}")"""
    }

    val versionStrings = versions.joinToString(", ") { version ->
        "Version.${version.name}"
    }

    return """Music(
        |    id = UUID.fromString("$id"),
        |    title = "$title",
        |    artists = listOf($artistStrings),
        |    releaseDate = LocalDate.parse("$releaseDate"),
        |    dataUrl = "$dataUrl",
        |    coverThumbnailUrl = "$coverThumbnailUrl",
        |    coverUrl = "$coverUrl",
        |    detailUrl = "$detailUrl",
        |    genres = setOf($genreStrings),
        |    moods = setOf($moodStrings),
        |    versions = setOf($versionStrings),
        |    status = MusicStatus.${status.toStatusString()}
        |)""".trimMargin()
}
