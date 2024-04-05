package com.ccc.ncs.data.di

import com.ccc.ncs.data.repository.ArtistRepository
import com.ccc.ncs.data.repository.DefaultArtistRepository
import com.ccc.ncs.data.repository.DefaultMusicRepository
import com.ccc.ncs.data.repository.MusicRepository
import com.ccc.ncs.data.util.ConnectivityManagerNetworkMonitor
import com.ccc.ncs.data.util.NetworkMonitor
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

    @Binds
    internal abstract fun bindsArtistRepository(
        artistRepository: DefaultArtistRepository
    ): ArtistRepository

    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor
}