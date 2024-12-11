package com.ccc.ncs.playback.listener

import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.analytics.PlaybackStatsListener
import com.ccc.ncs.analytics.AnalyticsEvent
import com.ccc.ncs.analytics.AnalyticsHelper
import com.ccc.ncs.playback.listener.PlaybackAnalyticsEventListener.Companion.ParamKeys.DURATION
import com.ccc.ncs.playback.listener.PlaybackAnalyticsEventListener.Companion.ParamKeys.MUSIC_ID
import com.ccc.ncs.playback.listener.PlaybackAnalyticsEventListener.Companion.ParamKeys.PLAY_TIME
import com.ccc.ncs.playback.listener.PlaybackAnalyticsEventListener.Companion.Types.PLAYBACK_TIME
import javax.inject.Inject


@OptIn(UnstableApi::class)
internal class PlaybackAnalyticsEventListener @Inject constructor(
    private val analyticsHelper: AnalyticsHelper
) : AnalyticsListener {
    var playbackStatsListener: PlaybackStatsListener? = null

    fun attach(player: ExoPlayer) {
        player.addListener(object : Player.Listener {
            // onMediaItemTransition 에서는 duration이 초기화 되어 있지 않음
            override fun onTracksChanged(tracks: Tracks) {
                playbackStatsListener?.let {
                    player.removeAnalyticsListener(it)
                }

                val currentMediaItem = player.currentMediaItem
                val duration = player.duration

                playbackStatsListener = PlaybackStatsListener(false) { _, stats ->
                    currentMediaItem?.let { mediaItem ->
                        logPlaybackDuration(mediaItem, stats.totalPlayTimeMs, duration)
                    }
                }.also {
                    player.addAnalyticsListener(it)
                }
            }
        })
    }

    private fun logPlaybackDuration(mediaItem: MediaItem, totalPlayTimeMs: Long, duration: Long) {
        analyticsHelper.logEvent(
            AnalyticsEvent(
                type = PLAYBACK_TIME,
                extras = listOf(
                    AnalyticsEvent.Param(
                        key = MUSIC_ID,
                        value = mediaItem.mediaId
                    ),
                    AnalyticsEvent.Param(
                        key = PLAY_TIME,
                        value = "$totalPlayTimeMs"
                    ),
                    AnalyticsEvent.Param(
                        key = DURATION,
                        value = "$duration"
                    )
                )
            )
        )
    }

    companion object {
        object Types {
            const val PLAYBACK_TIME = "playback_time"
        }

        object ParamKeys {
            const val MUSIC_ID = "music_id"
            const val PLAY_TIME = "play_time"
            const val DURATION = "duration"
        }
    }
}