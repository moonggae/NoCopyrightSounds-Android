package com.ccc.ncs.playback.session

import android.content.Intent
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.ccc.ncs.cache.CacheManager
import com.ccc.ncs.domain.repository.MusicRepository
import com.ccc.ncs.model.MusicStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : MediaLibraryService() {

    @Inject
    lateinit var session: MediaLibrarySession

    @Inject
    lateinit var musicRepository: MusicRepository

    @Inject
    lateinit var cacheManager: CacheManager


    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(serviceJob + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        setupPlayer()
    }

    override fun onDestroy() {
        session.player.release()
        session.release()
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        session.player.pause()
        session.player.stop()
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession = session

    @OptIn(UnstableApi::class)
    private fun setupPlayer() {
        // TODO: isCurrentMediaItemLive true 일 때 처리, 상태 update 되는 시점 찾기
        session.player.addListener(object : Player.Listener {
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
        if (cacheManager.enableCache) {
            cacheManager.clearOnCacheUpdateListener()

            if (mediaItem.isNetworkSource) {
                setupCacheListener(mediaItem)
                return
            }
        }

        if (!mediaItem.isNetworkSource && !mediaItem.isLocalFileExists) {
            handleMissingLocalFile(mediaItem)
        }
    }

    @OptIn(UnstableApi::class)
    fun setupCacheListener(mediaItem: MediaItem) {
        cacheManager.addOnCacheUpdateListener(
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
        musicRepository.getMusic(currentMusicId).first()?.let { musicItem ->
            withContext(Dispatchers.Main) {
                session.player.replaceMediaItem(session.player.currentMediaItemIndex, musicItem.asMediaItem())
            }
        }
    }

    private suspend fun handleNetworkMusicBadStatus() {
        session.player.currentMediaItem?.let { mediaItem ->
            val wasPlaying = session.player.playWhenReady
            Log.d("TAG", "handleNetworkMusicBadStatus - session.player.playWhenReady: ${session.player.playWhenReady}")
            Log.d("TAG", "handleNetworkMusicBadStatus - session.player.playbackState: ${session.player.playbackState}")

            // 재생중:     true  STATE_IDLE
            // not 재생중: false STATE_IDLE

            musicRepository.deleteMusic(UUID.fromString(mediaItem.mediaId))
            withContext(Dispatchers.Main) {
                session.player.removeMediaItem(session.player.currentMediaItemIndex)
                if (wasPlaying) {
                    session.player.prepare()
                    session.player.play()
                }
            }
        }
    }

    companion object {
        private const val TAG = "PlaybackService"
    }
}