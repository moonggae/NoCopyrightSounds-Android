package com.ccc.ncs.playback.playstate

import android.net.Uri
import androidx.media3.common.C
import androidx.media3.common.Player

data class PlaybackState(
    val isPlaying: Boolean = false,
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