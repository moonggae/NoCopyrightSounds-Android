package com.ccc.ncs.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ccc.ncs.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicRepository: MusicRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> get() = _uiState

    @OptIn(ExperimentalCoroutinesApi::class)
    val musics = _uiState
        .flatMapLatest {
            musicRepository
                .getSearchResultStream(
                    query = it.query,
                    genreId = it.genreId,
                    moodId = it.moodId
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
                    query = query,
                    genreId = genreId,
                    moodId = moodId
                )
            }
        }
    }

    fun getAllGenres() = musicRepository.getGenres()
    fun getAllMoods() = musicRepository.getMoods()
}

data class HomeUiState(
    val query: String? = null,
    val genreId: Int? = null,
    val moodId: Int? = null
)