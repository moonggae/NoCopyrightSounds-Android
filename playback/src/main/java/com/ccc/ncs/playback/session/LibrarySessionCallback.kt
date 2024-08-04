package com.ccc.ncs.playback.session

import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.MediaItemsWithStartPosition
import com.ccc.ncs.data.repository.PlayerRepository
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.guava.future
import javax.inject.Inject


@UnstableApi
internal class LibrarySessionCallback @Inject constructor(
    private val scope: CoroutineScope,
    private val playerRepository: PlayerRepository
) : MediaLibrarySession.Callback {
    override fun onPlaybackResumption(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo
    ): ListenableFuture<MediaItemsWithStartPosition> {
        return scope.future {
            val mediaItems = playerRepository.playlist.first()?.musics?.map { it.asMediaItem() } ?: emptyList()
            val startIndex = playerRepository.musicIndex.first() ?: C.INDEX_UNSET
            val startPosition = playerRepository.position.first() ?: C.TIME_UNSET
            MediaItemsWithStartPosition(
                mediaItems,
                startIndex,
                startPosition
            )
        }
    }
}