package com.ccc.ncs.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.domain.repository.PlayListRepository
import com.ccc.ncs.domain.repository.PlayerRepository
import com.ccc.ncs.model.PlayList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    playListRepository: PlayListRepository,
    playerRepository: PlayerRepository
) : ViewModel() {
    val playListUiState = combine(
        playListRepository.getPlayLists(),
        playerRepository.playlist
    ) { playlists, currentPlaying ->
        PlayListUiState.Success(
            playLists = playlists,
            currentPlaylist = currentPlaying
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = PlayListUiState.Loading
    )

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