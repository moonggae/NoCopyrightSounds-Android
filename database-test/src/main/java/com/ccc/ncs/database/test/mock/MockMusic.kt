package com.ccc.ncs.database.test.mock

import com.ccc.ncs.database.model.asEntity
import com.ccc.ncs.database.model.relation.MusicWithGenreAndMood
import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import java.time.LocalDate
import java.util.UUID


val MockMusicList = listOf(
    Music(
        id = UUID.fromString("c81804c6-c916-4288-bb28-1e811f2b795b"),
        title = "I Wanna Dance",
        artists = listOf(Artist("PYTI", null, "artist/1100/pyti", "Techno")),
        releaseDate = LocalDate.parse("2024-04-16"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/662/1713276672_isnlxFlS9J_01-PYTI---I-Wanna-Dance-NCS-Release.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/662/100x100/i-wanna-dance-1713272452-ESu9xGPxYk.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/662/325x325/i-wanna-dance-1713272452-ESu9xGPxYk.jpg",
        detailUrl = "https://ncs.io/IWannaDance",
        genres = setOf(Genre(id = 80, name = "Techno")),
        moods = setOf(
            Mood(id = 1, name = "Angry"),
            Mood(id = 2, name = "Dark"),
            Mood(id = 26, name = "Chasing"),
            Mood(id = 5, name = "Euphoric"),
            Mood(id = 4, name = "Epic"),
            Mood(id = 7, name = "Fear"),
            Mood(id = 30, name = "Heavy"),
            Mood(id = 18, name = "Restless"),
            Mood(id = 23, name = "Suspense")
        ),
        versions = setOf()
    ),
    Music(
        id = UUID.fromString("049fc0ba-8998-45d7-bba4-cacd18aa1793"),
        title = "Hollow",
        artists = listOf(Artist("Unlike Pluto", null, "artist/1099/unlike-pluto", "Alternative Pop")),
        releaseDate = LocalDate.parse("2024-04-12"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/661/hollow-1712880052-0MJUprE41X.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/661/100x100/hollow-1712880050-hgQO2wDq2Y.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/661/325x325/hollow-1712880050-hgQO2wDq2Y.jpg",
        detailUrl = "https://ncs.io/UP_Hollow",
        genres = setOf(Genre(id = 33, name = "Alternative Pop")),
        moods = setOf(
            Mood(id = 26, name = "Chasing"),
            Mood(id = 27, name = "Eccentric"),
            Mood(id = 5, name = "Euphoric"),
            Mood(id = 30, name = "Heavy"),
            Mood(id = 14, name = "Mysterious"),
            Mood(id = 16, name = "Quirky"),
            Mood(id = 18, name = "Restless")
        ),
        versions = setOf()
    ),
    Music(
        id = UUID.fromString("f26da352-0c23-4829-90c1-e7070acdf7bd"),
        title = "blessing",
        artists = listOf(Artist("P3PPER", null, "artist/1098/p3pper", "Melodic House")),
        releaseDate = LocalDate.parse("2024-04-11"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/660/blessing-1712793654-kznX5SFTsL.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/660/100x100/blessing-1712793652-4JwmiqrIUP.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/660/325x325/blessing-1712793652-4JwmiqrIUP.jpg",
        detailUrl = "https://ncs.io/blessing",
        genres = setOf(Genre(id = 54, name = "Melodic House")),
        moods = setOf(
            Mood(id = 3, name = "Dreamy"),
            Mood(id = 28, name = "Elegant"),
            Mood(id = 5, name = "Euphoric"),
            Mood(id = 29, name = "Floating"),
            Mood(id = 12, name = "Hopeful"),
            Mood(id = 22, name = "Sexy")
        ),
        versions = setOf()
    ),
    Music(
        id = UUID.fromString("9e5f2700-a887-4c76-bd50-e69722dee3e1"),
        title = "Stars in the Sky",
        artists = listOf(Artist("BEKSY.", null, "artist/1097/beksy", "Dance Pop")),
        releaseDate = LocalDate.parse("2024-04-09"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/659/stars-in-the-sky-1712620859-Z5NW7kpyJJ.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/659/100x100/stars-in-the-sky-1712620851-2jkA3mqxBh.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/659/325x325/stars-in-the-sky-1712620851-2jkA3mqxBh.jpg",
        detailUrl = "https://ncs.io/SITS",
        genres = setOf(Genre(id = 36, name = "Dance Pop")),
        moods = setOf(
            Mood(id = 3, name = "Dreamy"),
            Mood(id = 27, name = "Eccentric"),
            Mood(id = 28, name = "Elegant"),
            Mood(id = 9, name = "Glamorous"),
            Mood(id = 29, name = "Floating"),
            Mood(id = 13, name = "Laid Back"),
            Mood(id = 17, name = "Relaxing"),
            Mood(id = 19, name = "Romantic"),
            Mood(id = 31, name = "Sentimental")
        ),
        versions = setOf()
    )
)

val MockMusicWithGenreAndMoodList: List<MusicWithGenreAndMood> = MockMusicList.map { music ->
    MusicWithGenreAndMood(
        music = music.asEntity(),
        genres = music.genres.map { it.asEntity() }.toSet(),
        moods = music.moods.map { it.asEntity() }.toSet()
    )
}