package com.ccc.ncs.di

import com.ccc.ncs.domain.repository.LyricsRepository
import com.ccc.ncs.domain.repository.PlaybackStateRepository
import com.ccc.ncs.domain.repository.PlayerRepository
import com.ccc.ncs.domain.usecase.GetPlayerStateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {
    @Provides
    fun provideGetPlayerStateUseCase(
        playbackStateRepository: PlaybackStateRepository,
        playerRepository: PlayerRepository,
        lyricsRepository: LyricsRepository,
    ) = GetPlayerStateUseCase(
        playbackStateRepository = playbackStateRepository,
        playerRepository = playerRepository,
        lyricsRepository = lyricsRepository
    )
}