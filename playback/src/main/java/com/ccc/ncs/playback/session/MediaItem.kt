package com.ccc.ncs.playback.session

import android.net.Uri
import androidx.annotation.OptIn
import androidx.core.net.toFile
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.MusicStatus
import com.ccc.ncs.model.artistText

@OptIn(UnstableApi::class)
internal fun Music.asMediaItem(): MediaItem = MediaItem.Builder()
    .setUri((status as? MusicStatus.Downloaded)?.localUri ?: dataUrl)
    .setCustomCacheKey(id.toString())
    .setMimeType(MimeTypes.AUDIO_MPEG)
    .setMediaId(id.toString())
    .setMediaMetadata(
        MediaMetadata
            .Builder()
            .setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
            .setArtist(artistText)
            .setTitle(title)
            .setArtworkUri(Uri.parse(coverUrl))
            .build()
    ).build()

val MediaItem.isNetworkSource: Boolean
    get() {
        val uriScheme = this.localConfiguration?.uri?.scheme?.lowercase()
        return uriScheme == "http" || uriScheme == "https"
    }

val MediaItem.isLocalFileExists: Boolean
    get() = try {
        localConfiguration?.uri?.toFile()?.exists() ?: false
    } catch (e: IllegalArgumentException) {
        false
    }