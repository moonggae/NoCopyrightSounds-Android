package com.ccc.ncs.database.mock

import com.ccc.ncs.database.model.MusicEntity
import com.ccc.ncs.database.model.relation.MusicWithGenreAndMood
import java.util.UUID


internal val MockMusic = MusicEntity(
    id = UUID.fromString("049fc0ba-8998-45d7-bba4-cacd18aa1793"),
    title = "Hollow",
    artist = "Unlike Pluto",
    releaseDate = "2024-04-12",
    dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/661/hollow-1712880052-0MJUprE41X.mp3",
    coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/661/100x100/hollow-1712880050-hgQO2wDq2Y.jpg",
    coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/661/325x325/hollow-1712880050-hgQO2wDq2Y.jpg",
    detailUrl = "/UP_Hollow",
    artistDetailUrl = "/artist/1099/unlike-pluto"
)

internal val MockMusicWithGenreAndMood: MusicWithGenreAndMood = MusicWithGenreAndMood(
    music = MockMusic,
    genres = MockGenreList.filter { it.id == 33 }.toSet(),
    moods = MockMoodList.filter {
        listOf(26, 27, 5, 30, 14, 16, 18).contains(it.id)
    }.toSet()
)