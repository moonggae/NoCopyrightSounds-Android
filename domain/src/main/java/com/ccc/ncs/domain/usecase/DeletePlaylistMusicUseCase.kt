package com.ccc.ncs.domain.usecase

import com.ccc.ncs.domain.MediaPlaybackController
import com.ccc.ncs.domain.repository.PlayListRepository
import com.ccc.ncs.domain.repository.PlayerRepository
import com.ccc.ncs.model.Music
import kotlinx.coroutines.flow.first
import java.util.UUID

class DeletePlaylistMusicUseCase(
    private val playlistRepository: PlayListRepository,
    private val playerRepository: PlayerRepository,
    private val playbackController: MediaPlaybackController
) {
    suspend operator fun invoke(playlistId: UUID, music: Music): Result<Unit> = runCatching {
        val updatedMusics = playlistRepository.getPlayList(playlistId).first()?.musics?.minus(music)
            ?: throw IllegalStateException("Playlist not found")

        playlistRepository.setPlayListMusics(playlistId, updatedMusics)

        if (playlistId == playerRepository.playlist.first()?.id) {
            playbackController.removeMusic(music)

            if (updatedMusics.isEmpty()) {
                playerRepository.clear()
            }
        }
    }
}