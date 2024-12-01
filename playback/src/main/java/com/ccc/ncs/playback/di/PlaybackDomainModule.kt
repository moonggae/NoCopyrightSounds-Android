package com.ccc.ncs.playback.di

import com.ccc.ncs.domain.MediaPlaybackController
import com.ccc.ncs.domain.repository.PlaybackStateRepository
import com.ccc.ncs.playback.DefaultMediaPlaybackController
import com.ccc.ncs.playback.repository.DefaultPlaybackStateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class PlaybackDomainModule {
    @Binds
    internal abstract fun bindsMediaPlaybackController(
        default: DefaultMediaPlaybackController
    ): MediaPlaybackController

    @Binds
    internal abstract fun bindsPlaybackStateRepository(
        default: DefaultPlaybackStateRepository
    ): PlaybackStateRepository
}