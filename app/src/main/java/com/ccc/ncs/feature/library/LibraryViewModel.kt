package com.ccc.ncs.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.PlayListRepository
import com.ccc.ncs.data.repository.PlayerRepository
import com.ccc.ncs.model.PlayList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val playListRepository: PlayListRepository,
    private val playerRepository: PlayerRepository
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
        started = SharingStarted.WhileSubscribed(5000),
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