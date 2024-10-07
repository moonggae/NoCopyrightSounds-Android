package com.ccc.ncs

import android.app.Application
import androidx.work.Configuration
import com.ccc.ncs.download.DownloadCompletedWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NcsApplication: Application(), Configuration.Provider {
    @Inject
    lateinit var downloadCompletedWorkerFactory: DownloadCompletedWorker.Factory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(downloadCompletedWorkerFactory)
            .build()
}