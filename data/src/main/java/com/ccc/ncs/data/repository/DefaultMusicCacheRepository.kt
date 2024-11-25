package com.ccc.ncs.data.repository

import com.ccc.ncs.cache.di.CacheManager
import com.ccc.ncs.datastore.CacheDataStore
import com.ccc.ncs.domain.repository.MusicCacheRepository
import com.ccc.ncs.domain.repository.MusicRepository
import com.ccc.ncs.model.MusicStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

internal class DefaultMusicCacheRepository @Inject constructor(
    private val cacheDataStore: CacheDataStore,
    private val musicRepository: MusicRepository,
    private val ioDispatcher: CoroutineDispatcher
) : MusicCacheRepository {
    override val maxSizeMb: Flow<Int> = cacheDataStore.storageMbSize

    override suspend fun setMaxSizeMb(size: Int) = cacheDataStore.setStorageMbSize(size)

    override val enableCache: Flow<Boolean> = cacheDataStore.enableCache

    @OptIn(ExperimentalCoroutinesApi::class)
    override val usedSizeBytes: Flow<Long?> = enableCache.flatMapLatest { enable ->
        if (enable) {
            flow<Long?> {
                while (true) {
                    emit(CacheManager.usedCacheBytes)
                    delay(1000)
                }
            }
        } else {
            flowOf(0L)
        }
    }

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

    override suspend fun removeCachedMusic(vararg keys: UUID) {
        keys.forEach { key ->
            CacheManager.removeFile(key.toString())
            musicRepository.updateMusicStatus(key, MusicStatus.None)
        }
    }
}