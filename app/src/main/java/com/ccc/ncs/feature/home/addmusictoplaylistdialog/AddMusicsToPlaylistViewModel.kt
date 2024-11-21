package com.ccc.ncs.feature.home.addmusictoplaylistdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.domain.repository.PlayListRepository
import com.ccc.ncs.domain.repository.PlayerRepository
import com.ccc.ncs.model.PlayList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddMusicsToPlaylistViewModel @Inject constructor(
    private val playlistRepository: com.ccc.ncs.domain.repository.PlayListRepository,
    private val playerRepository: com.ccc.ncs.domain.repository.PlayerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<AddMusicsToPlaylistUiState>(AddMusicsToPlaylistUiState.Loading)
    val uiState: StateFlow<AddMusicsToPlaylistUiState> = _uiState

    init {
        observePlayLists()
        observeCurrentPlaylist()
    }

    private fun observePlayLists() {
        viewModelScope.launch {
            playlistRepository.getPlayLists().collectLatest { newPlaylist ->
                _uiState.update { state ->
                    when (state) {
                        is AddMusicsToPlaylistUiState.Success -> state.copy(playlist = newPlaylist)
                        else -> AddMusicsToPlaylistUiState.Success(newPlaylist)
                    }
                }
            }
        }
    }

    private fun observeCurrentPlaylist() {
        viewModelScope.launch {
            playerRepository.playlist.collectLatest { currentPlaylist ->
                _uiState.update { state ->
                    when (state) {
                        is AddMusicsToPlaylistUiState.Success -> state.copy(currentPlaylist = currentPlaylist)
                        else -> AddMusicsToPlaylistUiState.Success(playlist = emptyList(), currentPlaylist = currentPlaylist)
                    }
                }
            }
        }
    }

    fun addMusicToPlaylist(playList: PlayList, musicIds: List<UUID>) {
        viewModelScope.launch {
            playlistRepository.setPlayListMusicsWithId(
                playListId = playList.id,
                musicIds = playList.musics.map { it.id }
                    .minus(musicIds)
                    .plus(musicIds)
            )
        }
    }


    companion object {
        private const val TAG = "AddMusicsToPlaylistViewModel"
    }
}

sealed interface AddMusicsToPlaylistUiState {
    data object Loading : AddMusicsToPlaylistUiState
    data class Success(
        val playlist: List<PlayList>,
        val currentPlaylist: PlayList? = null
    ) : AddMusicsToPlaylistUiState
}