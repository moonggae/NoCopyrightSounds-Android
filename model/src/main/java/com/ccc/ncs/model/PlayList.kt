package com.ccc.ncs.model

import java.util.UUID

data class PlayList(
    val id: UUID,
    val name: String,
    val musics: List<Music>
)
