package com.ccc.ncs.domain.usecase

import com.ccc.ncs.domain.MediaPlaybackController
import com.ccc.ncs.domain.repository.PlayListRepository
import com.ccc.ncs.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.first
import java.util.UUID

class PlayPlaylistUseCase(
    private val playlistRepository: PlayListRepository,
    private val playerRepository: PlayerRepository,
    private val playbackController: MediaPlaybackController
) {
    suspend operator fun invoke(playlistId: UUID, startIndex: Int = 0): Result<Unit> = runCatching {
        val playlist = playlistRepository.getPlayList(playlistId).first()
            ?: throw IllegalStateException("Playlist not found")

        playerRepository.setPlaylist(playlist.id)
        playerRepository.updateMusicIndex(startIndex)
        playbackController.playMusics(playlist.musics, startIndex)
    }
}