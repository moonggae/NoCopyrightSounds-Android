package com.ccc.ncs.data.repository

import com.ccc.ncs.datastore.PlayerDataStore
import com.ccc.ncs.domain.repository.PlayListRepository
import com.ccc.ncs.domain.repository.PlayerRepository
import com.ccc.ncs.model.PlayList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.util.UUID
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
internal class DefaultPlayerRepository @Inject constructor(
    private val playlistRepository: PlayListRepository,
    private val playerDataSource: PlayerDataStore
): PlayerRepository {
    override val playlist: Flow<PlayList?>
        get() = playerDataSource.playlistId.flatMapLatest { playlistId ->
            if (playlistId == null) flowOf(null)
            else playlistRepository.getPlayList(UUID.fromString(playlistId))
        }

    override suspend fun setPlaylist(playlistId: UUID) {
        playerDataSource.setPlaylistId(playlistId)
    }

    override val musicIndex: Flow<Int?>
        get() = playerDataSource.currentMusicIndex

    override suspend fun updateMusicIndex(index: Int) {
        playerDataSource.setCurrentMusicIndex(index)
    }

    override val position: Flow<Long?>
        get() = playerDataSource.position

    override suspend fun updatePosition(position: Long) {
        playerDataSource.setPosition(position)
    }

    override suspend fun clear() {
        playerDataSource.clear()
    }
}