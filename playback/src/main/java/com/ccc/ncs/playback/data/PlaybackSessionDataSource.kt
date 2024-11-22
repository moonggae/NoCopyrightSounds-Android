package com.ccc.ncs.playback.data

import com.ccc.ncs.model.PlayList


interface PlaybackSessionDataSource {
    suspend fun getCurrentPlaylist(): PlayList?
    suspend fun getCurrentMusicIndex(): Int?
    suspend fun getCurrentPosition(): Long?
}