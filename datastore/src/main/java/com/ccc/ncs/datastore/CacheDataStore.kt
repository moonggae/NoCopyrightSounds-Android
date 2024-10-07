package com.ccc.ncs.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheDataStore @Inject constructor (
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore by preferencesDataStore(name = "cache_data")

    val storageMbSize: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[CACHE_STORAGE_SIZE_KEY] ?: DEFAULT_STORAGE_MB
    }

    suspend fun setStorageMbSize(size: Int) {
        context.dataStore.edit { preferences ->
            preferences[CACHE_STORAGE_SIZE_KEY] = size
        }
    }

    val enableCache: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[CACHE_STORAGE_ENABLE_KEY] ?: DEFAULT_CACHE_ENABLE
    }

    suspend fun setEnableCache(enable: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[CACHE_STORAGE_ENABLE_KEY] = enable
        }
    }



    companion object {
        private val CACHE_STORAGE_SIZE_KEY = intPreferencesKey("CACHE_STORAGE_SIZE_KEY")
        private const val DEFAULT_STORAGE_MB = 1024

        private val CACHE_STORAGE_ENABLE_KEY = booleanPreferencesKey("CACHE_STORAGE_ENABLE_KEY")
        private const val DEFAULT_CACHE_ENABLE = true
    }
}