package com.ccc.ncs.data.di

import com.ccc.ncs.data.repository.PlaybackServiceDataSourceImpl
import com.ccc.ncs.data.repository.PlaybackSessionDataSourceImpl
import com.ccc.ncs.playback.data.PlaybackServiceDataSource
import com.ccc.ncs.playback.data.PlaybackSessionDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent

@Module
@InstallIn(ServiceComponent::class)
abstract class PlaybackModule {
    @Binds
    abstract fun bindPlaybackDataSource(
        impl: PlaybackServiceDataSourceImpl
    ): PlaybackServiceDataSource

    @Binds
    abstract fun bindSessionDataSource(
        impl: PlaybackSessionDataSourceImpl
    ): PlaybackSessionDataSource
}