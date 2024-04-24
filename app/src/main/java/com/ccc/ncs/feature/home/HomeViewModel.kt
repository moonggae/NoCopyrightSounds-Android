package com.ccc.ncs.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ccc.ncs.data.repository.MusicRepository
import com.ccc.ncs.model.Music
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val musics = _uiState
        .distinctUntilChangedBy { it.searchUiState }
        .flatMapLatest {
            musicRepository
                .getSearchResultStream(
                    query = it.searchUiState.query,
                    genreId = it.searchUiState.genreId,
                    moodId = it.searchUiState.moodId
                )
                .cachedIn(viewModelScope)
        }

    init {
        viewModelScope.launch {
            musicRepository.initGenreAndMood()
        }
    }


    fun searchMusic(
        query: String?,
        genreId: Int?,
        moodId: Int?
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    searchUiState = SearchUiState(
                        query = query,
                        genreId = genreId,
                        moodId = moodId
                    )
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

    fun getAllGenres() = musicRepository.getGenres()
    fun getAllMoods() = musicRepository.getMoods()
}

data class HomeUiState(
    val searchUiState: SearchUiState = SearchUiState(),
    val isSelectMode: Boolean = false,
    val selectedMusics: MutableList<Music> = mutableListOf()
)

data class SearchUiState(
    val query: String? = null,
    val genreId: Int? = null,
    val moodId: Int? = null
)