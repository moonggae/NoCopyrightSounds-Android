package com.ccc.ncs.playback.session

import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.MediaItemsWithStartPosition
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.guava.future
import javax.inject.Inject


@UnstableApi
internal class LibrarySessionCallback @Inject constructor(
    private val scope: CoroutineScope
) : MediaLibrarySession.Callback {
    override fun onPlaybackResumption(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo
    ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
        return scope.future {// todo
            MediaItemsWithStartPosition(
                listOfNotNull(mediaSession.player.currentMediaItem),
                C.INDEX_UNSET,
                C.TIME_UNSET
            )
        }
    }
}