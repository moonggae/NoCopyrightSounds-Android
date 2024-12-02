package com.ccc.ncs.cache.di

import com.ccc.ncs.cache.CacheDataStore
import com.ccc.ncs.cache.DefaultCacheDataStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


//@Module
//@InstallIn(SingletonComponent::class)
//object CacheModule {
//    @OptIn(UnstableApi::class)
//    @Provides
//    @Singleton
//    fun provideCacheManager(
//        @ApplicationContext context: Context,
//        cacheDataStore: CacheDataStore
//    ): CacheManager = CacheManager(
//        context = context,
//        dataStore = cacheDataStore
//    )
//}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CacheDataStoreModule {
    @Binds
    internal abstract fun bindsCacheDataStore(
        default: DefaultCacheDataStore
    ): CacheDataStore
}