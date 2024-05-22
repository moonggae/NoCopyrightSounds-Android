package com.ccc.ncs.feature.library.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.PlayListRepository
import com.ccc.ncs.data.repository.PlayerRepository
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.playback.PlayerController
import com.ccc.ncs.util.swap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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
    private val playlistRepository: PlayListRepository,
    private val playerController: PlayerController,
    private val playerRepository: PlayerRepository
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
        viewModelScope.launch {
            playlistRepository.setPlayListMusics(playlistId, musicList)
        }
    }

    fun updateMusicOrder(prevIndex: Int, currentIndex: Int) {
        viewModelScope.launch {
            playListUiState
                .value
                .takeIf { it is PlaylistDetailUiState.Success }
                ?.let {
                    val playlist = (it as PlaylistDetailUiState.Success).playlist
                    if (playlist.id == playerRepository.playlist.first()?.id) {
                        val reorderedMusicList = playlist.musics.swap(prevIndex, currentIndex)
                        updateMusicList(playlist.id, reorderedMusicList)
                        playerController.moveMediaItem(prevIndex, currentIndex)
                    }
                }
        }
    }

    fun deletePlaylist(playlistId: UUID) {
        viewModelScope.launch {
            playlistRepository.deletePlayList(playlistId)
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