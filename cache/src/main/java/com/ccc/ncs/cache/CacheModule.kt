package com.ccc.ncs.cache

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import com.ccc.ncs.cache.di.CacheManager
import com.ccc.ncs.datastore.CacheDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CacheModule {
    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideCache(
        @ApplicationContext context: Context,
        cacheDataStore: CacheDataStore
    ): Cache? = runBlocking {
        if (!cacheDataStore.enableCache.first()) {
            null
        }
        else if (CacheManager.isInitialized) {
            CacheManager.cache
        } else {
            val maxMb = cacheDataStore.storageMbSize.first()
            CacheManager.initialize(
                context, maxMb
            )
            CacheManager.cache
        }
    }
}