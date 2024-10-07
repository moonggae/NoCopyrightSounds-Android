package com.ccc.ncs.di

import com.ccc.ncs.data.repository.MusicRepository
import com.ccc.ncs.download.DownloadCompletedWorker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {

    @Provides
    fun providesDownloadCompletedWorkerFactory(
        musicRepository: MusicRepository
    ): DownloadCompletedWorker.Factory = DownloadCompletedWorker.Factory(musicRepository)
}