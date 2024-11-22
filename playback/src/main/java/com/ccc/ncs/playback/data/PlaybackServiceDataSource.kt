package com.ccc.ncs.playback.data

import com.ccc.ncs.model.Music
import com.ccc.ncs.model.MusicStatus
import java.util.UUID

interface PlaybackServiceDataSource {
    suspend fun updateMusicStatus(musicId: UUID, status: MusicStatus)
    suspend fun getMusic(musicId: UUID): Music?
    suspend fun deleteMusic(musicId: UUID)
}