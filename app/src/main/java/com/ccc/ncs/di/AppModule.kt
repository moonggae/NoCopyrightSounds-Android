package com.ccc.ncs.di

import android.content.Context
import com.ccc.ncs.data.repository.MusicRepository
import com.ccc.ncs.download.MusicDownloader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providesMusicDownloader(
        @ApplicationContext context: Context,
        musicRepository: MusicRepository,
        ioDispatcher: CoroutineDispatcher
    ): MusicDownloader = MusicDownloader(
        context = context,
        musicRepository = musicRepository,
        ioDispatcher = ioDispatcher
    )
}