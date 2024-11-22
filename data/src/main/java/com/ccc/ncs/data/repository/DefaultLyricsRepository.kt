package com.ccc.ncs.data.repository

import android.util.Log
import com.ccc.ncs.domain.repository.LyricsRepository
import com.ccc.ncs.network.LyricsNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultLyricsRepository @Inject constructor(
    private val networkDataSource: LyricsNetworkDataSource
) : LyricsRepository {
    private val cache = LyricsCache(maxSize = 50)

    private class LyricsCache(private val maxSize: Int) {
        private val cache = mutableMapOf<String, CacheEntry>()

        private data class CacheEntry(
            val lyrics: String?,
            var lastAccessed: Long = System.currentTimeMillis()
        )

        @Synchronized
        fun containsKey(key: String): Boolean {
            return cache.containsKey(key).also { exists ->
                if (exists) {
                    updateLastAccessed(key)
                }
            }
        }

        @Synchronized
        operator fun get(key: String): String? =
            cache[key]?.also { updateLastAccessed(key) }?.lyrics

        @Synchronized
        operator fun set(key: String, value: String?) {
            if (cache.size >= maxSize) {
                cache.entries
                    .minByOrNull { it.value.lastAccessed }
                    ?.key
                    ?.let { cache.remove(it) }
            }
            cache[key] = CacheEntry(value)
        }

        private fun updateLastAccessed(key: String) {
            cache[key]?.lastAccessed = System.currentTimeMillis()
        }
    }

    override fun getLyrics(title: String): Flow<String?> = flow {
        val lyrics = when {
            cache[title] != null -> cache[title]
            cache.containsKey(title) -> null
            else -> networkDataSource.getLyrics(title).takeIf { it.isNotBlank() }.apply {
                cache[title] = this
            }
        }

        emit(lyrics)
    }.catch {
        Log.e(TAG, "getLyrics: ", it)
        emit(null)
    }

    companion object {
        private const val TAG = "DefaultLyricsRepository"
    }
}