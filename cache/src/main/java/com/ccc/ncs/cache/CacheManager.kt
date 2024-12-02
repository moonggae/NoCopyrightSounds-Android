package com.ccc.ncs.cache

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.CacheSpan
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@OptIn(UnstableApi::class)
@Singleton
class CacheManager @Inject constructor(
    @ApplicationContext context: Context,
    private val dataStore: CacheDataStore
) {
    val enableCache: Boolean get() = dataStore.getEnableCache()
    val maxSizeMb: Int = dataStore.getStorageMbSize()

    val maxSizeMbFlow: Flow<Int> = dataStore.getStorageMbSizeFlow()
    val enableCacheFlow: Flow<Boolean> = dataStore.getEnableCacheFlow()

    private var cacheStorage: File = File(context.getExternalFilesDir(null), CACHE_DIR)
    private val cacheMaxBytes: Long = maxSizeMb * 1024 * 1024L
    private var databaseProvider: DatabaseProvider = StandaloneDatabaseProvider(context)
    private val cacheListenerMap = mutableMapOf<String, Cache.Listener>()

    private val cache: SimpleCache? by lazy {
        if (enableCache) {
            SimpleCache(
                cacheStorage,
                LeastRecentlyUsedCacheEvictor(cacheMaxBytes),
                databaseProvider
            )
        } else null
    }

    val usedCacheBytes: Long?
        get() = cache?.cacheSpace

    fun cleanCache() {
        SimpleCache.delete(cacheStorage, null)
        // databaseProvider 삭제시 바로 재생하면 오류 발생함
    }

    fun addOnCacheUpdateListener(
        key: String,
        scope: CoroutineScope,
        block: suspend (isFullyCached: Boolean) -> Unit,
    ) {
        val listener = object : Cache.Listener {
            override fun onSpanAdded(cache: Cache, span: CacheSpan) {
                scope.launch(Dispatchers.IO) {
                    val isFullyCached = cache.isFullyCached(key)
                    block(isFullyCached)
                }
            }

            override fun onSpanTouched(cache: Cache, oldSpan: CacheSpan, newSpan: CacheSpan) {
                scope.launch(Dispatchers.IO) {
                    val isFullyCached = cache.isFullyCached(key)
                    block(isFullyCached)
                }
            }

            override fun onSpanRemoved(cache: Cache, span: CacheSpan) {}
        }

        cacheListenerMap[key] = listener
        cache?.addListener(key, listener)
    }

    fun removeOnCacheUpdateListener(key: String) {
        cacheListenerMap[key]?.let {
            cache?.removeListener(key, it)
        }
        cacheListenerMap.remove(key)
    }

    fun clearOnCacheUpdateListener() {
        cacheListenerMap.keys.forEach { key ->
            removeOnCacheUpdateListener(key)
        }
    }

    fun removeFile(key: String) {
        cache?.removeResource(key)
    }

    fun getProgressiveMediaSourceFactory(context: Context): ProgressiveMediaSource.Factory? {
        return cache?.let { nonNullCache ->
            val cacheDataSourceFactory = CacheDataSource.Factory()
                .setCacheKeyFactory { it.key ?: "" }
                .setCache(nonNullCache)
                .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context))

            ProgressiveMediaSource.Factory(cacheDataSourceFactory)
        }
    }

    fun setCacheEnable(enable: Boolean) {
        dataStore.setEnableCache(enable)
        if (!enable) {
            clearOnCacheUpdateListener()
            cleanCache()
        }
    }

    fun setMaxSize(mb: Int) = dataStore.setStorageMbSize(mb)

    companion object {
        private val CACHE_DIR = ".musicCache"
    }
}