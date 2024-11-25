package com.ccc.ncs.di

import com.ccc.ncs.domain.MediaPlaybackController
import com.ccc.ncs.domain.repository.LyricsRepository
import com.ccc.ncs.domain.repository.MusicRepository
import com.ccc.ncs.domain.repository.PlayListRepository
import com.ccc.ncs.domain.repository.PlaybackStateRepository
import com.ccc.ncs.domain.repository.PlayerRepository
import com.ccc.ncs.domain.usecase.AddPlaylistMusicUseCase
import com.ccc.ncs.domain.usecase.DeletePlaylistMusicUseCase
import com.ccc.ncs.domain.usecase.DeletePlaylistUseCase
import com.ccc.ncs.domain.usecase.GetPlayerStateUseCase
import com.ccc.ncs.domain.usecase.PlayMusicsUseCase
import com.ccc.ncs.domain.usecase.PlayPlaylistUseCase
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

    @Provides
    fun providesDeletePlaylistMusicUseCase(
        playlistRepository: PlayListRepository,
        playerRepository: PlayerRepository,
        playbackController: MediaPlaybackController,
    ) = DeletePlaylistMusicUseCase(
        playlistRepository = playlistRepository,
        playerRepository = playerRepository,
        playbackController = playbackController
    )

    @Provides
    fun providesAddPlaylistMusicUseCase(
        playlistRepository: PlayListRepository,
        musicRepository: MusicRepository,
        playerRepository: PlayerRepository,
        playbackController: MediaPlaybackController,
    ) = AddPlaylistMusicUseCase(
        playlistRepository = playlistRepository,
        musicRepository = musicRepository,
        playerRepository = playerRepository,
        playbackController = playbackController
    )

    @Provides
    fun providesDeletePlaylistUseCase(
        playlistRepository: PlayListRepository,
        playerRepository: PlayerRepository,
        playbackController: MediaPlaybackController,
    ) = DeletePlaylistUseCase(
        playlistRepository = playlistRepository,
        playerRepository = playerRepository,
        playbackController = playbackController
    )

    @Provides
    fun providesPlayPlaylistUseCase(
        playlistRepository: PlayListRepository,
        playerRepository: PlayerRepository,
        playbackController: MediaPlaybackController,
    ) = PlayPlaylistUseCase(
        playlistRepository = playlistRepository,
        playerRepository = playerRepository,
        playbackController = playbackController
    )

    @Provides
    fun providesPlayMusicsUseCase(
        playlistRepository: PlayListRepository,
        playerRepository: PlayerRepository,
        musicRepository: MusicRepository,
        playPlaylistUseCase: PlayPlaylistUseCase
    ) = PlayMusicsUseCase(
        playlistRepository = playlistRepository,
        playerRepository = playerRepository,
        musicRepository = musicRepository,
        playPlaylistUseCase = playPlaylistUseCase
    )
}