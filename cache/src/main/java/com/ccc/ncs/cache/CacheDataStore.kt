package com.ccc.ncs.cache

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

interface CacheDataStore {
    fun getStorageMbSizeFlow(): Flow<Int>

    fun getEnableCacheFlow(): Flow<Boolean>

    fun getStorageMbSize(): Int

    fun setStorageMbSize(size: Int)

    fun getEnableCache(): Boolean

    fun setEnableCache(enable: Boolean)
}

internal class DefaultCacheDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val scope: CoroutineScope
): CacheDataStore {
    private val Context.oldDataStore by preferencesDataStore(name = PREFERENCES_NAME)
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    init {
        if (needsMigration()) {
            scope.launch {
                migrateToSharedPreferences()
            }
        }
    }

    private fun needsMigration(): Boolean {
        return !prefs.getBoolean(MIGRATION_COMPLETED_KEY, false)
    }

    private suspend fun migrateToSharedPreferences() {
        try {
            val currentData = context.oldDataStore.data.first()

            prefs.edit().apply {
                currentData[intPreferencesKey(CACHE_STORAGE_SIZE_KEY)]?.let { size ->
                    putInt(CACHE_STORAGE_SIZE_KEY, size)
                }

                currentData[booleanPreferencesKey(CACHE_STORAGE_ENABLE_KEY)]?.let { enabled ->
                    putBoolean(CACHE_STORAGE_ENABLE_KEY, enabled)
                }

                putBoolean(MIGRATION_COMPLETED_KEY, true)
                apply()
            }

            context.oldDataStore.edit { it.clear() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getStorageMbSizeFlow(): Flow<Int> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == CACHE_STORAGE_SIZE_KEY) {
                trySend(getStorageMbSize())
            }
        }

        // 초기값
        trySend(getStorageMbSize())

        prefs.registerOnSharedPreferenceChangeListener(listener)

        awaitClose { // Flow 수집 종료시 실행
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }.distinctUntilChanged()

    override fun getEnableCacheFlow(): Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == CACHE_STORAGE_ENABLE_KEY) {
                trySend(getEnableCache())
            }
        }

        trySend(getEnableCache())

        prefs.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }.distinctUntilChanged()

    override fun getStorageMbSize(): Int {
        return prefs.getInt(CACHE_STORAGE_SIZE_KEY, DEFAULT_STORAGE_MB)
    }

    override fun setStorageMbSize(size: Int) {
        prefs.edit()
            .putInt(CACHE_STORAGE_SIZE_KEY, size)
            .apply()
    }

    override fun getEnableCache(): Boolean {
        return prefs.getBoolean(CACHE_STORAGE_ENABLE_KEY, DEFAULT_CACHE_ENABLE)
    }

    override fun setEnableCache(enable: Boolean) {
        prefs.edit()
            .putBoolean(CACHE_STORAGE_ENABLE_KEY, enable)
            .apply()
    }

    companion object {
        private const val PREFERENCES_NAME = "cache_data"
        private const val CACHE_STORAGE_SIZE_KEY = "CACHE_STORAGE_SIZE_KEY"
        private const val DEFAULT_STORAGE_MB = 1024
        private const val CACHE_STORAGE_ENABLE_KEY = "CACHE_STORAGE_ENABLE_KEY"
        private const val DEFAULT_CACHE_ENABLE = true
        private const val MIGRATION_COMPLETED_KEY = "MIGRATION_CACHE_TO_SHARED_PREFERENCES_COMPLETED"
    }
}