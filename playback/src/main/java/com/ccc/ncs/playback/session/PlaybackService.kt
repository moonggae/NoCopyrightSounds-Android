package com.ccc.ncs.playback.session

import android.content.Intent
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.ccc.ncs.cache.di.CacheManager
import com.ccc.ncs.data.repository.MusicRepository
import com.ccc.ncs.model.MusicStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
    lateinit var player: ExoPlayer

    @Inject
    lateinit var musicRepository: MusicRepository


    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(serviceJob + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        setupPlayer()
    }

    override fun onDestroy() {
        release()
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        release()
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession = session

    private fun release() {
        try {
            player.pause()
            session.release()
            player.release()
            serviceJob.cancel()
        } catch (e: Exception) {
            Log.e(TAG, "release", e)
        }
    }

    private fun setupPlayer() {
        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                mediaItem?.let {
                    serviceScope.launch {
                        handleMediaItemTransition(it)
                    }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                if (error.errorCode == PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS) {
                    serviceScope.launch {
                        handleNetworkMusicBadStatus()
                    }
                }
            }
        })
    }

    private suspend fun handleMediaItemTransition(mediaItem: MediaItem) {
        CacheManager.clearOnCacheUpdateListener()

        if (mediaItem.isNetworkSource) {
            setupCacheListener(mediaItem)
        } else {
            if (!mediaItem.isLocalFileExists) {
                handleMissingLocalFile(mediaItem)
            }
        }
    }

    @OptIn(UnstableApi::class)
    suspend fun setupCacheListener(mediaItem: MediaItem) {
        CacheManager.addOnCacheUpdateListener(
            key = mediaItem.mediaId,
            scope = serviceScope
        ) { isFullyCached ->
            musicRepository.updateMusicStatus(
                musicId = UUID.fromString(mediaItem.mediaId),
                status = if (isFullyCached) MusicStatus.FullyCached else MusicStatus.PartiallyCached
            )
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

    private suspend fun handleNetworkMusicBadStatus() {
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

    companion object {
        private const val TAG = "PlaybackService"
    }
}