package com.ccc.ncs.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.ccc.ncs.data.paging.MusicPagingData
import com.ccc.ncs.domain.repository.MusicRepository
import com.ccc.ncs.download.MusicDownloader
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.MusicStatus
import com.ccc.ncs.model.MusicTag
import com.ccc.ncs.util.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val savedStateHandle: SavedStateHandle,
    private val musicDownloader: MusicDownloader,
    musicPagingData: MusicPagingData
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> get() = _uiState

    private val _errorEvents = MutableSharedFlow<String>()
    val errorEvents = _errorEvents.asSharedFlow()

    private val statusMusics = musicRepository
        .getMusicsByStatus(listOf(MusicStatus.FullyCached, MusicStatus.Downloading, MusicStatus.Downloaded("")))
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val musics = uiState
        .distinctUntilChangedBy { it.searchUiState }
        .flatMapLatest { state ->
            musicPagingData.getData(
                query = state.searchUiState.query,
                genreId = state.searchUiState.genre?.id,
                moodId = state.searchUiState.mood?.id
            )
        }.cachedIn(viewModelScope)
        .combine(statusMusics) { pagingData, localMusic ->
            pagingData.map { data ->
                val localStatusMusic = localMusic.firstOrNull { it.id == data.id }
                data.copy(
                    status = localStatusMusic?.status ?: MusicStatus.None
                )
            }
        }


    init {
        viewModelScope.launch {
            launch {
                musicRepository.getGenres().collectLatest { genres ->
                    _uiState.update {
                        it.copy(genres = genres)
                    }
                }
            }

            launch {
                musicRepository.getMoods().collectLatest { moods ->
                    _uiState.update {
                        it.copy(moods = moods)
                    }
                }
            }
        }
    }


    fun onSearchQueryChanged(query: String?) {
        _uiState.update { state ->
            state.copy(
                searchUiState = state.searchUiState.copy(query = query)
            )
        }
    }

    fun onSearchGenreChanged(genre: Genre?) {
        _uiState.update { state ->
            state.copy(
                searchUiState = state.searchUiState.copy(genre = genre)
            )
        }
    }

    fun onSearchMoodChanged(mood: Mood?) {
        _uiState.update { state ->
            state.copy(
                searchUiState = state.searchUiState.copy(mood = mood)
            )
        }
    }

    fun <T : MusicTag> onUpdateTagFromDetail(tag: T) {
        _uiState.update { state ->
            val updateState = when (tag) {
                is Genre -> state.copy(
                    searchUiState = state.searchUiState.copy(
                        genre = tag,
                        mood = null,
                        query = null
                    )
                )

                is Mood -> state.copy(
                    searchUiState = state.searchUiState.copy(
                        mood = tag,
                        genre = null,
                        query = null
                    )
                )

                else -> state
            }
            updateState
        }

        updateSelectMode(false)
    }

    fun updateSelectMode(on: Boolean) {
        _uiState.update {
            it.copy(
                selectedMusicIds = if (on) it.selectedMusicIds else mutableListOf(),
                isSelectMode = on
            )
        }
    }

    fun updateSelectMusic(musicId: UUID) {
        if (!_uiState.value.isSelectMode) return

        _uiState.update { currentState ->
            currentState.copy(
                selectedMusicIds = currentState.selectedMusicIds.toMutableList().apply {
                    if (contains(musicId)) remove(musicId) else add(musicId)
                }
            )
        }
    }

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
}

data class HomeUiState(
    val searchUiState: SearchUiState = SearchUiState(),
    val isSelectMode: Boolean = false,
    val selectedMusicIds: List<UUID> = listOf(),
    val genres: List<Genre> = emptyList(),
    val moods: List<Mood> = emptyList()
)

data class SearchUiState(
    val query: String? = null,
    val genre: Genre? = null,
    val mood: Mood? = null
)