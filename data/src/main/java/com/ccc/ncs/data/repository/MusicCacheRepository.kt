package com.ccc.ncs.data.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface MusicCacheRepository {
    fun initialize(context: Context, maxCacheSizeMb: Int)

    val maxSizeMb: Flow<Int>
    suspend fun setMaxSizeMb(size: Int)

    val usedSizeBytes: Flow<Long?>

    val enableCache: Flow<Boolean>

    suspend fun setCacheEnable(enable: Boolean)

    fun clearCache()
}