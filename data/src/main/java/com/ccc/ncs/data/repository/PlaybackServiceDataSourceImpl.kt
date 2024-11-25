package com.ccc.ncs.data.repository

import com.ccc.ncs.domain.repository.MusicRepository
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.MusicStatus
import com.ccc.ncs.playback.data.PlaybackServiceDataSource
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class PlaybackServiceDataSourceImpl @Inject constructor(
    private val musicRepository: MusicRepository
): PlaybackServiceDataSource {
    override suspend fun updateMusicStatus(musicId: UUID, status: MusicStatus) =
        musicRepository.updateMusicStatus(musicId, status)

    override suspend fun getMusic(musicId: UUID): Music? =
        musicRepository.getMusic(musicId).first()

    override suspend fun deleteMusic(musicId: UUID) =
        musicRepository.deleteMusic(musicId)
}