package com.ccc.ncs.di

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.activity.ComponentActivity
import com.ccc.ncs.MainActivity
import com.ccc.ncs.R
import com.ccc.ncs.playback.util.PlaylistString
import com.ccc.ncs.playback.util.RecentPlaylistString
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.io.File
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Provides
    fun providesContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    @Provides
    @Singleton
    fun provideMusicDownloadDirectory(
        @ApplicationContext context: Context,
        crashlytics: FirebaseCrashlytics
    ): File? = try {
        val isOnePlusDevice = Build.MANUFACTURER.equals("OnePlus", ignoreCase = true) ||
                Build.BRAND.equals("OnePlus", ignoreCase = true)

        crashlytics.setCustomKey("is_oneplus", isOnePlusDevice.toString())

        val directory = if (isOnePlusDevice) {
            context.getExternalFilesDir(null)?.also {
                File(it, "Music").apply { mkdirs() }
            }
        } else {
            context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        }

        directory?.takeIf {
            it.canRead() && it.canWrite()
        } ?: throw SecurityException("Unsupported path: ${directory?.absolutePath}")
    } catch (e: SecurityException) {
        crashlytics.recordException(e)
        File(context.filesDir, "Music").apply { mkdirs() }
    }

    @Provides
    @Singleton
    fun providesMainActivityClass(): Class<out ComponentActivity> = MainActivity::class.java

    @Provides
    @Singleton
    @PlaylistString
    fun providesPlaylistString(@ApplicationContext context: Context): String =
        context.getString(R.string.feature_playlist_title)

    @Provides
    @Singleton
    @RecentPlaylistString
    fun providesRecentPlaylistString(@ApplicationContext context: Context): String =
        context.getString(R.string.playlist_name_auto_generated)
}