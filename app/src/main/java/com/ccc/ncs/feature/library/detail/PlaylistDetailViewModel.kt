package com.ccc.ncs.feature.library.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.PlayListRepository
import com.ccc.ncs.data.repository.PlayerRepository
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.model.util.reorder
import com.ccc.ncs.playback.PlayerController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
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
    private val savedStateHandle: SavedStateHandle,
    private val playlistRepository: PlayListRepository,
    private val playerController: PlayerController,
    private val playerRepository: PlayerRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<PlaylistDetailUiState> = MutableStateFlow(PlaylistDetailUiState.Loading)
    val uiState = _uiState as StateFlow<PlaylistDetailUiState>

    init {
        observePlaylistDetail()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observePlaylistDetail() {
        viewModelScope.launch {
            savedStateHandle.getStateFlow(PLAYLIST_DETAIL_ID_ARG, "")
                .flatMapLatest { id ->
                    if (id.isEmpty()) flowOf(PlaylistDetailUiState.Loading)
                    else playlistRepository.getPlayList(UUID.fromString(id))
                        .map { playlist -> playlist?.let { PlaylistDetailUiState.Success(it) } ?: PlaylistDetailUiState.Fail }
                }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), PlaylistDetailUiState.Loading)
                .collectLatest {
                    _uiState.value = it
                    if (it is PlaylistDetailUiState.Success) {
                        observePlayingMusic()
                    }
                }
        }
    }

    private fun observePlayingMusic() {
        viewModelScope.launch {
            playerRepository.playlist.combine(playerRepository.musicIndex) { playlist, musicIndex ->
                when (val state = _uiState.value) {
                    is PlaylistDetailUiState.Success -> {
                        if (state.playlist.id == playlist?.id) {
                            state.copy(
                                playingMusic = playlist.musics.getOrNull(musicIndex ?: 0),
                                isPlaying = true
                            )
                        } else state
                    }
                    else -> state
                }
            }.collectLatest { _uiState.value = it }
        }
    }

    fun updateMusicOrder(prevIndex: Int, currentIndex: Int) {
        viewModelScope.launch {
            (_uiState.value as? PlaylistDetailUiState.Success)?.let { state ->
                val playlist = state.playlist
                val reorderedMusicList = playlist.musics.reorder(prevIndex, currentIndex)
                playlistRepository.setPlayListMusics(playlist.id, reorderedMusicList)
                if (playlist.id == playerRepository.playlist.first()?.id) {
                    playerController.moveMediaItem(prevIndex, currentIndex)
                }
            }
        }
    }

    fun deletePlaylist(playlistId: UUID) {
        viewModelScope.launch {
            playlistRepository.deletePlayList(playlistId)
            playerRepository.clear()
            playerController.stop()
        }
    }

    fun deleteMusic(music: Music) {
        viewModelScope.launch {
            (_uiState.value as? PlaylistDetailUiState.Success)?.let { state ->
                val playlist = state.playlist
                val updatedMusics = playlist.musics - music
                playlistRepository.setPlayListMusics(playlist.id, updatedMusics)
                if (playlist.id == playerRepository.playlist.first()?.id) {
                    playerController.removeMusic(music)
                }
            }
        }
    }
}

sealed interface PlaylistDetailUiState {
    data object Loading : PlaylistDetailUiState
    data object Fail : PlaylistDetailUiState
    data class Success(
        val playlist: PlayList,
        val isPlaying: Boolean = false,
        val playingMusic: Music? = null
    ) : PlaylistDetailUiState
}