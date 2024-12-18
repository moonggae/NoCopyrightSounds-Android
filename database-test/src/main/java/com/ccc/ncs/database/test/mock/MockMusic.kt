package com.ccc.ncs.database.test.mock

import com.ccc.ncs.database.model.asEntity
import com.ccc.ncs.database.model.relation.MusicWithGenreAndMood
import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.MusicStatus
import java.time.LocalDate
import java.util.UUID


val MockMusicList = listOf(
    Music(
        id = UUID.fromString("bf8bbb28-d551-4cbb-93c9-34f23063bda4"),
        title = "After The End",
        artists = listOf(Artist(
            name = "Rex Hooligan",
            photoUrl = null,
            detailUrl = "/artist/751/rex-hooligan",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-12-17"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/819/1734441205_bXSf9DoWLA_01-Rex-Hooligan---After-The-End-NCS-Release.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/819/100x100/after-the-end-1734397255-9Lh9UTTj82.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/819/1000x0/after-the-end-1734397255-9Lh9UTTj82.jpg",
        detailUrl = "https://ncs.io/AfterTheEnd",
        genres = setOf(Genre(id = 3, name = "Drum & Bass")),
        moods = setOf(Mood(id = 6, name = "energetic"), Mood(id = 40, name = "positive"), Mood(id = 32, name = "powerful")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("b4a5ea39-ab6f-4d35-a34b-1ad5d3efeead"),
        title = "Shine x Never Have I Felt This (VIP) Mashup",
        artists = listOf(Artist(
            name = "Koven",
            photoUrl = null,
            detailUrl = "/artist/251/koven",
            tags = ""
        ), Artist(
            name = "Spektrem",
            photoUrl = null,
            detailUrl = "/artist/416/spektrem",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-12-15"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/818/shine-x-never-have-i-felt-this-vip-mashup-1734224456-mkQK6MstNZ.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/818/100x100/shine-x-never-have-i-felt-this-vip-mashup-1734224454-llSoL9nvW5.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/818/1000x0/shine-x-never-have-i-felt-this-vip-mashup-1734224454-llSoL9nvW5.jpg",
        detailUrl = "https://ncs.io/SxNH",
        genres = setOf(Genre(id = 3, name = "Drum & Bass")),
        moods = setOf(Mood(id = 36, name = "dramatic"), Mood(id = 6, name = "energetic"), Mood(id = 32, name = "powerful")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("6d9a7866-a655-454d-b9a9-7af0a179f3d9"),
        title = "CHEAT CODES",
        artists = listOf(Artist(
            name = "TOKYO MACHINE",
            photoUrl = null,
            detailUrl = "/artist/531/tokyo-machine",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-12-13"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/817/cheat-codes-1734051655-NotfduweTh.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/817/100x100/cheat-codes-1734051653-hHkS55rL0V.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/817/1000x0/cheat-codes-1734051653-hHkS55rL0V.jpg",
        detailUrl = "https://ncs.io/cheatcodes",
        genres = setOf(Genre(id = 5, name = "Dubstep")),
        moods = setOf(Mood(id = 32, name = "powerful"), Mood(id = 33, name = "angry"), Mood(id = 34, name = "negative")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("49d4a22d-5bd4-48b5-b690-23f935073e26"),
        title = "I Can Feel",
        artists = listOf(Artist(
            name = "Syn Cole",
            photoUrl = null,
            detailUrl = "/artist/434/syn-cole",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-12-12"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/816/i-can-feel-1733965258-CUZGpIG30o.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/816/100x100/i-can-feel-1733965256-YXbmlkiuMp.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/816/1000x0/i-can-feel-1733965256-YXbmlkiuMp.jpg",
        detailUrl = "https://ncs.io/ICanFeel",
        genres = setOf(Genre(id = 3, name = "Drum & Bass")),
        moods = setOf(Mood(id = 6, name = "energetic"), Mood(id = 40, name = "positive"), Mood(id = 32, name = "powerful")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("73101dd0-c319-494a-96d9-6e48de9594f2"),
        title = "What You Did",
        artists = listOf(Artist(
            name = "eerie",
            photoUrl = null,
            detailUrl = "/artist/1146/eerie",
            tags = ""
        ), Artist(
            name = "Rameses B",
            photoUrl = null,
            detailUrl = "/artist/364/rameses-b",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-12-10"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/815/what-you-did-1733792461-IunWtNAKbg.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/815/100x100/what-you-did-1733792458-vYVprGNPBu.png",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/815/1000x0/what-you-did-1733792458-vYVprGNPBu.png",
        detailUrl = "https://ncs.io/WhatYouDid",
        genres = setOf(Genre(id = 29, name = "Liquid DnB")),
        moods = setOf(Mood(id = 32, name = "powerful"), Mood(id = 6, name = "energetic"), Mood(id = 39, name = "neutral")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("598a4b7b-7980-4d27-912e-81ca14fec7ca"),
        title = "Strobe",
        artists = listOf(Artist(
            name = "NIVIRO",
            photoUrl = null,
            detailUrl = "/artist/337/niviro",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-12-06"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/813/strobe-1733446858-c4mvlbTKTf.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/813/100x100/strobe-1733446856-PCrTkLuaWv.png",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/813/1000x0/strobe-1733446856-PCrTkLuaWv.png",
        detailUrl = "https://ncs.io/Strobe",
        genres = setOf(Genre(id = 80, name = "Techno")),
        moods = setOf(Mood(id = 6, name = "energetic"), Mood(id = 35, name = "dark"), Mood(id = 39, name = "neutral")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("13d34597-2546-46a4-9842-3ba06425f170"),
        title = "RUINS",
        artists = listOf(Artist(
            name = "DJ FKU",
            photoUrl = null,
            detailUrl = "/artist/1130/dj-fku",
            tags = ""
        ), Artist(
            name = "LXNGVX",
            photoUrl = null,
            detailUrl = "/artist/1156/lxngvx",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-12-06"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/814/ruins-1733446865-LQlnvzERhj.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/814/100x100/ruins-1733446864-4gin1pwuwN.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/814/1000x0/ruins-1733446864-4gin1pwuwN.jpg",
        detailUrl = "https://ncs.io/RUINS",
        genres = setOf(Genre(id = 3, name = "Drum & Bass")),
        moods = setOf(Mood(id = 32, name = "powerful"), Mood(id = 6, name = "energetic"), Mood(id = 39, name = "neutral")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("78400e46-00c6-4600-84a0-84049a99eb95"),
        title = "Severed Rose",
        artists = listOf(Artist(
            name = "SlidV",
            photoUrl = null,
            detailUrl = "/artist/994/slidv",
            tags = ""
        ), Artist(
            name = "Clarx",
            photoUrl = null,
            detailUrl = "/artist/81/clarx",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-12-05"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/812/severed-rose-1733360456-Vpv1z3x0mY.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/812/100x100/severed-rose-1733360455-zVggjMOPR9.png",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/812/1000x0/severed-rose-1733360455-zVggjMOPR9.png",
        detailUrl = "https://ncs.io/SeveredRose",
        genres = setOf(Genre(id = 83, name = "Electronic Rock")),
        moods = setOf(Mood(id = 36, name = "dramatic"), Mood(id = 6, name = "energetic"), Mood(id = 32, name = "powerful")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("6a41cd45-1c40-4190-b2e6-a12cffe929be"),
        title = "Like That",
        artists = listOf(Artist(
            name = "Cherry Morello",
            photoUrl = null,
            detailUrl = "/artist/1201/cherry-morello",
            tags = ""
        ), Artist(
            name = "Don Darkoe",
            photoUrl = null,
            detailUrl = "/artist/1202/don-darkoe",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-12-03"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/811/like-that-1733187657-jvnY9Ppx3V.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/811/100x100/like-that-1733187655-L15ODYoZAG.JPG",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/811/1000x0/like-that-1733187655-L15ODYoZAG.JPG",
        detailUrl = "https://ncs.io/DD_LikeThat",
        genres = setOf(Genre(id = 3, name = "Drum & Bass")),
        moods = setOf(Mood(id = 36, name = "dramatic"), Mood(id = 6, name = "energetic"), Mood(id = 32, name = "powerful")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("f4cfe567-a900-41cd-8240-f16f2d99e9d1"),
        title = "Second Wind",
        artists = listOf(Artist(
            name = "Matt Pridgyn",
            photoUrl = null,
            detailUrl = "/artist/1200/matt-pridgyn",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-11-29"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/810/second-wind-1732842057-rxivPWt0Vm.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/810/100x100/second-wind-1732842054-KYgMrGozrf.png",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/810/1000x0/second-wind-1732842054-KYgMrGozrf.png",
        detailUrl = "https://ncs.io/SecondWind",
        genres = setOf(Genre(id = 55, name = "Progressive House")),
        moods = setOf(Mood(id = 6, name = "energetic"), Mood(id = 32, name = "powerful"), Mood(id = 40, name = "positive")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("4a76fa4a-908e-4179-b6b7-d1e8ed33dcfa"),
        title = "Retrospective",
        artists = listOf(Artist(
            name = "Alex Skrindo",
            photoUrl = null,
            detailUrl = "/artist/15/alex-skrindo",
            tags = ""
        ), Artist(
            name = "JJD",
            photoUrl = null,
            detailUrl = "/artist/211/jjd",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-11-28"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/809/retrospective-1732755656-insKerPyAF.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/809/100x100/retrospective-1732755654-fkVlbqmplD.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/809/1000x0/retrospective-1732755654-fkVlbqmplD.jpg",
        detailUrl = "https://ncs.io/Retrospective",
        genres = setOf(Genre(id = 11, name = "Indie Dance")),
        moods = setOf(Mood(id = 6, name = "energetic"), Mood(id = 32, name = "powerful"), Mood(id = 40, name = "positive")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("a72b01e3-1976-4dee-b721-814bc4b7cca7"),
        title = "On My Mind",
        artists = listOf(Artist(
            name = "No Hero",
            photoUrl = null,
            detailUrl = "/artist/1199/no-hero",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-11-26"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/808/on-my-mind-1732582857-HrvroCzgQ9.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/808/100x100/on-my-mind-1732582855-BeyB7w7ETO.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/808/1000x0/on-my-mind-1732582855-BeyB7w7ETO.jpg",
        detailUrl = "https://ncs.io/OnMyMind",
        genres = setOf(Genre(id = 9, name = "Hardstyle")),
        moods = setOf(Mood(id = 6, name = "energetic"), Mood(id = 32, name = "powerful"), Mood(id = 40, name = "positive")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("55e23ca1-072d-4445-8a3e-f38353b5c2c2"),
        title = "Firestarter",
        artists = listOf(Artist(
            name = "Lack D",
            photoUrl = null,
            detailUrl = "/artist/1198/lack-d",
            tags = ""
        ), Artist(
            name = "KDH",
            photoUrl = null,
            detailUrl = "/artist/877/kdh",
            tags = ""
        ), Artist(
            name = "jeonghyeon",
            photoUrl = null,
            detailUrl = "/artist/611/jeonghyeon",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-11-22"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/806/firestarter-1732273255-86MB4ibYuj.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/806/100x100/firestarter-1732273253-0z9FH8fDXU.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/806/1000x0/firestarter-1732273253-0z9FH8fDXU.jpg",
        detailUrl = "https://ncs.io/Firestarter",
        genres = setOf(Genre(id = 8, name = "Future House")),
        moods = setOf(Mood(id = 32, name = "powerful"), Mood(id = 6, name = "energetic"), Mood(id = 39, name = "neutral")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("e19b9284-ed7d-4130-b39c-0c53939a8bda"),
        title = "Citadel",
        artists = listOf(Artist(
            name = "Boom Kitty",
            photoUrl = null,
            detailUrl = "/artist/1197/boom-kitty",
            tags = ""
        ), Artist(
            name = "Waterflame",
            photoUrl = null,
            detailUrl = "/artist/1196/waterflame",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-11-21"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/807/citadel-1732273259-JkI4v3mrNV.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/807/100x100/citadel-1732273257-0dOvLwQ0V6.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/807/1000x0/citadel-1732273257-0dOvLwQ0V6.jpg",
        detailUrl = "https://ncs.io/Citadel",
        genres = setOf(Genre(id = 18, name = "Bass House")),
        moods = setOf(Mood(id = 32, name = "powerful"), Mood(id = 6, name = "energetic"), Mood(id = 39, name = "neutral")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("254bddf2-0504-44ab-b175-d73181b65bd2"),
        title = "Your Burn",
        artists = listOf(Artist(
            name = "vxcelm",
            photoUrl = null,
            detailUrl = "/artist/1195/vxcelm",
            tags = ""
        ), Artist(
            name = "ANIZYZ",
            photoUrl = null,
            detailUrl = "/artist/1149/anizyz",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-11-19"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/805/your-burn-1731978057-NcMU8vkEBn.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/805/100x100/your-burn-1731978054-rhCAZVNaIC.png",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/805/1000x0/your-burn-1731978054-rhCAZVNaIC.png",
        detailUrl = "https://ncs.io/YourBurn",
        genres = setOf(Genre(id = 9, name = "Hardstyle")),
        moods = setOf(Mood(id = 6, name = "energetic"), Mood(id = 37, name = "exciting"), Mood(id = 40, name = "positive")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("a51d80dc-ef37-4996-a021-5245f7965787"),
        title = "Call My Name",
        artists = listOf(Artist(
            name = "Robbie Hutton",
            photoUrl = null,
            detailUrl = "/artist/1194/robbie-hutton",
            tags = ""
        ), Artist(
            name = "Janji",
            photoUrl = null,
            detailUrl = "/artist/198/janji",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-11-15"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/804/call-my-name-1731632457-61g1WQb0d8.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/804/100x100/call-my-name-1731632455-PS8GyCQEln.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/804/1000x0/call-my-name-1731632455-PS8GyCQEln.jpg",
        detailUrl = "https://ncs.io/CallMyName",
        genres = setOf(Genre(id = 36, name = "Dance-Pop")),
        moods = setOf(Mood(id = 19, name = "romantic"), Mood(id = 32, name = "powerful"), Mood(id = 39, name = "neutral")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("44732c73-f15a-4994-9cad-02c180765a11"),
        title = "Geometry",
        artists = listOf(Artist(
            name = "Diviners",
            photoUrl = null,
            detailUrl = "/artist/115/diviners",
            tags = ""
        ), Artist(
            name = "Tobu",
            photoUrl = null,
            detailUrl = "/artist/456/tobu",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-11-14"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/803/geometry-1731546058-Hz3KjOVBtU.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/803/100x100/geometry-1731546056-ZRpP9TkxU5.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/803/1000x0/geometry-1731546056-ZRpP9TkxU5.jpg",
        detailUrl = "https://ncs.io/Geometry",
        genres = setOf(Genre(id = 55, name = "Progressive House")),
        moods = setOf(Mood(id = 6, name = "energetic"), Mood(id = 37, name = "exciting"), Mood(id = 40, name = "positive")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("be29348f-e002-4c42-b90e-8f222335126e"),
        title = "Crowded Room",
        artists = listOf(Artist(
            name = "Josh Rubin",
            photoUrl = null,
            detailUrl = "/artist/920/josh-rubin",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-11-12"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/802/crowded-room-1731373258-cFfqyLhLpr.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/802/100x100/crowded-room-1731373256-PmYUDXScqk.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/802/1000x0/crowded-room-1731373256-PmYUDXScqk.jpg",
        detailUrl = "https://ncs.io/CrowdedRoom",
        genres = setOf(Genre(id = 17, name = "Future Bass")),
        moods = setOf(Mood(id = 32, name = "powerful"), Mood(id = 33, name = "angry"), Mood(id = 34, name = "negative")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("b75141d3-af58-4c27-a2e5-71eb194b4d5b"),
        title = "Back on Dash",
        artists = listOf(Artist(
            name = "DJVI",
            photoUrl = null,
            detailUrl = "/artist/1193/djvi",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-11-08"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/801/back-on-dash-1731027656-46x8eA7Qc4.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/801/100x100/back-on-dash-1731027655-GO2zDIsYtp.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/801/1000x0/back-on-dash-1731027655-GO2zDIsYtp.jpg",
        detailUrl = "https://ncs.io/BackOnDash",
        genres = setOf(Genre(id = 55, name = "Progressive House")),
        moods = setOf(Mood(id = 6, name = "energetic"), Mood(id = 37, name = "exciting"), Mood(id = 40, name = "positive")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("389756fc-3503-4d45-819d-a03bdd369ee5"),
        title = "Obsession",
        artists = listOf(Artist(
            name = "More Plastic",
            photoUrl = null,
            detailUrl = "/artist/322/more-plastic",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-11-07"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/800/obsession-1730941257-mFaCk2t7kZ.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/800/100x100/1731427545_vUim1pu6wh_obsession-cover_3k.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/800/1000x0/1731427545_vUim1pu6wh_obsession-cover_3k.jpg",
        detailUrl = "https://ncs.io/Obsession",
        genres = setOf(Genre(id = 30, name = "Neurofunk")),
        moods = setOf(Mood(id = 6, name = "energetic"), Mood(id = 32, name = "powerful"), Mood(id = 39, name = "neutral")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("cb6dc883-6cd0-4bbe-b3c3-0d9fe7c29299"),
        title = "i want u",
        artists = listOf(Artist(
            name = "Rameses B",
            photoUrl = null,
            detailUrl = "/artist/364/rameses-b",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-11-05"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/799/i-want-u-1730768460-8aJSGzPgJD.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/799/100x100/i-want-u-1730768457-7wk36p9MQU.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/799/1000x0/i-want-u-1730768457-7wk36p9MQU.jpg",
        detailUrl = "https://ncs.io/iwantu",
        genres = setOf(Genre(id = 3, name = "Drum & Bass")),
        moods = setOf(Mood(id = 32, name = "powerful"), Mood(id = 6, name = "energetic"), Mood(id = 39, name = "neutral")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("9bbbb432-febf-4122-9eb9-070dfca80902"),
        title = "Perfection (Feat. Derek Cate)",
        artists = listOf(Artist(
            name = "Derek Cate",
            photoUrl = null,
            detailUrl = "/artist/1190/derek-cate",
            tags = ""
        ), Artist(
            name = "B3nte",
            photoUrl = null,
            detailUrl = "/artist/1191/b3nte",
            tags = ""
        ), Artist(
            name = "Mangoo",
            photoUrl = null,
            detailUrl = "/artist/553/mangoo",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-11-01"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/798/1730713313_7UzY0cpVMj_01-Mangoo-B3nte---Perfection-Feat.-Derek-Cate-NCS-Release.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/798/100x100/1730713312_hsfbBKXzm4_Perfection_FINAL_shrunk.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/798/1000x0/1730713312_hsfbBKXzm4_Perfection_FINAL_shrunk.jpg",
        detailUrl = "https://ncs.io/Perfection",
        genres = setOf(Genre(id = 36, name = "Dance-Pop")),
        moods = setOf(Mood(id = 17, name = "relaxed"), Mood(id = 40, name = "positive")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("ca2b2c1e-17af-4420-b334-558e1e7a24ce"),
        title = "Cyberblade",
        artists = listOf(Artist(
            name = "Extra Terra",
            photoUrl = null,
            detailUrl = "/artist/1175/extra-terra",
            tags = ""
        ), Artist(
            name = "Max Brhon",
            photoUrl = null,
            detailUrl = "/artist/298/max-brhon",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-10-31"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/797/cyberblade-1730336458-qObwoJC2Nd.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/797/100x100/cyberblade-1730336456-G3cx4PbGyL.png",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/797/1000x0/cyberblade-1730336456-G3cx4PbGyL.png",
        detailUrl = "https://ncs.io/Cyberblade",
        genres = setOf(Genre(id = 22, name = "Midtempo Bass")),
        moods = setOf(Mood(id = 32, name = "powerful"), Mood(id = 33, name = "angry"), Mood(id = 34, name = "negative")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("26ac29eb-d75b-4265-bafd-a5d2c240fa2a"),
        title = "Hinterlands",
        artists = listOf(Artist(
            name = "Everen Maxwell",
            photoUrl = null,
            detailUrl = "/artist/780/everen-maxwell",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-10-29"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/795/hinterlands-1730163656-67BplquSFh.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/795/100x100/hinterlands-1730163654-bJagtIMajE.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/795/1000x0/hinterlands-1730163654-bJagtIMajE.jpg",
        detailUrl = "https://ncs.io/Hinterlands",
        genres = setOf(Genre(id = 22, name = "Midtempo Bass")),
        moods = setOf(Mood(id = 32, name = "powerful"), Mood(id = 20, name = "sad"), Mood(id = 34, name = "negative")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("da568d9f-7340-418b-ad04-11862f682171"),
        title = "My Heart x Everything Mashup",
        artists = listOf(Artist(
            name = "EH!DE",
            photoUrl = null,
            detailUrl = "/artist/124/ehde",
            tags = ""
        ), Artist(
            name = "Different Heaven",
            photoUrl = null,
            detailUrl = "/artist/110/different-heaven",
            tags = ""
        ), Artist(
            name = "Diamond Eyes",
            photoUrl = null,
            detailUrl = "/artist/109/diamond-eyes",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-10-25"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/796/my-heart-x-everything-mashup-1730199656-LpTZsoJ7zp.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/796/100x100/my-heart-x-everything-mashup-1730199654-J4W9CRumPX.png",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/796/1000x0/my-heart-x-everything-mashup-1730199654-J4W9CRumPX.png",
        detailUrl = "https://ncs.io/MHxE",
        genres = setOf(Genre(id = 12, name = "Melodic Dubstep")),
        moods = setOf(Mood(id = 32, name = "powerful"), Mood(id = 33, name = "angry"), Mood(id = 39, name = "neutral")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("1b1c6a8e-b1ed-430a-b3ae-1b6358945747"),
        title = "AIWA",
        artists = listOf(Artist(
            name = "THIRST",
            photoUrl = null,
            detailUrl = "/artist/1189/thirst",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-10-24"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/794/aiwa-1729728057-pGTSvDFEse.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/794/100x100/aiwa-1729728055-kHJL0MnYqD.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/794/1000x0/aiwa-1729728055-kHJL0MnYqD.jpg",
        detailUrl = "https://ncs.io/AIWA",
        genres = setOf(Genre(id = 16, name = "Phonk")),
        moods = setOf(Mood(id = 32, name = "powerful"), Mood(id = 33, name = "angry"), Mood(id = 39, name = "neutral")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("ec1cb703-b625-4934-b198-97a21b4b0705"),
        title = "Shoulders of Giants",
        artists = listOf(Artist(
            name = "Halvorsen",
            photoUrl = null,
            detailUrl = "/artist/171/halvorsen",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-10-22"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/793/shoulders-of-giants-1729555257-ZS80Q2HrAP.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/793/100x100/shoulders-of-giants-1729555255-lV3oPLz333.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/793/1000x0/shoulders-of-giants-1729555255-lV3oPLz333.jpg",
        detailUrl = "https://ncs.io/SOG",
        genres = setOf(Genre(id = 65, name = "Complextro")),
        moods = setOf(Mood(id = 6, name = "energetic"), Mood(id = 32, name = "powerful"), Mood(id = 40, name = "positive")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("b3ca451c-3b71-4c9b-9b99-d361d8d8de69"),
        title = "Break My Heart",
        artists = listOf(Artist(
            name = "sapientdream",
            photoUrl = null,
            detailUrl = "/artist/1187/sapientdream",
            tags = ""
        ), Artist(
            name = "Slushii",
            photoUrl = null,
            detailUrl = "/artist/1188/slushii",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-10-18"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/792/break-my-heart-1729209661-ztNlIJxb0a.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/792/100x100/1729257940_RyLwhtv90T_FINAL-BMH_3k.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/792/1000x0/1729257940_RyLwhtv90T_FINAL-BMH_3k.jpg",
        detailUrl = "https://ncs.io/BreakMyHeart",
        genres = setOf(Genre(id = 3, name = "Drum & Bass")),
        moods = setOf(Mood(id = 35, name = "dark"), Mood(id = 39, name = "neutral")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("6ab8d0e3-2ecd-4a2e-b72b-c4599d9e7bbc"),
        title = "From The Top",
        artists = listOf(Artist(
            name = "Ariis",
            photoUrl = null,
            detailUrl = "/artist/1186/ariis",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-10-17"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/791/from-the-top-1729123260-3tOJiB7Ewf.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/791/100x100/from-the-top-1729123257-DTjTWVwvmu.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/791/1000x0/from-the-top-1729123257-DTjTWVwvmu.jpg",
        detailUrl = "https://ncs.io/FromTheTop",
        genres = setOf(Genre(id = 16, name = "Phonk")),
        moods = setOf(Mood(id = 19, name = "romantic"), Mood(id = 6, name = "energetic"), Mood(id = 39, name = "neutral")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("dc7edbe0-3848-4746-b54c-02579dc269bd"),
        title = "Notice That",
        artists = listOf(Artist(
            name = "Zaug",
            photoUrl = null,
            detailUrl = "/artist/902/zaug",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-10-15"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/789/notice-that-1728950459-uHbxIHB2J8.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/789/100x100/notice-that-1728950458-5ZtHHKDo52.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/789/1000x0/notice-that-1728950458-5ZtHHKDo52.jpg",
        detailUrl = "https://ncs.io/NoticeThat",
        genres = setOf(Genre(id = 83, name = "Electronic Rock")),
        moods = setOf(Mood(id = 32, name = "powerful"), Mood(id = 39, name = "neutral")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("5a305115-6319-4d15-802b-da9c1024f056"),
        title = "Revolution",
        artists = listOf(Artist(
            name = "Linn Sandin",
            photoUrl = null,
            detailUrl = "/artist/590/linn-sandin",
            tags = ""
        ), Artist(
            name = "Midranger",
            photoUrl = null,
            detailUrl = "/artist/312/midranger",
            tags = ""
        ), Artist(
            name = "Poylow",
            photoUrl = null,
            detailUrl = "/artist/613/poylow",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-10-11"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/788/1728648382_QznkBmsygy_01-Poylow-Linn-Sandin-Midranger---Revolution-NCS-Release.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/788/100x100/revolution-1728648053-lcOYMCjgjw.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/788/1000x0/revolution-1728648053-lcOYMCjgjw.jpg",
        detailUrl = "https://ncs.io/PL_Revolution",
        genres = setOf(Genre(id = 69, name = "Future Rave")),
        moods = setOf(Mood(id = 32, name = "powerful"), Mood(id = 37, name = "exciting"), Mood(id = 39, name = "neutral"), Mood(id = 35, name = "dark"), Mood(id = 34, name = "negative"), Mood(id = 6, name = "energetic")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("0f42c347-dce4-4173-9d61-4ec44d8a5c51"),
        title = "Your Poison",
        artists = listOf(Artist(
            name = "ROY KNOX",
            photoUrl = null,
            detailUrl = "/artist/392/roy-knox",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-10-10"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/786/1728645187_ZAIsKaw6Lv_ROY-KNOX---Your-Poison.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/786/100x100/your-poison-1728561655-YBgf7fx1Bb.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/786/1000x0/your-poison-1728561655-YBgf7fx1Bb.jpg",
        detailUrl = "https://ncs.io/YourPoison",
        genres = setOf(Genre(id = 5, name = "Dubstep")),
        moods = setOf(Mood(id = 4, name = "Epic"), Mood(id = 5, name = "Euphoric"), Mood(id = 9, name = "Glamorous"), Mood(id = 30, name = "Heavy"), Mood(id = 18, name = "Restless"), Mood(id = 12, name = "Hopeful")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("dec55a22-b569-40ec-af12-14e2fc84e24e"),
        title = "Magnetic",
        artists = listOf(Artist(
            name = "springs!",
            photoUrl = null,
            detailUrl = "/artist/1183/springs",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-10-08"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/785/magnetic-1728385253-LamC7QyVlv.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/785/100x100/magnetic-1728385251-hyJZShDHui.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/785/1000x0/magnetic-1728385251-hyJZShDHui.jpg",
        detailUrl = "https://ncs.io/Magnetic",
        genres = setOf(Genre(id = 86, name = "Pluggnb")),
        moods = setOf(Mood(id = 3, name = "Dreamy"), Mood(id = 28, name = "Elegant"), Mood(id = 5, name = "Euphoric"), Mood(id = 29, name = "Floating"), Mood(id = 9, name = "Glamorous"), Mood(id = 13, name = "Laid Back"), Mood(id = 16, name = "Quirky"), Mood(id = 19, name = "romantic"), Mood(id = 31, name = "Sentimental"), Mood(id = 22, name = "Sexy")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("13cc39ff-e9eb-4842-b5ae-fc29c8622de6"),
        title = "The Riot",
        artists = listOf(Artist(
            name = "NIVIRO",
            photoUrl = null,
            detailUrl = "/artist/337/niviro",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-10-04"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/777/the-riot-1728000056-LYPkA3oa3w.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/777/100x100/the-riot-1728000053-6YoJReHr7r.png",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/777/1000x0/the-riot-1728000053-6YoJReHr7r.png",
        detailUrl = "https://ncs.io/TheRiot",
        genres = setOf(Genre(id = 22, name = "Midtempo Bass")),
        moods = setOf(Mood(id = 1, name = "Angry"), Mood(id = 26, name = "Chasing"), Mood(id = 7, name = "Fear"), Mood(id = 30, name = "Heavy"), Mood(id = 18, name = "Restless"), Mood(id = 23, name = "Suspense")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("901341b5-ba39-4f9f-93bd-703fa83abba6"),
        title = "Happier Now",
        artists = listOf(Artist(
            name = "Gabriel Eli",
            photoUrl = null,
            detailUrl = "/artist/1182/gabriel-eli",
            tags = ""
        ), Artist(
            name = "SadBois",
            photoUrl = null,
            detailUrl = "/artist/1079/sadbois",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-10-03"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/776/happier-now-1727913656-M2yKqJiV3p.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/776/100x100/happier-now-1727913653-F6F2izeDCI.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/776/1000x0/happier-now-1727913653-F6F2izeDCI.jpg",
        detailUrl = "https://ncs.io/HappierNow",
        genres = setOf(Genre(id = 83, name = "Electronic Rock")),
        moods = setOf(Mood(id = 26, name = "Chasing"), Mood(id = 5, name = "Euphoric"), Mood(id = 30, name = "Heavy"), Mood(id = 18, name = "Restless"), Mood(id = 31, name = "Sentimental")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("ca57c697-9762-4e5d-8375-6d9ed7854b6e"),
        title = "Baby Sweet (Tails Remix)",
        artists = listOf(Artist(
            name = "Tails",
            photoUrl = null,
            detailUrl = "/artist/1181/tails",
            tags = ""
        ), Artist(
            name = "intouch",
            photoUrl = null,
            detailUrl = "/artist/1001/intouch",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-10-02"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/775/baby-sweet-tails-remix-1727827255-BIPxwsifqt.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/775/100x100/baby-sweet-tails-remix-1727827254-b9w3wK0kIY.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/775/1000x0/baby-sweet-tails-remix-1727827254-b9w3wK0kIY.jpg",
        detailUrl = "https://ncs.io/BS_Tails",
        genres = setOf(Genre(id = 51, name = "Garage")),
        moods = setOf(Mood(id = 3, name = "Dreamy"), Mood(id = 27, name = "Eccentric"), Mood(id = 28, name = "Elegant"), Mood(id = 5, name = "Euphoric"), Mood(id = 29, name = "Floating"), Mood(id = 9, name = "Glamorous"), Mood(id = 19, name = "romantic"), Mood(id = 31, name = "Sentimental")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("dfad6b7f-ef19-4dee-b97d-3a2026b61ed2"),
        title = "Echo",
        artists = listOf(Artist(
            name = "Apollo On The Run",
            photoUrl = null,
            detailUrl = "/artist/1180/apollo-on-the-run",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-10-01"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/774/echo-1727784055-0bQCYNqoKJ.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/774/100x100/echo-1727784053-ScH5VfFzvw.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/774/1000x0/echo-1727784053-ScH5VfFzvw.jpg",
        detailUrl = "https://ncs.io/A_Echo",
        genres = setOf(Genre(id = 33, name = "Alternative Pop")),
        moods = setOf(Mood(id = 2, name = "Dark"), Mood(id = 3, name = "Dreamy"), Mood(id = 4, name = "Epic"), Mood(id = 12, name = "Hopeful"), Mood(id = 14, name = "Mysterious"), Mood(id = 18, name = "Restless"), Mood(id = 23, name = "Suspense")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("e107e1b8-d869-4742-a649-8a18a5027c2e"),
        title = "On and On",
        artists = listOf(Artist(
            name = "Sayfro",
            photoUrl = null,
            detailUrl = "/artist/1177/sayfro",
            tags = ""
        ), Artist(
            name = "BAYZY",
            photoUrl = null,
            detailUrl = "/artist/1178/bayzy",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-09-26"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/758/on-and-on-1727308858-tLh4ktBdt5.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/758/100x100/on-and-on-1727308855-nY2KAC7oXx.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/758/1000x0/on-and-on-1727308855-nY2KAC7oXx.jpg",
        detailUrl = "https://ncs.io/BS_OnandOn",
        genres = setOf(Genre(id = 10, name = "House")),
        moods = setOf(Mood(id = 4, name = "Epic"), Mood(id = 5, name = "Euphoric"), Mood(id = 11, name = "Happy"), Mood(id = 18, name = "Restless")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("1f1b2fc1-9aea-41ee-99c4-0c72044f38ee"),
        title = "Desperate",
        artists = listOf(Artist(
            name = "TOKYO MACHINE",
            photoUrl = null,
            detailUrl = "/artist/531/tokyo-machine",
            tags = ""
        ), Artist(
            name = "NEFFEX",
            photoUrl = null,
            detailUrl = "/artist/927/neffex",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-09-25"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/757/desperate-1727222454-NxFwPQLQCk.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/757/100x100/desperate-1727222452-FZRAmrmXyX.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/757/1000x0/desperate-1727222452-FZRAmrmXyX.jpg",
        detailUrl = "https://ncs.io/TM_Desperate",
        genres = setOf(Genre(id = 5, name = "Dubstep")),
        moods = setOf(Mood(id = 2, name = "Dark"), Mood(id = 1, name = "Angry"), Mood(id = 30, name = "Heavy"), Mood(id = 4, name = "Epic")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("33c26504-0108-4392-adc4-00e8154af6ff"),
        title = "blu",
        artists = listOf(Artist(
            name = "Zachz Winner",
            photoUrl = null,
            detailUrl = "/artist/1176/zachz-winner",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-09-24"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/756/blu-1727136055-NS2Rx3v9Ex.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/756/100x100/blu-1727136052-XeLOIaKGYK.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/756/1000x0/blu-1727136052-XeLOIaKGYK.jpg",
        detailUrl = "https://ncs.io/blu",
        genres = setOf(Genre(id = 86, name = "Pluggnb")),
        moods = setOf(Mood(id = 27, name = "Eccentric"), Mood(id = 28, name = "Elegant"), Mood(id = 8, name = "Funny"), Mood(id = 11, name = "Happy")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("8cce7a18-6c06-45d2-8754-e828f8ed0df9"),
        title = "Like Rain",
        artists = listOf(Artist(
            name = "Rameses B",
            photoUrl = null,
            detailUrl = "/artist/364/rameses-b",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-09-20"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/755/like-rain-1726790455-JHo5d1Ehqe.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/755/100x100/like-rain-1726790452-KAwYXEWC9j.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/755/1000x0/like-rain-1726790452-KAwYXEWC9j.jpg",
        detailUrl = "https://ncs.io/LikeRain",
        genres = setOf(Genre(id = 3, name = "Drum & Bass")),
        moods = setOf(Mood(id = 26, name = "Chasing"), Mood(id = 2, name = "Dark"), Mood(id = 29, name = "Floating"), Mood(id = 14, name = "Mysterious"), Mood(id = 18, name = "Restless"), Mood(id = 16, name = "Quirky")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("b056cd90-89db-44fa-a352-eb0a080afb08"),
        title = "Invincible (Sped Up)",
        artists = listOf(Artist(
            name = "DEAF KEV",
            photoUrl = null,
            detailUrl = "/artist/103/deaf-kev",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-09-20"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/759/invincible-sped-up-1727366457-RfLWTl9BNp.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/759/100x100/invincible-sped-up-1727366455-0LrxIyWqd4.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/759/1000x0/invincible-sped-up-1727366455-0LrxIyWqd4.jpg",
        detailUrl = "https://ncs.io/IV_SU",
        genres = setOf(Genre(id = 12, name = "Melodic Dubstep")),
        moods = setOf(Mood(id = 29, name = "Floating"), Mood(id = 28, name = "Elegant"), Mood(id = 31, name = "Sentimental"), Mood(id = 8, name = "Funny")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("1f8c2129-cf44-4490-8de1-2d71d7a707c5"),
        title = "Invincible (Slowed)",
        artists = listOf(Artist(
            name = "DEAF KEV",
            photoUrl = null,
            detailUrl = "/artist/103/deaf-kev",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-09-20"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/760/invincible-slowed-1727366463-hj0o5nmrgG.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/760/100x100/invincible-slowed-1727366461-vVYEdFUvMf.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/760/1000x0/invincible-slowed-1727366461-vVYEdFUvMf.jpg",
        detailUrl = "https://ncs.io/IV_SL",
        genres = setOf(Genre(id = 12, name = "Melodic Dubstep")),
        moods = setOf(Mood(id = 29, name = "Floating"), Mood(id = 28, name = "Elegant"), Mood(id = 31, name = "Sentimental"), Mood(id = 8, name = "Funny")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("aa10a463-431f-4669-9ec0-f42bdd267b7b"),
        title = "Silence",
        artists = listOf(Artist(
            name = "N3b",
            photoUrl = null,
            detailUrl = "/artist/1174/n3b",
            tags = ""
        ), Artist(
            name = "Extra Terra",
            photoUrl = null,
            detailUrl = "/artist/1175/extra-terra",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-09-19"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/754/silence-1726704056-SKhwVEYNSL.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/754/100x100/silence-1726704054-uoTEHNm0c7.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/754/1000x0/silence-1726704054-uoTEHNm0c7.jpg",
        detailUrl = "https://ncs.io/EN_Silence",
        genres = setOf(Genre(id = 22, name = "Midtempo Bass")),
        moods = setOf(Mood(id = 26, name = "Chasing"), Mood(id = 2, name = "Dark"), Mood(id = 1, name = "Angry"), Mood(id = 27, name = "Eccentric"), Mood(id = 7, name = "Fear"), Mood(id = 14, name = "Mysterious"), Mood(id = 18, name = "Restless"), Mood(id = 23, name = "Suspense")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("ce9fc522-de01-44e4-9dac-8e25e19d0059"),
        title = "Ares (Tear Up My Horizon)",
        artists = listOf(Artist(
            name = "ZAM",
            photoUrl = null,
            detailUrl = "/artist/1171/zam",
            tags = ""
        ), Artist(
            name = "Reverse Prodigy",
            photoUrl = null,
            detailUrl = "/artist/1172/reverse-prodigy",
            tags = ""
        ), Artist(
            name = "Fourier",
            photoUrl = null,
            detailUrl = "/artist/1173/fourier",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-09-17"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/753/ares-tear-up-my-horizon-1726531257-0RV7vjR9l2.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/753/100x100/ares-tear-up-my-horizon-1726531255-ekPiPOYUBh.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/753/1000x0/ares-tear-up-my-horizon-1726531255-ekPiPOYUBh.jpg",
        detailUrl = "https://ncs.io/ATUMH",
        genres = setOf(Genre(id = 33, name = "Alternative Pop")),
        moods = setOf(Mood(id = 26, name = "Chasing"), Mood(id = 2, name = "Dark"), Mood(id = 4, name = "Epic"), Mood(id = 7, name = "Fear"), Mood(id = 30, name = "Heavy"), Mood(id = 14, name = "Mysterious"), Mood(id = 18, name = "Restless"), Mood(id = 23, name = "Suspense"), Mood(id = 24, name = "Weird")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("43175dd8-6fba-4990-8ce9-53ed40875b6d"),
        title = "Party Pioneers",
        artists = listOf(Artist(
            name = "NOYSE",
            photoUrl = null,
            detailUrl = "/artist/875/noyse",
            tags = ""
        ), Artist(
            name = "Rudeejay",
            photoUrl = null,
            detailUrl = "/artist/1170/rudeejay",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-09-13"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/752/party-pioneers-1726185656-2Yf3gOw55u.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/752/100x100/party-pioneers-1726185655-s0bfh1Mbjg.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/752/1000x0/party-pioneers-1726185655-s0bfh1Mbjg.jpg",
        detailUrl = "https://ncs.io/PartyPioneers",
        genres = setOf(Genre(id = 36, name = "Dance-Pop")),
        moods = setOf(Mood(id = 5, name = "Euphoric"), Mood(id = 30, name = "Heavy"), Mood(id = 16, name = "Quirky"), Mood(id = 18, name = "Restless"), Mood(id = 23, name = "Suspense")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("4f57d9f2-a14d-4d08-a13b-661505562985"),
        title = "Lock n' Load",
        artists = listOf(Artist(
            name = "HXI",
            photoUrl = null,
            detailUrl = "/artist/1169/hxi",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-09-12"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/751/lock-n-load-1726099257-4Zci4qJmWw.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/751/100x100/lock-n-load-1726099255-6eKiGFRmjN.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/751/1000x0/lock-n-load-1726099255-6eKiGFRmjN.jpg",
        detailUrl = "https://ncs.io/LockNLoad",
        genres = setOf(Genre(id = 16, name = "Phonk")),
        moods = setOf(Mood(id = 1, name = "Angry"), Mood(id = 2, name = "Dark"), Mood(id = 26, name = "Chasing"), Mood(id = 7, name = "Fear"), Mood(id = 30, name = "Heavy"), Mood(id = 18, name = "Restless"), Mood(id = 23, name = "Suspense")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("341eed67-b9c9-4765-b7e1-37e5bd35107d"),
        title = "RIDE WITH ME",
        artists = listOf(Artist(
            name = "LUKE ALEXANDER",
            photoUrl = null,
            detailUrl = "/artist/1168/luke-alexander",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-09-10"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/749/ride-with-me-1725926456-5kJmxfN93V.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/749/100x100/ride-with-me-1725926454-iHAGikW6lx.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/749/1000x0/ride-with-me-1725926454-iHAGikW6lx.jpg",
        detailUrl = "https://ncs.io/RWM",
        genres = setOf(Genre(id = 51, name = "Garage")),
        moods = setOf(Mood(id = 3, name = "Dreamy"), Mood(id = 26, name = "Chasing"), Mood(id = 29, name = "Floating"), Mood(id = 28, name = "Elegant"), Mood(id = 18, name = "Restless")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("006a71a3-a320-4dff-bdec-bb43bd4219b9"),
        title = "Heroes Tonight x Dreams pt. II Mashup",
        artists = listOf(Artist(
            name = "Sara Skinner",
            photoUrl = null,
            detailUrl = "/artist/395/sara-skinner",
            tags = ""
        ), Artist(
            name = "Lost Sky",
            photoUrl = null,
            detailUrl = "/artist/279/lost-sky",
            tags = ""
        ), Artist(
            name = "Johnning",
            photoUrl = null,
            detailUrl = "/artist/219/johnning",
            tags = ""
        ), Artist(
            name = "Janji",
            photoUrl = null,
            detailUrl = "/artist/198/janji",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-09-08"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/750/heroes-tonight-x-dreams-pt-ii-mashup-1725969652-gPmvlvBBTP.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/750/100x100/heroes-tonight-x-dreams-pt-ii-mashup-1725969651-F2Ho14TMDA.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/750/1000x0/heroes-tonight-x-dreams-pt-ii-mashup-1725969651-F2Ho14TMDA.jpg",
        detailUrl = "https://ncs.io/HTD",
        genres = setOf(Genre(id = 14, name = "Trap")),
        moods = setOf(Mood(id = 4, name = "Epic"), Mood(id = 5, name = "Euphoric"), Mood(id = 11, name = "Happy"), Mood(id = 18, name = "Restless")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("2906a469-1aab-423d-b4bc-1df344acba7b"),
        title = "Look No Further (ft Grace Luisa)",
        artists = listOf(Artist(
            name = "Grace Luisa",
            photoUrl = null,
            detailUrl = "/artist/1166/grace-luisa",
            tags = ""
        ), Artist(
            name = "Madison Mars",
            photoUrl = null,
            detailUrl = "/artist/1167/madison-mars",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-09-06"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/748/look-no-further-ft-grace-luisa-1725580855-UU82B1Ixlx.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/748/100x100/look-no-further-ft-grace-luisa-1725580853-htXRq9BJrb.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/748/1000x0/look-no-further-ft-grace-luisa-1725580853-htXRq9BJrb.jpg",
        detailUrl = "https://ncs.io/LookNoFurther",
        genres = setOf(Genre(id = 3, name = "Drum & Bass")),
        moods = setOf(Mood(id = 3, name = "Dreamy"), Mood(id = 28, name = "Elegant"), Mood(id = 29, name = "Floating"), Mood(id = 9, name = "Glamorous"), Mood(id = 13, name = "Laid Back"), Mood(id = 19, name = "romantic"), Mood(id = 31, name = "Sentimental"), Mood(id = 22, name = "Sexy")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("4ae31905-1850-4ed7-ad21-f35820a16ae4"),
        title = "Models",
        artists = listOf(Artist(
            name = "Hush",
            photoUrl = null,
            detailUrl = "/artist/625/hush",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-09-05"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/747/models-1725494457-HAp6uMRH2l.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/747/100x100/models-1725494456-yA4G6tIanw.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/747/1000x0/models-1725494456-yA4G6tIanw.jpg",
        detailUrl = "https://ncs.io/Models",
        genres = setOf(Genre(id = 5, name = "Dubstep")),
        moods = setOf(Mood(id = 27, name = "Eccentric"), Mood(id = 30, name = "Heavy"), Mood(id = 14, name = "Mysterious"), Mood(id = 16, name = "Quirky"), Mood(id = 18, name = "Restless"), Mood(id = 23, name = "Suspense"), Mood(id = 24, name = "Weird")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("9d290f9a-09c7-4067-bfdf-130e2e192297"),
        title = "Home",
        artists = listOf(Artist(
            name = "Robbie Mendez",
            photoUrl = null,
            detailUrl = "/artist/1165/robbie-mendez",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-09-03"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/746/home-1725372056-sUBZMDMTgs.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/746/100x100/home-1725372052-GWRK7fgqwq.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/746/1000x0/home-1725372052-GWRK7fgqwq.jpg",
        detailUrl = "https://ncs.io/RM_Home",
        genres = setOf(Genre(id = 10, name = "House")),
        moods = setOf(Mood(id = 3, name = "Dreamy"), Mood(id = 5, name = "Euphoric"), Mood(id = 16, name = "Quirky"), Mood(id = 18, name = "Restless"), Mood(id = 23, name = "Suspense")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("d835a7f7-b54a-432f-ac18-904150046caa"),
        title = "Why So Serious",
        artists = listOf(Artist(
            name = "Eytan Peled",
            photoUrl = null,
            detailUrl = "/artist/1163/eytan-peled",
            tags = ""
        ), Artist(
            name = "Roby Fayer",
            photoUrl = null,
            detailUrl = "/artist/1164/roby-fayer",
            tags = ""
        ), Artist(
            name = "Sync",
            photoUrl = null,
            detailUrl = "/artist/1047/sync",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-08-29"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/745/why-so-serious-1724889655-g87mRygxzb.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/745/100x100/why-so-serious-1724889654-6Hs7AZkTlT.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/745/1000x0/why-so-serious-1724889654-6Hs7AZkTlT.jpg",
        detailUrl = "https://ncs.io/WSS",
        genres = setOf(Genre(id = 39, name = "Electronic Pop")),
        moods = setOf(Mood(id = 27, name = "Eccentric"), Mood(id = 28, name = "Elegant"), Mood(id = 9, name = "Glamorous"), Mood(id = 13, name = "Laid Back"), Mood(id = 16, name = "Quirky"), Mood(id = 18, name = "Restless")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("d692cb61-1ca1-46a2-88a3-4bfb77876531"),
        title = "Win & Lose",
        artists = listOf(Artist(
            name = "Chime",
            photoUrl = null,
            detailUrl = "/artist/78/chime",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-08-27"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/744/win-lose-1724716855-av5xiHP4Am.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/744/100x100/win-lose-1724716853-eU08vmIL7s.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/744/1000x0/win-lose-1724716853-eU08vmIL7s.jpg",
        detailUrl = "https://ncs.io/WinAndLose",
        genres = setOf(Genre(id = 12, name = "Melodic Dubstep")),
        moods = setOf(Mood(id = 3, name = "Dreamy"), Mood(id = 9, name = "Glamorous"), Mood(id = 11, name = "Happy"), Mood(id = 13, name = "Laid Back"), Mood(id = 19, name = "romantic"), Mood(id = 31, name = "Sentimental")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("c8d3f6a0-2b30-492f-b085-3aaef873c30d"),
        title = "Wonder (ft. Rarin & Bri Tolani)",
        artists = listOf(Artist(
            name = "Rarin",
            photoUrl = null,
            detailUrl = "/artist/1162/rarin",
            tags = ""
        ), Artist(
            name = "Bri Tolani",
            photoUrl = null,
            detailUrl = "/artist/61/bri-tolani",
            tags = ""
        ), Artist(
            name = "Unknown Brain",
            photoUrl = null,
            detailUrl = "/artist/466/unknown-brain",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-08-23"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/743/wonder-ft-rarin-bri-tolani-1724371256-h6oXol1wF2.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/743/100x100/wonder-ft-rarin-bri-tolani-1724371254-EijOHavsBb.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/743/1000x0/wonder-ft-rarin-bri-tolani-1724371254-EijOHavsBb.jpg",
        detailUrl = "https://ncs.io/Wonder",
        genres = setOf(Genre(id = 14, name = "Trap")),
        moods = setOf(Mood(id = 3, name = "Dreamy"), Mood(id = 27, name = "Eccentric"), Mood(id = 28, name = "Elegant"), Mood(id = 30, name = "Heavy"), Mood(id = 12, name = "Hopeful"), Mood(id = 18, name = "Restless"), Mood(id = 31, name = "Sentimental"), Mood(id = 9, name = "Glamorous")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("62aeefbf-66bf-4c43-9bd6-7be6cb32d599"),
        title = "AIN'T MISS A CALL",
        artists = listOf(Artist(
            name = "Guy Arthur",
            photoUrl = null,
            detailUrl = "/artist/521/guy-arthur",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-08-22"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/742/aint-miss-a-call-1724284856-tqZRznMNUU.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/742/100x100/aint-miss-a-call-1724284854-9vROhK0KyQ.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/742/1000x0/aint-miss-a-call-1724284854-9vROhK0KyQ.jpg",
        detailUrl = "https://ncs.io/AMAC",
        genres = setOf(Genre(id = 57, name = "Future Trap")),
        moods = setOf(Mood(id = 26, name = "Chasing"), Mood(id = 27, name = "Eccentric"), Mood(id = 9, name = "Glamorous"), Mood(id = 18, name = "Restless"), Mood(id = 24, name = "Weird")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("b283acf8-c8e4-44b8-b74a-d5192cbb8582"),
        title = "Earthquake (feat. Justin J. Moore)",
        artists = listOf(Artist(
            name = "Justin J. Moore",
            photoUrl = null,
            detailUrl = "/artist/1144/justin-j-moore",
            tags = ""
        ), Artist(
            name = "SNAILS",
            photoUrl = null,
            detailUrl = "/artist/1160/snails",
            tags = ""
        ), Artist(
            name = "ESCARGOT",
            photoUrl = null,
            detailUrl = "/artist/1161/escargot",
            tags = ""
        ), Artist(
            name = "Jay Eskar",
            photoUrl = null,
            detailUrl = "/artist/736/jay-eskar",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-08-20"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/740/earthquake-feat-justin-j-moore-1724112056-47MvZZyqAh.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/740/100x100/earthquake-feat-justin-j-moore-1724112054-G6qcggrrg5.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/740/1000x0/earthquake-feat-justin-j-moore-1724112054-G6qcggrrg5.jpg",
        detailUrl = "https://ncs.io/SJ_Earthquake",
        genres = setOf(Genre(id = 8, name = "Future House")),
        moods = setOf(Mood(id = 29, name = "Floating"), Mood(id = 14, name = "Mysterious"), Mood(id = 2, name = "Dark"), Mood(id = 30, name = "Heavy"), Mood(id = 18, name = "Restless"), Mood(id = 23, name = "Suspense")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("a8658055-6350-46fc-b0be-6a7d9710f104"),
        title = "Pump Up The Bassline",
        artists = listOf(Artist(
            name = "Sippy",
            photoUrl = null,
            detailUrl = "/artist/1159/sippy",
            tags = ""
        ), Artist(
            name = "RIOT",
            photoUrl = null,
            detailUrl = "/artist/1120/riot",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-08-16"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/739/pump-up-the-bassline-1723766458-cYyol13E96.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/739/100x100/pump-up-the-bassline-1723766456-kcVAcn4OHx.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/739/1000x0/pump-up-the-bassline-1723766456-kcVAcn4OHx.jpg",
        detailUrl = "https://ncs.io/PUTB",
        genres = setOf(Genre(id = 5, name = "Dubstep")),
        moods = setOf(Mood(id = 30, name = "Heavy"), Mood(id = 18, name = "Restless"), Mood(id = 16, name = "Quirky"), Mood(id = 23, name = "Suspense"), Mood(id = 24, name = "Weird")),
        versions = setOf(),
        status = MusicStatus.None
    ),
    Music(
        id = UUID.fromString("6cd2e920-4901-448e-9630-dddaa78fc7cf"),
        title = "EXECUTIONER",
        artists = listOf(Artist(
            name = "DJ FKU",
            photoUrl = null,
            detailUrl = "/artist/1130/dj-fku",
            tags = ""
        )),
        releaseDate = LocalDate.parse("2024-08-15"),
        dataUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/738/1723718204_7CFlBpXrJX_01-DJ-FKU---EXECUTIONER-NCS-Release.mp3",
        coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/738/100x100/executioner-1723680055-GZacfkgNhO.jpg",
        coverUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/738/1000x0/executioner-1723680055-GZacfkgNhO.jpg",
        detailUrl = "https://ncs.io/EXECUTIONER",
        genres = setOf(Genre(id = 16, name = "Phonk")),
        moods = setOf(Mood(id = 2, name = "Dark"), Mood(id = 7, name = "Fear"), Mood(id = 26, name = "Chasing"), Mood(id = 14, name = "Mysterious"), Mood(id = 18, name = "Restless")),
        versions = setOf(),
        status = MusicStatus.None
    ),
)

val MockMusicWithGenreAndMoodList: List<MusicWithGenreAndMood> = MockMusicList.map { music ->
    MusicWithGenreAndMood(
        music = music.asEntity(),
        genres = music.genres.map { it.asEntity() }.toSet(),
        moods = music.moods.map { it.asEntity() }.toSet()
    )
}