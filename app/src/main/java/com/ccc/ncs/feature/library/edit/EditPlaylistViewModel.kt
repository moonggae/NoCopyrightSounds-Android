package com.ccc.ncs.feature.library.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.domain.repository.PlayListRepository
import com.ccc.ncs.model.PlayList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    @OptIn(ExperimentalCoroutinesApi::class)
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
            started = SharingStarted.Lazily,
            initialValue = EditPlayListUiState.Loading
        )

    fun savePlaylist(name: String) {
        viewModelScope.launch {
            val currentState = uiState.value as? EditPlayListUiState.Success ?: return@launch

            if (currentState.playList != null) {
                playlistRepository.updatePlayListName(currentState.playList.id, name)
            } else {
                playlistRepository.insertPlayList(name, true)
            }
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