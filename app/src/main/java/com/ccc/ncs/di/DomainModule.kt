package com.ccc.ncs.di

import com.ccc.ncs.domain.MediaPlaybackController
import com.ccc.ncs.domain.repository.LyricsRepository
import com.ccc.ncs.domain.repository.PlayListRepository
import com.ccc.ncs.domain.repository.PlaybackStateRepository
import com.ccc.ncs.domain.repository.PlayerRepository
import com.ccc.ncs.domain.usecase.GetPlayerStateUseCase
import com.ccc.ncs.domain.usecase.UpdatePlaylistMusicOrderUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {
    @Provides
    fun providesGetPlayerStateUseCase(
        playbackStateRepository: PlaybackStateRepository,
        playerRepository: PlayerRepository,
        lyricsRepository: LyricsRepository,
    ) = GetPlayerStateUseCase(
        playbackStateRepository = playbackStateRepository,
        playerRepository = playerRepository,
        lyricsRepository = lyricsRepository
    )

    @Provides
    fun providesUpdatePlaylistMusicOrderUseCase(
        playlistRepository: PlayListRepository,
        playerRepository: PlayerRepository,
        playbackController: MediaPlaybackController,
    ) = UpdatePlaylistMusicOrderUseCase(
        playlistRepository = playlistRepository,
        playerRepository = playerRepository,
        playbackController = playbackController
    )
}