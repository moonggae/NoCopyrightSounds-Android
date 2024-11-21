package com.ccc.ncs.domain.repository

import com.ccc.ncs.model.PlayList
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface PlayerRepository {
    val playlist: Flow<PlayList?>

    suspend fun setPlaylist(playlistId: UUID)

    val musicIndex: Flow<Int?>

    suspend fun updateMusicIndex(index: Int)

    val position: Flow<Long?>

    suspend fun updatePosition(position: Long)

    suspend fun clear()
}