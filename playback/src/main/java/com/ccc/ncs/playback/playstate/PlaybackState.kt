package com.ccc.ncs.playback.playstate

import android.net.Uri
import androidx.media3.common.C

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
)