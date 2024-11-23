package com.ccc.ncs.feature.home.addmusictoplaylistdialog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.domain.repository.PlayListRepository
import com.ccc.ncs.domain.repository.PlayerRepository
import com.ccc.ncs.domain.usecase.AddPlaylistMusicUseCase
import com.ccc.ncs.model.PlayList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddMusicsToPlaylistViewModel @Inject constructor(
    playlistRepository: PlayListRepository,
    playerRepository: PlayerRepository,
    private val addPlaylistMusicUseCase: AddPlaylistMusicUseCase
) : ViewModel() {
    val uiState = combine(
        playlistRepository.getPlayLists(),
        playerRepository.playlist
    ) { playlist, currentPlaylist ->
        AddMusicsToPlaylistUiState.Success(
            playlist = playlist,
            currentPlaylist = currentPlaylist
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = AddMusicsToPlaylistUiState.Loading
    )

    fun addMusicToPlaylist(playList: PlayList, musicIds: List<UUID>) {
        viewModelScope.launch {
            addPlaylistMusicUseCase(playList.id, musicIds).onFailure {
                Log.e(TAG, "addMusicToPlaylist", it)
            }
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