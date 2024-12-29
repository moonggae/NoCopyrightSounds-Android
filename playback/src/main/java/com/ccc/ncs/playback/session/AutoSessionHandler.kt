package com.ccc.ncs.playback.session

import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import com.ccc.ncs.domain.repository.PlayListRepository
import com.ccc.ncs.domain.usecase.PlayPlaylistUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

internal class AutoSessionHandler @Inject constructor(
    private val scope: CoroutineScope,
    private val playlistRepository: PlayListRepository,
    private val playPlaylistUseCase: PlayPlaylistUseCase
) {
    private var currentPlaylistId: UUID? = null

    fun handleAutoMediaItems(mediaItems: MutableList<MediaItem>) {
        currentPlaylistId?.let { playlistId ->
            scope.launch {
                playlistRepository.getPlayList(playlistId).first()?.let { playlist ->
                    playlist.musics.indexOfFirst { it.id.toString() == mediaItems.first().mediaId }
                        .takeIf { it > -1 }
                        ?.let { index ->
                            playPlaylistUseCase(playlist.id, index)
                        }
                }
            }
        }
    }

    fun setPlaylistId(
        mediaId: String
    ) {
        currentPlaylistId = mediaId.toUUID()
    }

    @OptIn(UnstableApi::class)
    fun isAutoController(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): Boolean {
        return session.isAutomotiveController(controller) || session.isAutoCompanionController(controller)
    }
}