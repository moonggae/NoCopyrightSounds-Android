package com.ccc.ncs.playback.session

import android.content.Intent
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.ccc.ncs.cache.CacheManager
import com.ccc.ncs.data.repository.MusicRepository
import com.ccc.ncs.model.MusicStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : MediaLibraryService() {

    @Inject
    lateinit var session: MediaLibrarySession

    @Inject
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var musicRepository: MusicRepository

    @Inject
    lateinit var ioDispatcher: CoroutineDispatcher

    override fun onTaskRemoved(rootIntent: Intent?) {
        if (!player.playWhenReady
            || player.mediaItemCount == 0
            || player.playbackState == Player.STATE_ENDED
        ) {
            release()
            stopSelf()
        }
    }

    override fun onCreate() {
        super.onCreate()
        addPlayerCacheStatusUpdateListener()
    }

    private fun addPlayerCacheStatusUpdateListener() {
        player.addListener(object : Player.Listener {
            @OptIn(UnstableApi::class)
            override fun onMediaItemTransition(nullableMediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(nullableMediaItem, reason)
                nullableMediaItem?.let { mediaItem ->
                    val uriScheme = mediaItem.localConfiguration?.uri?.scheme?.lowercase()
                    val isNetworkMusic = uriScheme == "http" || uriScheme == "https"

                    Log.d("TAG", "onMediaItemTransition - mediaItem.localConfiguration?.uri: ${mediaItem.localConfiguration?.uri}")
                    Log.d("TAG", "onMediaItemTransition - mediaItem.mediaId: ${mediaItem.mediaId}")
                    Log.d("TAG", "onMediaItemTransition - mediaItem.mediaMetadata.title: ${mediaItem.mediaMetadata.title}")
                    Log.d("TAG", "onMediaItemTransition - isNetworkMusic: ${isNetworkMusic}")

                    CacheManager.clearOnCacheUpdateListener()

                    if (isNetworkMusic) {
                        CacheManager.cache.checkInitialization()
                        CacheManager.addOnCacheUpdateListener(mediaItem.mediaId) { isFullyCached: Boolean ->
                            CoroutineScope(ioDispatcher).launch {
                                musicRepository.updateMusicStatus(
                                    musicId = UUID.fromString(mediaItem.mediaId),
                                    status = if (isFullyCached) MusicStatus.FullyCached else MusicStatus.PartiallyCached
                                )
                            }
                        }
                    }
                }
            }
        })
    }

    private fun release() {
        player.release()
        session.release()
        scope.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession = session
}