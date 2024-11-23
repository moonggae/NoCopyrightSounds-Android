package com.ccc.ncs.domain.usecase

import com.ccc.ncs.domain.MediaPlaybackController
import com.ccc.ncs.domain.repository.PlayListRepository
import com.ccc.ncs.domain.repository.PlayerRepository
import com.ccc.ncs.model.util.reorder
import kotlinx.coroutines.flow.first
import java.util.UUID

class UpdatePlaylistMusicOrderUseCase(
    private val playlistRepository: PlayListRepository,
    private val playerRepository: PlayerRepository,
    private val playbackController: MediaPlaybackController,
) {
    suspend operator fun invoke(playlistId: UUID, from: Int, to: Int): Result<Unit> = runCatching {
        val playlist = playlistRepository.getPlayList(playlistId).first()
            ?: throw IllegalStateException("Playlist not found")

        val isCurrentPlaylist = playlist.id == playerRepository.playlist.first()?.id
        val playingIndex = if (isCurrentPlaylist) playerRepository.musicIndex.first() else null
        val playingMusicId = playlist.musics.getOrNull(playingIndex ?: -1)?.id

        val reorderedMusics = playlist.musics.reorder(from, to)
        playlistRepository.setPlayListMusics(playlistId, reorderedMusics)

        if (isCurrentPlaylist) {
            playbackController.moveMediaItem(from, to)

            val newIndex = reorderedMusics.indexOfFirst { it.id == playingMusicId }
            if (newIndex >= 0) {
                playerRepository.updateMusicIndex(newIndex)
            }
        }
    }
}