package com.ccc.ncs

import android.app.Application
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import com.ccc.ncs.download.DownloadCompletedWorker
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class NcsApplication: Application(), Configuration.Provider, ImageLoaderFactory {
    @Inject
    lateinit var downloadCompletedWorkerFactory: DownloadCompletedWorker.Factory

    @Inject
    lateinit var scope: CoroutineScope

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(downloadCompletedWorkerFactory)
            .build()

    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve(".imageCache"))
                    .minimumMaxSizeBytes(30 * 1024 * 1024)
                    .maximumMaxSizeBytes(100 * 1024 * 1024)
                    .build()
            }
            .build()

    override fun onCreate() {
        super.onCreate()
        configureFirebaseAnalytics()
    }

    private fun configureFirebaseAnalytics() {
        scope.launch {
            if (BuildConfig.DEBUG) {
                Firebase.crashlytics.isCrashlyticsCollectionEnabled = false
            } else {
                val enabled = BuildConfig.FIREBASE_ANALYTICS_ENABLED.toBooleanStrictOrNull() ?: false
                Firebase.crashlytics.isCrashlyticsCollectionEnabled = enabled
            }
        }
    }
}