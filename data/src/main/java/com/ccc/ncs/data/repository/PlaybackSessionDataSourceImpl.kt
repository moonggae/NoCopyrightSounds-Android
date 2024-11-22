package com.ccc.ncs.data.repository

import com.ccc.ncs.domain.repository.PlayerRepository
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.playback.data.PlaybackSessionDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PlaybackSessionDataSourceImpl @Inject constructor(
    private val playerRepository: PlayerRepository
): PlaybackSessionDataSource {
    override suspend fun getCurrentPlaylist(): PlayList? =
        playerRepository.playlist.first()

    override suspend fun getCurrentMusicIndex(): Int? =
        playerRepository.musicIndex.first()

    override suspend fun getCurrentPosition(): Long? =
        playerRepository.position.first()
}