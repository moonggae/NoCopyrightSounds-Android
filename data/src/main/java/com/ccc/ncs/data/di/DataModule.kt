package com.ccc.ncs.data.di

import com.ccc.ncs.data.repository.ArtistRepository
import com.ccc.ncs.data.repository.DefaultArtistRepository
import com.ccc.ncs.data.repository.DefaultLyricsRepository
import com.ccc.ncs.data.repository.DefaultMusicCacheRepository
import com.ccc.ncs.data.repository.DefaultMusicRepository
import com.ccc.ncs.data.repository.DefaultPlayListRepository
import com.ccc.ncs.data.repository.DefaultPlayerRepository
import com.ccc.ncs.data.repository.DefaultRecentSearchRepository
import com.ccc.ncs.data.repository.MusicRepository
import com.ccc.ncs.data.util.ConnectivityManagerNetworkMonitor
import com.ccc.ncs.data.util.NetworkMonitor
import com.ccc.ncs.domain.repository.LyricsRepository
import com.ccc.ncs.domain.repository.MusicCacheRepository
import com.ccc.ncs.domain.repository.PlayListRepository
import com.ccc.ncs.domain.repository.PlayerRepository
import com.ccc.ncs.domain.repository.RecentSearchRepository
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

    @Binds
    internal abstract fun bindRecentSearchRepository(
        recentSearchRepository: DefaultRecentSearchRepository
    ): RecentSearchRepository

    @Binds
    internal abstract fun bindPlaylistRepository(
        playlistRepository: DefaultPlayListRepository
    ): PlayListRepository

    @Binds
    internal abstract fun bindPlayerRepository(
        playerRepository: DefaultPlayerRepository
    ): PlayerRepository

    @Binds
    internal abstract fun bindLyricsRepository(
        lyricsRepository: DefaultLyricsRepository
    ): LyricsRepository

    @Binds
    internal abstract fun bindMusicCacheRepository(
        musicCacheRepository: DefaultMusicCacheRepository
    ): MusicCacheRepository
}