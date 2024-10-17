package com.ccc.ncs.download

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import com.ccc.ncs.data.repository.MusicRepository
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.MusicStatus
import com.ccc.ncs.model.artistText
import com.ccc.ncs.ui.model.downloadUrl
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class MusicDownloader(
    @ApplicationContext private val context: Context,
    private val musicRepository: MusicRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val downloadDirectory: File?
) {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    fun download(music: Music): Long {
        CoroutineScope(ioDispatcher).launch {
            musicRepository.updateMusicStatus(music.id, MusicStatus.Downloading)
        }

        val file = File(downloadDirectory, "${music.id}")

        if (file.exists()) {
            file.delete()
        }

        val request = DownloadManager.Request(Uri.parse(music.downloadUrl))
            .setMimeType("audio/mpeg")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            .setTitle("${music.artistText} - ${music.title}")
            .setDestinationUri(Uri.fromFile(file))

        return downloadManager.enqueue(request)
    }
}