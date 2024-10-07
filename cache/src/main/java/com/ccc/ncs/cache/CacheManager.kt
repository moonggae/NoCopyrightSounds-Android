package com.ccc.ncs.cache

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheSpan
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

@OptIn(UnstableApi::class)
object CacheManager {
    lateinit var cache: SimpleCache
    private lateinit var cacheStorage: File
    private var cacheMaxBytes: Long = 0L
    private lateinit var databaseProvider: DatabaseProvider
    private const val CACHE_DIR = ".musicCache"
    private val cacheListenerMap = mutableMapOf<String, Cache.Listener>()

    fun initialize(
        context: Context,
        cacheMaxMB: Int
    ) {
        if (isInitialized) cache.release()
        cacheStorage = File(context.getExternalFilesDir(null), CACHE_DIR)
        cacheMaxBytes = cacheMaxMB * 1024 * 1024L
        databaseProvider = StandaloneDatabaseProvider(context)

        cache = SimpleCache(
            cacheStorage,
            LeastRecentlyUsedCacheEvictor(cacheMaxBytes),
            databaseProvider
        )
    }

    val isInitialized: Boolean
        get() = this::cache.isInitialized

    val usedCacheBytes: Long? get() {
        return if (!isInitialized) {
            null
        } else {
            cache.cacheSpace
        }
    }

    fun cleanCache() {
        if (!isInitialized) return
        SimpleCache.delete(cacheStorage, null)
        // databaseProvider 삭제시 바로 재생하면 오류 발생함
    }

    fun addOnCacheUpdateListener(key: String, block:(isFullyCached: Boolean) -> Unit) {
        val listener = object : Cache.Listener {
            override fun onSpanAdded(cache: Cache, span: CacheSpan) {
                val isFullyCached = cache.isFullyCached(key)
                block(isFullyCached)
            }
            override fun onSpanTouched(cache: Cache, oldSpan: CacheSpan, newSpan: CacheSpan) {
                val isFullyCached = cache.isFullyCached(key)
                block(isFullyCached)
            }
            override fun onSpanRemoved(cache: Cache, span: CacheSpan) {}
        }

        cacheListenerMap[key] = listener
        cache.addListener(key, listener)
    }

    fun removeOnCacheUpdateListener(key: String) {
        cacheListenerMap[key]?.let {
            cache.removeListener(key, it)
        }
        cacheListenerMap.remove(key)
    }

    fun clearOnCacheUpdateListener() {
        cacheListenerMap.keys.forEach { key ->
            removeOnCacheUpdateListener(key)
        }
    }
}