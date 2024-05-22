package com.ccc.ncs.feature.library.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.PlayListRepository
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
class EditPlaylistViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val playlistRepository: PlayListRepository
) : ViewModel() {
    val uiState: StateFlow<EditPlayListUiState> = savedStateHandle
        .getStateFlow(EDIT_PLAYLIST_ID_ARG, null)
        .flatMapLatest { it: String? ->
            val id = try {
                UUID.fromString(it)
            } catch (e: Throwable) {
                null
            } ?: return@flatMapLatest flowOf(null)
            playlistRepository.getPlayList(id)
        }
        .map {
            EditPlayListUiState.Success(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EditPlayListUiState.Loading
        )


    fun addPlaylist(name: String) {
        viewModelScope.launch {
            playlistRepository.insertPlayList(name, true)
        }
    }

    fun updatePlaylistName(id: UUID, name: String) {
        viewModelScope.launch {
            playlistRepository.updatePlayListName(id, name)
        }
    }

    companion object {
        private const val TAG = "EditPlaylistViewModel"
    }
}


sealed interface EditPlayListUiState {
    data object Loading : EditPlayListUiState
    data class Success(
        val playList: PlayList?
    ) : EditPlayListUiState
}