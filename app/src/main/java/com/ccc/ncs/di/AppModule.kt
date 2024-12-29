package com.ccc.ncs.di

import android.content.Context
import android.os.Environment
import androidx.activity.ComponentActivity
import com.ccc.ncs.MainActivity
import com.ccc.ncs.R
import com.ccc.ncs.domain.repository.MusicRepository
import com.ccc.ncs.download.MusicDownloader
import com.ccc.ncs.playback.util.PlaylistString
import com.ccc.ncs.playback.util.RecentPlaylistString
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
    fun provideMusicDownloadDirectory(
        @ApplicationContext context: Context
    ): File? = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)

    @Provides
    @Singleton
    fun providesMusicDownloader(
        @ApplicationContext context: Context,
        musicRepository: MusicRepository,
        ioDispatcher: CoroutineDispatcher,
        downloadDirectory: File?
    ): MusicDownloader = MusicDownloader(
        context = context,
        musicRepository = musicRepository,
        ioDispatcher = ioDispatcher,
        downloadDirectory = downloadDirectory
    )

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