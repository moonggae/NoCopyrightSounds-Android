package com.ccc.ncs

import android.content.Context
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.ccc.ncs.data.repository.MusicCacheRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CacheViewModel @Inject constructor(
    private val cacheRepository: MusicCacheRepository,
) : ViewModel() {
    private var initCacheJob: Job? = null

    val uiState: StateFlow<CacheUiState> = combine(
        cacheRepository.maxSizeMb,
        cacheRepository.usedSizeBytes,
        cacheRepository.enableCache
    ) { maxMb, usedBytes, enableCache ->
        if (usedBytes != null) {
            initCacheJob?.cancel()

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


    @OptIn(UnstableApi::class)
    fun initCacheManager(
        context: Context
    ) {
        initCacheJob = viewModelScope.launch {
            uiState.collectLatest { state ->
                if (state.maxCacheSizeMb != null) {
                    cacheRepository.initialize(context, state.maxCacheSizeMb!!)
                }
            }
        }
    }

    fun setCacheEnable(enableCache: Boolean) {
        viewModelScope.launch {
            cacheRepository.setCacheEnable(enableCache)
        }
    }

    fun setCacheSize(mb: Int) {
        viewModelScope.launch {
            cacheRepository.setMaxSizeMb(mb)
        }
    }

    fun clearCache() {
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