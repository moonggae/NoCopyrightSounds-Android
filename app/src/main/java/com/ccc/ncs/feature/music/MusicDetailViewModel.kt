package com.ccc.ncs.feature.music

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.domain.repository.LyricsRepository
import com.ccc.ncs.domain.repository.MusicRepository
import com.ccc.ncs.download.MusicDownloader
import com.ccc.ncs.model.Music
import com.ccc.ncs.util.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class MusicDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val musicRepository: MusicRepository,
    private val lyricsRepository: LyricsRepository,
    private val musicDownloader: MusicDownloader,
) : ViewModel() {
    val uiState: StateFlow<MusicDetailUiState> = savedStateHandle.getStateFlow<String?>(MUSIC_DETAIL_ID_ARG, null)
        .map { musicIdString ->
            UUID.fromString(musicIdString)
        }.flatMapLatest { musicId ->
            musicRepository.getMusic(musicId)
        }.map { music ->
            if (music == null) throw Exception("Music not found")
            else {
                MusicDetailUiState.Success(
                    music = music,
                    lyrics = lyricsRepository.getLyrics(music.title).first()
                )
            }
        }.catch<MusicDetailUiState> {
            Log.e(TAG, "init uiState", it)
            emit(MusicDetailUiState.Fail)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = MusicDetailUiState.Loading
        )

    private val _errorEvents = MutableSharedFlow<String>()
    val errorEvents = _errorEvents.asSharedFlow()

    fun downloadMusic(musicId: UUID) {
        viewModelScope.launch {
            try {
                musicRepository.getMusic(musicId).first()?.let { music ->
                    musicDownloader.download(music)
                }
            } catch (e: Exception) {
                _errorEvents.emit(Const.ERROR_DOWNLOAD_MUSIC)
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