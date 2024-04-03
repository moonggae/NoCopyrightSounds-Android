package com.ccc.ncs.data.di

import com.ccc.ncs.data.repository.DefaultMusicRepository
import com.ccc.ncs.data.repository.MusicRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsMusicRepository(
        musicRepository: DefaultMusicRepository
    ): MusicRepository
}