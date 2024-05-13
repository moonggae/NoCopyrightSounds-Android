package com.ccc.ncs.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.PlayListRepository
import com.ccc.ncs.model.PlayList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val playListRepository: PlayListRepository
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val playListUiState: StateFlow<PlayListUiState> = playListRepository
        .getPlayLists()
        .flatMapLatest {
            flowOf(PlayListUiState.Success(it))
        }.stateIn(
            scope = viewModelScope,
            initialValue = PlayListUiState.Loading,
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun addPlayList(name: String) {
        viewModelScope.launch {
            playListRepository.insertPlayList(name)
        }
    }

    companion object {
        private const val TAG = "LibraryViewModel"
    }
}

sealed interface PlayListUiState {
    data object Loading : PlayListUiState
    data class Success(
        val playLists: List<PlayList>
    ) : PlayListUiState
}