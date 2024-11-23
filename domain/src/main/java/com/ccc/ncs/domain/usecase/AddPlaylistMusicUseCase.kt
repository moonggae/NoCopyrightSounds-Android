package com.ccc.ncs.domain.usecase

import com.ccc.ncs.domain.MediaPlaybackController
import com.ccc.ncs.domain.repository.PlayListRepository
import com.ccc.ncs.domain.repository.PlayerRepository
import com.ccc.ncs.model.Music
import kotlinx.coroutines.flow.first
import java.util.UUID

class AddPlaylistMusicUseCase (
    private val playlistRepository: PlayListRepository,
    private val playerRepository: PlayerRepository,
    private val playbackController: MediaPlaybackController
) {
    suspend operator fun invoke(playlistId: UUID, musicIds: List<UUID>) = runCatching {
        val playlist = playlistRepository.getPlayList(playlistId).first()
            ?: throw Exception("Playlist not found")

        val currentMusicIds = playlist.musics.map(Music::id)
        val appendMusicIds = currentMusicIds + (musicIds - currentMusicIds.toSet())
        playlistRepository.setPlayListMusicsWithId(playlist.id, appendMusicIds)

        playerRepository.playlist.first()?.id?.let { currentPlaylistId ->
            if (currentPlaylistId == playlist.id) {
                playlistRepository.getPlayList(playlistId).first()?.musics?.filter {
                    it.id in appendMusicIds
                }?.let { appendedMusics ->
                    playbackController.appendMusics(appendedMusics)
                }
            }
        }
    }
}