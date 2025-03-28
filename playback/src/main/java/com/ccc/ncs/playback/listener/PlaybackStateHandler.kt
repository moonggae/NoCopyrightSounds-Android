package com.ccc.ncs.playback.listener

import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import com.ccc.ncs.domain.model.PlaybackState
import com.ccc.ncs.domain.model.PlayingStatus
import com.ccc.ncs.domain.model.RepeatMode
import com.ccc.ncs.playback.playstate.PlaybackStateManager
import com.ccc.ncs.playback.playstate.playingStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

internal class PlaybackStateHandler @Inject constructor(
    private val scope: CoroutineScope,
    private val playbackStateManager: PlaybackStateManager,
    private val mainDispatcher: CoroutineDispatcher
) : Player.Listener {

    private lateinit var player: Player
    private var job: Job? = null

    fun attachTo(player: Player) {
        this.player = player
        player.addListener(this)

        job?.cancel()
        job = scope.launch(mainDispatcher) {
            playbackStateManager.flow
                .map { it.playingStatus }
                .collectLatest { playingStatus ->
                    if (playingStatus != PlayingStatus.IDLE && playingStatus != PlayingStatus.ENDED) {
                        while (true) {
                            updatePlayState()
                            delay(400.milliseconds)
                        }
                    }
                }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        updatePlayState()
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        updatePlayState()
    }

    override fun onPositionDiscontinuity(
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: Int
    ) {
        updatePlayState()
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        updatePlayState()
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
        updatePlayState()
    }

    override fun onEvents(player: Player, events: Player.Events) {
        if (events.contains(Player.EVENT_POSITION_DISCONTINUITY)) {
            onPositionChanged()
        }
        super.onEvents(player, events)
    }

    fun onPositionChanged() { // seekTo()를 호출해서 position이 변경된 경우 호출
        updatePlayState()
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
        updatePlayState()
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        updatePlayState()
    }

    override fun onTimelineChanged(timeline: Timeline, reason: Int) { // MediaItem 추가시 호출
        updatePlayState()
    }

    private fun updatePlayState() {
        playbackStateManager.playbackState = PlaybackState(
            playingStatus = player.playingStatus,
            currentIndex = player.currentMediaItemIndex,
            hasPrevious = player.hasPreviousMediaItem(),
            hasNext = player.hasNextMediaItem(),
            position = player.contentPosition,
            duration = player.duration,
            isShuffleOn = player.shuffleModeEnabled,
            repeatMode = RepeatMode.valueOf(player.repeatMode)
        )

        Player.REPEAT_MODE_ALL
    }
}