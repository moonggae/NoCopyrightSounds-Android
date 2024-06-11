package com.ccc.ncs.network.di

import com.ccc.ncs.network.LyricsNetworkDataSource
import com.ccc.ncs.network.NcsNetworkDataSource
import com.ccc.ncs.network.retrofit.RetrofitLyricsNetwork
import com.ccc.ncs.network.retrofit.RetrofitNcsNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NcsNetworkModule {
    @Binds
    fun bindsNcsNetwork(impl: RetrofitNcsNetwork): NcsNetworkDataSource

    @Binds
    fun bindsLyricsNetwork(impl: RetrofitLyricsNetwork): LyricsNetworkDataSource
}