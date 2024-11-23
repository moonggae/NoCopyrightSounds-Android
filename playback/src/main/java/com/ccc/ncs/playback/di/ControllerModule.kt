package com.ccc.ncs.playback.di

import com.ccc.ncs.domain.MediaPlaybackController
import com.ccc.ncs.playback.DefaultMediaPlaybackController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ControllerModule {
    @Binds
    internal abstract fun bindsMediaPlaybackController(
        default: DefaultMediaPlaybackController
    ): MediaPlaybackController
}