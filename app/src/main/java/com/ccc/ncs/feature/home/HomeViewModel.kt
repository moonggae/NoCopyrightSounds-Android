package com.ccc.ncs.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ccc.ncs.data.repository.MusicRepository
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> get() = _uiState

    @OptIn(ExperimentalCoroutinesApi::class)
    val musics = uiState
        .distinctUntilChangedBy { it.searchUiState }
        .flatMapLatest {
            musicRepository
                .getSearchResultStream(
                    query = it.searchUiState.query,
                    genreId = it.searchUiState.genre?.id,
                    moodId = it.searchUiState.mood?.id
                )
        }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            musicRepository.initGenreAndMood()

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
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    searchUiState = state.searchUiState.copy(query = query)
                )
            }
        }
    }

    fun onSearchGenreChanged(genre: Genre?) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    searchUiState = state.searchUiState.copy(genre = genre)
                )
            }
        }
    }

    fun onSearchMoodChanged(mood: Mood?) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    searchUiState = state.searchUiState.copy(mood = mood)
                )
            }
        }
    }

    fun updateSelectMode(on: Boolean) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedMusics = if (on) it.selectedMusics else mutableListOf(),
                    isSelectMode = on
                )
            }
        }
    }

    fun updateSelectMusic(music: Music) {
        if (!_uiState.value.isSelectMode) return

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedMusics = currentState.selectedMusics.toMutableList().apply {
                        if (contains(music)) remove(music) else add(music)
                    }
                )
            }
        }
    }
}

data class HomeUiState(
    val searchUiState: SearchUiState = SearchUiState(),
    val isSelectMode: Boolean = false,
    val selectedMusics: MutableList<Music> = mutableListOf(),
    val genres: List<Genre> = emptyList(),
    val moods: List<Mood> = emptyList()
)

data class SearchUiState(
    val query: String? = null,
    val genre: Genre? = null,
    val mood: Mood? = null
)