package com.ccc.ncs.feature.music

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.LyricsRepository
import com.ccc.ncs.data.repository.MusicRepository
import com.ccc.ncs.model.Music
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class MusicDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val musicRepository: MusicRepository,
    private val lyricsRepository: LyricsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<MusicDetailUiState>(MusicDetailUiState.Loading)
    val uiState: StateFlow<MusicDetailUiState> = _uiState

    init {
        observeMusicDetail()
        observeLyrics()
    }

    private fun observeMusicDetail() {
        viewModelScope.launch {
            savedStateHandle.getStateFlow<String?>(MUSIC_DETAIL_ID_ARG, null)
                .map { musicIdString ->
                    Log.d(TAG, "observeMusicDetail - musicIdString: ${musicIdString}")
                    if (musicIdString == null) null
                    else UUID.fromString(musicIdString)
                }.flatMapLatest { musicId ->
                    if (musicId == null) flowOf(MusicDetailUiState.Loading)
                    else {
                        musicRepository.getMusic(musicId).map { music ->
                            Log.d(TAG, "observeMusicDetail - music: ${music}")
                            when (music) {
                                null -> MusicDetailUiState.Fail
                                else -> MusicDetailUiState.Success(music, null)
                            }
                        }
                    }
                }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MusicDetailUiState.Loading)
                .collectLatest(_uiState::emit)
        }
    }

    private fun observeLyrics() {
        viewModelScope.launch {
            _uiState.distinctUntilChangedBy { state ->
                when (state) {
                    is MusicDetailUiState.Success -> state.music
                    else -> state
                }
            }.flatMapLatest { state ->
                when (state) {
                    is MusicDetailUiState.Success -> {
                        val musicTitle = state.music.title
                        lyricsRepository.getLyrics(musicTitle)
                    }
                    else -> flowOf(null)
                }
            }.collect { lyrics ->
                _uiState.update {
                    if (it is MusicDetailUiState.Success) {
                        it.copy(lyrics = lyrics)
                    } else { it }
                }
            }
        }
    }

    companion object {
        private const val TAG = "MusicDetailViewModel"
    }
}

sealed interface MusicDetailUiState {
    data object Loading : MusicDetailUiState

    data object Fail : MusicDetailUiState

    data class Success(
        val music: Music,
        val lyrics: String?
    ) : MusicDetailUiState
}