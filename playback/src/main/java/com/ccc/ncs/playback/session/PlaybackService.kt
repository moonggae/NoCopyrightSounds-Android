package com.ccc.ncs.playback.session

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.ccc.ncs.playback.listener.PlaybackIOHandler
import com.ccc.ncs.playback.listener.PlaybackStateHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : MediaLibraryService() {

    @Inject
    lateinit var session: MediaLibrarySession

    @Inject
    internal lateinit var playbackStateHandler: PlaybackStateHandler

    @Inject
    internal lateinit var playbackIOHandler: PlaybackIOHandler

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
        playbackStateHandler.attachTo(session.player)
        playbackIOHandler.attachTo(session.player)
    }


    companion object {
        private const val TAG = "PlaybackService"
    }
}