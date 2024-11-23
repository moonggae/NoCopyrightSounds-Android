package com.ccc.ncs.download

import android.app.DownloadManager
import android.content.Context
import android.content.res.Resources.NotFoundException
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.ccc.ncs.data.repository.MusicRepository
import com.ccc.ncs.domain.MediaPlaybackController
import com.ccc.ncs.model.MusicStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject


@HiltWorker
class DownloadCompletedWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val musicRepository: MusicRepository,
    private val mediaPlaybackController: MediaPlaybackController
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val downloadStatus = inputData.getInt(DownloadManager.COLUMN_STATUS, -1)
            val downloadUri = inputData.getString(DownloadManager.COLUMN_LOCAL_URI)

            downloadUri?.split("/")?.last()?.let { musicId ->
                val music =
                    musicRepository.getMusic(UUID.fromString(musicId)).first()?.copy(
                        status = MusicStatus.Downloaded(downloadUri)
                    )
                    ?: throw NotFoundException("wrong music id")


                if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL) {
                    musicRepository.updateMusicStatus(music.id, music.status)
                    mediaPlaybackController.updateCurrentPlaylistMusic(music)
                } else {
                    musicRepository.updateMusicStatus(music.id, MusicStatus.None)
                }
            }

            Result.success()
        } catch (th: Throwable) {
            Result.failure()
        }
    }

    class Factory @Inject constructor(
        private val musicRepository: MusicRepository,
        private val mediaPlaybackController: MediaPlaybackController
    ) : WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return DownloadCompletedWorker(appContext, workerParameters, musicRepository, mediaPlaybackController)
        }
    }
}