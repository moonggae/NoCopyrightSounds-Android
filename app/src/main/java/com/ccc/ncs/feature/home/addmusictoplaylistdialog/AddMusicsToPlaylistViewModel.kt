package com.ccc.ncs.feature.home.addmusictoplaylistdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.PlayListRepository
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMusicsToPlaylistViewModel @Inject constructor(
    private val playlistRepository: PlayListRepository
) : ViewModel() {
    val uiState: StateFlow<AddMusicsToPlaylistUiState> = playlistRepository.getPlayLists()
        .map(AddMusicsToPlaylistUiState::Success)
        .stateIn(
            scope = viewModelScope,
            initialValue = AddMusicsToPlaylistUiState.Loading,
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun addMusicToPlaylist(playList: PlayList, musics: List<Music>) {
        viewModelScope.launch {
            playlistRepository.setPlayListMusics(
                playListId = playList.id,
                musics = playList.musics.toMutableList().plus(musics)
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
        val playlist: List<PlayList>
    ) : AddMusicsToPlaylistUiState
}