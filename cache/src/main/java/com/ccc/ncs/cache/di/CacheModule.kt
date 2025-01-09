package com.ccc.ncs.cache.di

import com.ccc.ncs.cache.CacheDataStore
import com.ccc.ncs.cache.DefaultCacheDataStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CacheDataStoreModule {
    @Binds
    internal abstract fun bindsCacheDataStore(
        default: DefaultCacheDataStore
    ): CacheDataStore
}