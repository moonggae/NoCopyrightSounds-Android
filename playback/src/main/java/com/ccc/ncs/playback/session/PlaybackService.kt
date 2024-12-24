package com.ccc.ncs.playback.session

import android.app.PendingIntent
import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.ccc.ncs.playback.listener.PlaybackAnalyticsEventListener
import com.ccc.ncs.playback.listener.PlaybackIOHandler
import com.ccc.ncs.playback.listener.PlaybackStateHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : MediaLibraryService() {

    lateinit var session: MediaLibrarySession

    @Inject
    internal lateinit var playbackStateHandler: PlaybackStateHandler

    @Inject
    internal lateinit var playbackIOHandler: PlaybackIOHandler

    @Inject
    internal lateinit var playbackAnalyticsEventListener: PlaybackAnalyticsEventListener

    @Inject
    internal lateinit var player: ExoPlayer

    @OptIn(UnstableApi::class)
    @Inject
    internal lateinit var callback: LibrarySessionCallback

    @Inject
    internal lateinit var sessionActivity: PendingIntent

    override fun onCreate() {
        super.onCreate()
        session = MediaLibrarySession
            .Builder(this, player, callback)
            .setSessionActivity(sessionActivity)
            .build()

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
        (session.player as? ExoPlayer)?.apply {
            playbackAnalyticsEventListener.attach(this)
        }
    }


    companion object {
        private const val TAG = "PlaybackService"
    }
}