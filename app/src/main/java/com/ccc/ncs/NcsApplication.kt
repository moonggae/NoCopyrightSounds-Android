package com.ccc.ncs

import android.app.Application
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import com.ccc.ncs.cache.CacheInitializer
import com.ccc.ncs.download.DownloadCompletedWorker
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.installations.ktx.installations
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltAndroidApp
class NcsApplication: Application(), Configuration.Provider, ImageLoaderFactory {
    @Inject
    lateinit var downloadCompletedWorkerFactory: DownloadCompletedWorker.Factory

    @Inject
    lateinit var cacheInitializer: CacheInitializer

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(downloadCompletedWorkerFactory)
            .build()

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

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
        cacheInitializer.initialize()
        configureFirebaseAnalytics()
    }

    private fun configureFirebaseAnalytics() {
        CoroutineScope(Dispatchers.IO).launch {
            Firebase.analytics.setUserId(Firebase.installations.id.await())
            val enabled = BuildConfig.FIREBASE_ANALYTICS_ENABLED.toBooleanStrictOrNull() ?: false
            Firebase.analytics.setAnalyticsCollectionEnabled(enabled)
            Firebase.crashlytics.isCrashlyticsCollectionEnabled = enabled
        }
    }
}