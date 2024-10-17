package com.ccc.ncs.data.repository

import android.content.Context
import com.ccc.ncs.cache.CacheManager
import com.ccc.ncs.datastore.CacheDataStore
import com.ccc.ncs.model.MusicStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

internal class DefaultMusicCacheRepository @Inject constructor(
    private val cacheDataStore: CacheDataStore,
    private val musicRepository: MusicRepository,
    private val ioDispatcher: CoroutineDispatcher
) : MusicCacheRepository {
    override fun initialize(context: Context, maxCacheSizeMb: Int) {
        if (CacheManager.isInitialized) return
        CacheManager.initialize(context, maxCacheSizeMb)
    }

    override val maxSizeMb: Flow<Int> = cacheDataStore.storageMbSize

    override suspend fun setMaxSizeMb(size: Int) = cacheDataStore.setStorageMbSize(size)

    override val usedSizeBytes: Flow<Long?> = flow {
        while (true) {
            emit(CacheManager.usedCacheBytes)
            delay(1000)
        }
    }

    override val enableCache: Flow<Boolean> = cacheDataStore.enableCache

    override suspend fun setCacheEnable(enable: Boolean) {
        cacheDataStore.setEnableCache(enable)
        if (!enable) {
            clearCache()
        }
    }

    override fun clearCache() {
        CoroutineScope(ioDispatcher).launch {
            CacheManager.cleanCache()
            musicRepository.getMusicsByStatus(
                status = listOf(MusicStatus.FullyCached, MusicStatus.PartiallyCached)
            ).first()
                .forEach { cachedMusic ->
                    musicRepository.updateMusicStatus(cachedMusic.id, MusicStatus.None)
                }
        }
    }

    override suspend fun removeCachedMusic(vararg key: UUID) {
        key.forEach {
            CacheManager.removeFile(it.toString())
            musicRepository.updateMusicStatus(it, MusicStatus.None)
        }
    }
}