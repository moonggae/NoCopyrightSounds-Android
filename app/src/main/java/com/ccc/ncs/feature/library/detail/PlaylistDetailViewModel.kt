package com.ccc.ncs.feature.library.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.domain.repository.PlayListRepository
import com.ccc.ncs.domain.usecase.DeletePlaylistMusicUseCase
import com.ccc.ncs.domain.usecase.DeletePlaylistUseCase
import com.ccc.ncs.domain.usecase.GetPlayerStateUseCase
import com.ccc.ncs.domain.usecase.PlayPlaylistUseCase
import com.ccc.ncs.domain.usecase.UpdatePlaylistMusicOrderUseCase
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val playlistRepository: PlayListRepository,
    private val playPlaylistUseCase: PlayPlaylistUseCase,
    private val updatePlaylistMusicOrderUseCase: UpdatePlaylistMusicOrderUseCase,
    private val deletePlaylistMusicUseCase: DeletePlaylistMusicUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    getPlayerStateUseCase: GetPlayerStateUseCase
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<PlaylistDetailUiState> = savedStateHandle.getStateFlow<String?>(PLAYLIST_DETAIL_ID_ARG, null)
        .flatMapLatest { idString ->
            if (idString == null) flowOf(null)
            else playlistRepository.getPlayList(UUID.fromString(idString))
        }.combine(getPlayerStateUseCase()) { playlist, playerState ->
            when {
                playlist == null -> PlaylistDetailUiState.Deleted
                else -> {
                    val isPlayingCurrentPlaylist = playerState?.playlist?.id == playlist.id
                    val currentPlayingMusic = if (isPlayingCurrentPlaylist) playerState?.currentMusic else null
                    PlaylistDetailUiState.Success(
                        playlist = playlist,
                        isPlaying = isPlayingCurrentPlaylist,
                        playingMusic = currentPlayingMusic
                    )
                }
            }
        }.catch<PlaylistDetailUiState> {
            Log.e(TAG, "init uiState", it)
            emit(PlaylistDetailUiState.Fail)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = PlaylistDetailUiState.Loading
        )

    fun updateMusicOrder(prevIndex: Int, currentIndex: Int) {
        viewModelScope.launch {
            (uiState.value as? PlaylistDetailUiState.Success)?.let { state ->
                updatePlaylistMusicOrderUseCase(state.playlist.id, prevIndex, currentIndex)
                    .onFailure {
                        Log.e(TAG, "updateMusicOrder", it)
                    }
            }
        }
    }

    fun deletePlaylist(playlistId: UUID) {
        viewModelScope.launch {
            deletePlaylistUseCase(playlistId).onFailure {
                Log.e(TAG, "deletePlaylist", it)
            }
        }
    }

    fun deleteMusic(music: Music) {
        viewModelScope.launch {
            (uiState.value as? PlaylistDetailUiState.Success)?.let { state ->
                deletePlaylistMusicUseCase(state.playlist.id, music).onFailure {
                    Log.e(TAG, "deleteMusic", it)
                }
            }
        }
    }

    fun playPlaylist(playlist: PlayList, startIndex: Int) {
        viewModelScope.launch {
            playPlaylistUseCase(playlist.id, startIndex).onFailure {
                Log.e(TAG, "playPlaylist", it)
            }
        }
    }

    companion object {
        private const val TAG = "PlaylistDetailViewModel"
    }
}

sealed interface PlaylistDetailUiState {
    data object Loading: PlaylistDetailUiState
    data object Fail: PlaylistDetailUiState
    data class Success(
        val playlist: PlayList,
        val isPlaying: Boolean = false,
        val playingMusic: Music? = null
    ): PlaylistDetailUiState
    data object Deleted: PlaylistDetailUiState
}