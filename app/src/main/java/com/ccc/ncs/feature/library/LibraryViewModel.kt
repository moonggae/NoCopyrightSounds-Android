package com.ccc.ncs.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.PlayListRepository
import com.ccc.ncs.data.repository.PlayerRepository
import com.ccc.ncs.model.PlayList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val playListRepository: PlayListRepository,
    private val playerRepository: PlayerRepository
) : ViewModel() {
    private val _playListUiState: MutableStateFlow<PlayListUiState> = MutableStateFlow(PlayListUiState.Loading)
    val playListUiState: StateFlow<PlayListUiState> = _playListUiState

    init {
        observePlayLists()
        observeCurrentPlaylist()
    }

    private fun observePlayLists() {
        viewModelScope.launch {
            playListRepository.getPlayLists().collectLatest { newPlaylist ->
                _playListUiState.update { state ->
                    when (state) {
                        is PlayListUiState.Success -> state.copy(playLists = newPlaylist)
                        else -> PlayListUiState.Success(newPlaylist)
                    }
                }
            }
        }
    }

    private fun observeCurrentPlaylist() {
        viewModelScope.launch {
            playerRepository.playlist.collectLatest { currentPlaylist ->
                _playListUiState.update { state ->
                    when (state) {
                        is PlayListUiState.Success -> state.copy(currentPlaylist = currentPlaylist)
                        else -> PlayListUiState.Success(playLists = emptyList(), currentPlaylist = currentPlaylist)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "LibraryViewModel"
    }
}

sealed interface PlayListUiState {
    data object Loading : PlayListUiState
    data class Success(
        val playLists: List<PlayList>,
        val currentPlaylist: PlayList? = null
    ) : PlayListUiState
}