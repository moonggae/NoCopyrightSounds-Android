package com.ccc.ncs.domain.usecase

import com.ccc.ncs.domain.MediaPlaybackController
import com.ccc.ncs.domain.repository.PlayListRepository
import com.ccc.ncs.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.first
import java.util.UUID

class DeletePlaylistUseCase(
    private val playlistRepository: PlayListRepository,
    private val playerRepository: PlayerRepository,
    private val playbackController: MediaPlaybackController
) {
    suspend operator fun invoke(playlistId: UUID): Result<Unit> = runCatching {
        playlistRepository.deletePlayList(playlistId)

        if (playerRepository.playlist.first()?.id == playlistId) {
            playbackController.stop()
            playerRepository.clear()
        }
    }
}