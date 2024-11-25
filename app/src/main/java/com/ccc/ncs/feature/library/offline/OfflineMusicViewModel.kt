package com.ccc.ncs.feature.library.offline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.domain.repository.MusicRepository
import com.ccc.ncs.domain.repository.MusicCacheRepository
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.MusicStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class OfflineMusicViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val musicCacheRepository: MusicCacheRepository
) : ViewModel() {
    val uiState: StateFlow<OfflineMusicUiState> = musicRepository
        .getMusicsByStatus(listOf(MusicStatus.Downloaded(""), MusicStatus.FullyCached))
        .map { musics ->
            OfflineMusicUiState.Success(musics)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1000),
            initialValue = OfflineMusicUiState.Loading
        )

    fun deleteMusics(uuids: List<UUID>) {
        viewModelScope.launch {
            val (downloadedMusics, cachedMusics) = uiState
                .filterIsInstance(OfflineMusicUiState.Success::class)
                .first()
                .musics
                .filter {
                    uuids.contains(it.id) &&
                    (it.status is MusicStatus.Downloaded || it.status is MusicStatus.FullyCached)
                }
                .partition { it.status is MusicStatus.Downloaded }

            musicRepository.removeDownloadedMusic(*downloadedMusics.map { it.id }.toTypedArray())
            musicCacheRepository.removeCachedMusic(*cachedMusics.map { it.id }.toTypedArray())
        }
    }

    companion object {
        private const val TAG = "OfflineMusicViewModel"
    }
}

sealed interface OfflineMusicUiState {
    data object Loading : OfflineMusicUiState
    data class Success(
        val musics: List<Music>
    ) : OfflineMusicUiState
}