package com.ccc.ncs.model

data class ArtistDetail(
    val artist: Artist,
    val musics: List<Music>,
    val similarArtists: List<Artist>
)