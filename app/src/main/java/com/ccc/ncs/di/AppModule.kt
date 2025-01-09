package com.ccc.ncs.di

import android.content.Context
import android.os.Environment
import androidx.activity.ComponentActivity
import com.ccc.ncs.MainActivity
import com.ccc.ncs.R
import com.ccc.ncs.playback.di.PlaylistString
import com.ccc.ncs.playback.di.RecentPlaylistString
import com.ccc.ncs.util.isDeviceOnePlus
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.File
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Provides
    fun providesContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun providesApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    @Singleton
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    @Provides
    @Singleton
    fun provideMusicDownloadDirectory(
        @ApplicationContext context: Context,
        crashlytics: FirebaseCrashlytics
    ): File? = try {
        val directory = if (isDeviceOnePlus()) {
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