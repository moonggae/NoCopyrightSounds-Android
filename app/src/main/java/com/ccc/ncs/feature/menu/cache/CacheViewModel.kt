package com.ccc.ncs.feature.menu.cache

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.MusicCacheRepository
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CacheViewModel @Inject constructor(
    private val cacheRepository: MusicCacheRepository,
    private val analytics: FirebaseAnalytics
) : ViewModel() {
    val uiState: StateFlow<CacheUiState> = combine(
        cacheRepository.maxSizeMb,
        cacheRepository.usedSizeBytes,
        cacheRepository.enableCache
    ) { maxMb, usedBytes, enableCache ->
        if (usedBytes != null) {
            CacheUiState.Success(
                maxCacheSizeMb = maxMb,
                usedCacheSizeBytes = usedBytes,
                enableCache = enableCache
            )
        } else {
            CacheUiState.Loading(maxMb)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3000),
        initialValue = CacheUiState.Loading(null)
    )

    fun setCacheEnable(enableCache: Boolean) {
        analytics.logEvent("cache_enable") {
            param("enable", "$enableCache")
        }

        viewModelScope.launch {
            cacheRepository.setCacheEnable(enableCache)
        }
    }

    fun setCacheSize(mb: Int) {
        analytics.logEvent("cache_set_size") {
            param("mb", mb.toLong())
        }

        viewModelScope.launch {
            cacheRepository.setMaxSizeMb(mb)
        }
    }

    fun clearCache() {
        analytics.logEvent("cache_clear", null)

        cacheRepository.clearCache()
    }
}

sealed class CacheUiState(
    open val maxCacheSizeMb: Int?
) {

    data class Loading(
        override val maxCacheSizeMb: Int?
    ) : CacheUiState(maxCacheSizeMb)

    data class Success(
        override val maxCacheSizeMb: Int,
        val usedCacheSizeBytes: Long,
        val enableCache: Boolean
    ) : CacheUiState(maxCacheSizeMb) {
        val usedCacheSizeMb: Int get() = (usedCacheSizeBytes / 1024 / 1024).toInt()
    }
}