package com.ccc.ncs.playback.session

import android.content.Intent
import android.util.Log
import androidx.annotation.OptIn
import androidx.core.net.toFile
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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


    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(serviceJob + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        setupPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        player.release()
        session.release()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        if (!player.playWhenReady
            || player.mediaItemCount == 0
            || player.playbackState == Player.STATE_ENDED
        ) {
            release()
            stopSelf()
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession = session

    private fun release() {
        player.release()
        session.release()
        scope.cancel()
    }

    private fun setupPlayer() {
        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                mediaItem?.let { handleMediaItemTransition(it) }
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                if (error.errorCode == PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS) {
                    handleNetworkMusicBadStatus()
                }
            }
        })
    }

    private fun handleMediaItemTransition(mediaItem: MediaItem) {
        CacheManager.clearOnCacheUpdateListener()

        if (mediaItem.isNetworkSource) {
            setupCacheListener(mediaItem)
        } else {
            checkLocalFileExistence(mediaItem)
        }
    }

    @OptIn(UnstableApi::class)
    private fun setupCacheListener(mediaItem: MediaItem) {
        CacheManager.addOnCacheUpdateListener(mediaItem.mediaId) { isFullyCached ->
            serviceScope.launch(ioDispatcher) {
                musicRepository.updateMusicStatus(
                    musicId = UUID.fromString(mediaItem.mediaId),
                    status = if (isFullyCached) MusicStatus.FullyCached else MusicStatus.PartiallyCached
                )
            }
        }
    }

    private fun checkLocalFileExistence(mediaItem: MediaItem) {
        mediaItem.localConfiguration?.uri?.let { uri ->
            serviceScope.launch(ioDispatcher) {
                if (!uri.toFile().exists()) {
                    handleMissingLocalFile(mediaItem)
                }
            }
        }
    }

    private suspend fun handleMissingLocalFile(mediaItem: MediaItem) {
        val currentMusicId = UUID.fromString(mediaItem.mediaId)
        musicRepository.updateMusicStatus(currentMusicId, MusicStatus.None)
        musicRepository.getMusic(currentMusicId).firstOrNull()?.let { musicItem ->
            withContext(Dispatchers.Main) {
                player.replaceMediaItem(player.currentMediaItemIndex, musicItem.asMediaItem())
            }
        }
    }

    private fun handleNetworkMusicBadStatus() {
        serviceScope.launch {
            player.currentMediaItem?.let { mediaItem ->
                val wasPlaying = player.playWhenReady
                Log.d("TAG", "handleNetworkMusicBadStatus - player.playWhenReady: ${player.playWhenReady}")
                Log.d("TAG", "handleNetworkMusicBadStatus - player.playbackState: ${player.playbackState}")

                // 재생중:     true  STATE_IDLE
                // not 재생중: false STATE_IDLE

                musicRepository.deleteMusic(UUID.fromString(mediaItem.mediaId))
                withContext(Dispatchers.Main) {
                    player.removeMediaItem(player.currentMediaItemIndex)
                    if (wasPlaying) {
                        player.prepare()
                        player.play()
                    }
                }
            }
        }
    }
}