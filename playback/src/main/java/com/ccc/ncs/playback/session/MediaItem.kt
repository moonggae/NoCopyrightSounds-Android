package com.ccc.ncs.playback.session

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.artistText

fun Music.asMediaItem(): MediaItem = MediaItem.Builder()
    .setUri(dataUrl)
    .setMediaMetadata(
        MediaMetadata
            .Builder()
            .setArtist(artistText)
            .setTitle(title)
            .setArtworkUri(Uri.parse(coverUrl))
            .build()
    ).build()