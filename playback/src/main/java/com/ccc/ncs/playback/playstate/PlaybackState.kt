package com.ccc.ncs.playback.playstate

import android.net.Uri
import androidx.media3.common.C
import androidx.media3.common.Player

data class PlaybackState(
    val playingStatus: PlayingStatus = PlayingStatus.IDLE,
    val currentIndex: Int = -1,
    val hasPrevious: Boolean = false,
    val hasNext: Boolean = false,
    val position: Long = C.TIME_UNSET,
    val duration: Long = C.TIME_UNSET,
    val speed: Float = 1F,
    val title: String? = null,
    val artist: String? = null,
    val artworkUri: Uri? = null,
    val isShuffleEnabled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.REPEAT_MODE_OFF
)

enum class RepeatMode(val value: Int) {
    REPEAT_MODE_ALL(Player.REPEAT_MODE_ALL),
    REPEAT_MODE_ONE(Player.REPEAT_MODE_ONE),
    REPEAT_MODE_OFF(Player.REPEAT_MODE_OFF);

    fun next(): RepeatMode =
        if (this.value == RepeatMode.entries.maxOf { it.value }) RepeatMode.entries.minBy { it.value }
        else RepeatMode.valueOf(this.value + 1)

    companion object {
        fun valueOf(value: Int): RepeatMode =
            RepeatMode.entries.firstOrNull { it.value == value }
            ?: throw IllegalArgumentException("Not exists repeat mode")
    }
}


enum class PlayingStatus {
    PLAYING,        // 재생 중
    PAUSED,         // 일시 중지
    BUFFERING,      // 버퍼링 중
    IDLE,           // 준비되지 않음
    ENDED           // 재생 완료
}

val Player.playingStatus: PlayingStatus
    get() = when {
        playbackState == Player.STATE_READY && playWhenReady -> PlayingStatus.PLAYING
        playbackState == Player.STATE_READY && !playWhenReady -> PlayingStatus.PAUSED
        playbackState == Player.STATE_BUFFERING -> PlayingStatus.BUFFERING
        playbackState == Player.STATE_IDLE -> PlayingStatus.IDLE
        playbackState == Player.STATE_ENDED -> PlayingStatus.ENDED
        else -> PlayingStatus.IDLE // 기본값을 IDLE로 설정
    }