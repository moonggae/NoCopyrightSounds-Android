package com.ccc.ncs.network.di

import com.ccc.ncs.network.NcsNetworkDataSource
import com.ccc.ncs.network.retrofit.RetrofitNcsNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NcsNetworkModule {
    @Binds
    fun binds(impl: RetrofitNcsNetwork): NcsNetworkDataSource
}