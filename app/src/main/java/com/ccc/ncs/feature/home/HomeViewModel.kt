package com.ccc.ncs.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.paging.cachedIn
import androidx.paging.map
import com.ccc.ncs.data.repository.MusicRepository
import com.ccc.ncs.download.MusicDownloader
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.MusicStatus
import com.ccc.ncs.model.MusicTag
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
class HomeViewModel @androidx.annotation.OptIn(UnstableApi::class)
@Inject constructor(
    private val musicRepository: MusicRepository,
    private val savedStateHandle: SavedStateHandle,
    private val musicDownloader: MusicDownloader,
    private val analytics: FirebaseAnalytics
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> get() = _uiState

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
            musicRepository.getSearchResultStream(
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
        analytics.logEvent("home_search_query") {
            param("query", "$query")
        }

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    searchUiState = state.searchUiState.copy(query = query)
                )
            }
        }
    }

    fun onSearchGenreChanged(genre: Genre?) {
        analytics.logEvent("home_search_genre") {
            param("genre", "${genre?.id}")
        }

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    searchUiState = state.searchUiState.copy(genre = genre)
                )
            }
        }
    }

    fun onSearchMoodChanged(mood: Mood?) {
        analytics.logEvent("home_search_mood") {
            param("mood", "${mood?.id}")
        }

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    searchUiState = state.searchUiState.copy(mood = mood)
                )
            }
        }
    }

    fun <T : MusicTag> onUpdateTagFromDetail(tag: T) {
        analytics.logEvent("home_update_tag_from_detail") {
            when (tag) {
                is Mood -> param("type", "Mood")
                is Genre -> param("type", "Genre")
            }
            param("id", tag.id.toLong())
        }

        viewModelScope.launch {
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
    }

    fun updateSelectMode(on: Boolean) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedMusicIds = if (on) it.selectedMusicIds else mutableListOf(),
                    isSelectMode = on
                )
            }
        }
    }

    fun updateSelectMusic(musicId: UUID) {
        if (!_uiState.value.isSelectMode) return

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedMusicIds = currentState.selectedMusicIds.toMutableList().apply {
                        if (contains(musicId)) remove(musicId) else add(musicId)
                    }
                )
            }
        }
    }

    fun downloadMusic(musicId: UUID) {
        analytics.logEvent("home_download_music") {
            param("music_id", "$musicId")
        }

        viewModelScope.launch {
            musicRepository.getMusic(musicId).first()?.let { music ->
                musicDownloader.download(music)
            }
        }
    }
}

data class HomeUiState(
    val searchUiState: SearchUiState = SearchUiState(),
    val isSelectMode: Boolean = false,
    val selectedMusicIds: MutableList<UUID> = mutableListOf(),
    val genres: List<Genre> = emptyList(),
    val moods: List<Mood> = emptyList()
)

data class SearchUiState(
    val query: String? = null,
    val genre: Genre? = null,
    val mood: Mood? = null
)