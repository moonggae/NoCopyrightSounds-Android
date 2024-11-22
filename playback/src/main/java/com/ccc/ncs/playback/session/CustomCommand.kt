package com.ccc.ncs.playback.session

import android.os.Bundle
import androidx.media3.common.Player
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand

const val ACTION_REPEAT = "ACTION_REPEAT"
const val ACTION_SHUFFLE = "ACTION_SHUFFLE"

val customCommandRepeat = SessionCommand(ACTION_REPEAT, Bundle())
val customCommandShuffle = SessionCommand(ACTION_SHUFFLE, Bundle())

fun createRepeatButton(session: MediaSession): CommandButton {
    val repeatIconResId = when (session.player.repeatMode) {
        Player.REPEAT_MODE_OFF -> androidx.media3.session.R.drawable.media3_icon_repeat_off
        Player.REPEAT_MODE_ONE -> androidx.media3.session.R.drawable.media3_icon_repeat_one
        Player.REPEAT_MODE_ALL -> androidx.media3.session.R.drawable.media3_icon_repeat_all
        else -> androidx.media3.session.R.drawable.media3_icon_repeat_off
    }

    return CommandButton.Builder()
        .setSessionCommand(customCommandRepeat)
        .setIconResId(repeatIconResId)
        .setDisplayName("Repeat")
        .build()
}


fun createShuffleButton(session: MediaSession): CommandButton {
    val shuffleIconResId = if (session.player.shuffleModeEnabled) {
        androidx.media3.session.R.drawable.media3_icon_shuffle_on
    } else {
        androidx.media3.session.R.drawable.media3_icon_shuffle_off
    }

    return CommandButton.Builder()
        .setSessionCommand(customCommandShuffle)
        .setIconResId(shuffleIconResId)
        .setDisplayName("Shuffle")
        .build()
}