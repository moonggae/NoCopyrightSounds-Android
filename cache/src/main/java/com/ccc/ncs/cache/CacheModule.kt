package com.ccc.ncs.cache

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import com.ccc.ncs.datastore.CacheDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

// cache 모듈
@Singleton
class CacheInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cacheDataStore: CacheDataStore
) {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun initialize() {
        applicationScope.launch {
            val maxMb = cacheDataStore.storageMbSize.first()
            CacheManager.initialize(context, maxMb)
        }
    }

    @OptIn(UnstableApi::class)
    fun awaitInitialization(): Cache = CacheManager.cache
}

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {
    @Provides
    @Singleton
    fun provideCacheInitializer(
        @ApplicationContext context: Context,
        cacheDataStore: CacheDataStore
    ): CacheInitializer = CacheInitializer(context, cacheDataStore)

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideCache(initializer: CacheInitializer): Cache = initializer.awaitInitialization()
}