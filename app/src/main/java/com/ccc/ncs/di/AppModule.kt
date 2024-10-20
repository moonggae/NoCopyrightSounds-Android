package com.ccc.ncs.di

import android.content.Context
import android.os.Environment
import com.ccc.ncs.data.repository.MusicRepository
import com.ccc.ncs.download.MusicDownloader
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
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
object AppModule {
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
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics
}