package com.ccc.ncs.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

class DownloadCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadId != -1L) {
                context?.getSystemService(DownloadManager::class.java)?.let { manager ->
                    val query = DownloadManager.Query()
                    query.setFilterById(downloadId)

                    val downloadManagerCursor = manager.query(query)
                    downloadManagerCursor.moveToFirst()

                    val localUriIndex = downloadManagerCursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                    val statusIndex = downloadManagerCursor.getColumnIndex(DownloadManager.COLUMN_STATUS)

                    val downloadedFileUri = downloadManagerCursor.getString(localUriIndex)
                    val status = downloadManagerCursor.getInt(statusIndex)

                    val workRequest = OneTimeWorkRequestBuilder<DownloadCompletedWorker>()
                        .setInputData(workDataOf(
                            DownloadManager.COLUMN_LOCAL_URI to downloadedFileUri,
                            DownloadManager.COLUMN_STATUS to status
                        ))
                        .build()

                    WorkManager.getInstance(context).enqueue(workRequest)
                }
            }
        }
    }

    companion object {
        private const val TAG = "DownloadCompletedReceiver"
    }
}