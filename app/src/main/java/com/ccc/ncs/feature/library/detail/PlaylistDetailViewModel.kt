package com.ccc.ncs.feature.library.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.PlayListRepository
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val playlistRepository: PlayListRepository
) : ViewModel() {
    val playListUiState: StateFlow<PlaylistDetailUiState> = savedStateHandle.getStateFlow(PLAYLIST_DETAIL_ID_ARG, "")
        .flatMapLatest { id ->
            if (id.isEmpty()) {
                flowOf(PlaylistDetailUiState.Loading)
            } else {
                playlistRepository.getPlayList(UUID.fromString(id))
                    .map { playlist ->
                        if (playlist == null) PlaylistDetailUiState.Fail
                        else PlaylistDetailUiState.Success(playlist)
                    }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlaylistDetailUiState.Loading
        )

    fun updateMusicList(playlistId: UUID, musicList: List<Music>) {
        playListUiState
        viewModelScope.launch {
            playlistRepository.setPlayListMusics(playlistId, musicList)
        }
    }

    companion object {
        private const val TAG = "PlaylistDetailViewModel"
    }
}

sealed interface PlaylistDetailUiState {
    data object Loading : PlaylistDetailUiState
    data object Fail : PlaylistDetailUiState
    data class Success(
        val playlist: PlayList
    ) : PlaylistDetailUiState
}