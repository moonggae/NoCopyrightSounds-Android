package com.ccc.ncs.playback.playstate

import android.util.Log
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

internal class PlaybackStateListener @Inject constructor(
    private val scope: CoroutineScope,
    private val playbackStateManager: PlaybackStateManager
) : Player.Listener {

    private lateinit var player: Player
    private var job: Job? = null

    fun attachTo(player: Player) {
        this.player = player
        player.addListener(this)

        job?.cancel()
        job = scope.launch {
            playbackStateManager.flow
                .map { it.isPlaying }
                .collectLatest { isPlaying ->
                    while (true) {
                        updatePlayState()
                        delay(400.milliseconds)
                    }
                }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        Log.d("TAG", "PlaybackStateListener - onPlaybackStateChanged")
        updatePlayState()
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        Log.d("TAG", "PlaybackStateListener - onPlayWhenReadyChanged")
        updatePlayState()
    }

    override fun onPositionDiscontinuity(
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: Int
    ) {
        Log.d("TAG", "PlaybackStateListener - onPositionDiscontinuity")
        updatePlayState()
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.d("TAG", "PlaybackStateListener - onIsPlayingChanged")
        updatePlayState()
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
        Log.d("TAG", "PlaybackStateListener - onPlaybackParametersChanged")
        updatePlayState()
    }


    private fun updatePlayState() {
        val playbackState = player.playbackState
        playbackStateManager.playbackState = PlaybackState(
            isPlaying = when {
                playbackState == Player.STATE_ENDED || playbackState == Player.STATE_IDLE -> false
                player.playWhenReady -> true
                else -> false
            },
            currentIndex = player.currentMediaItemIndex,
            hasPrevious = player.hasPreviousMediaItem(),
            hasNext = player.hasNextMediaItem(),
            position = player.contentPosition,
            duration = player.duration,
            speed = player.playbackParameters.speed,
            title = player.currentMediaItem?.mediaMetadata?.title?.toString(),
            artist = player.currentMediaItem?.mediaMetadata?.artist?.toString(),
            artworkUri = player.currentMediaItem?.mediaMetadata?.artworkUri
        )
    }
}