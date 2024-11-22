package com.ccc.ncs.domain.model

import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList

data class PlayerState(
    val playlist: PlayList,
    val lyrics: String? = null,
    val playbackState: PlaybackState
) {
    val currentMusic: Music?
        get() = playlist.musics.getOrNull(playbackState.currentIndex)
}