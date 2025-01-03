package com.ccc.ncs.feature.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ccc.ncs.data.paging.ArtistPagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    artistPagingData: ArtistPagingData
) : ViewModel() {
    private val _searchUiState: MutableStateFlow<ArtistSearchUiState> = MutableStateFlow(ArtistSearchUiState())
    val searchUiState: StateFlow<ArtistSearchUiState> get() = _searchUiState

    @OptIn(ExperimentalCoroutinesApi::class)
    val artists = _searchUiState
        .flatMapLatest { uiState ->
            artistPagingData.getData(
                query = uiState.query,
                sort = uiState.sort.query,
                year = uiState.year
            )
        }.cachedIn(viewModelScope)

    fun updateSearchQuery(query: String?) {
        _searchUiState.update {
            it.copy(query = query)
        }
    }

    fun updateSort(sort: ArtistSort) {
        _searchUiState.update {
            it.copy(sort = sort)
        }
    }

    fun updateYear(year: Int?) {
        _searchUiState.update {
            it.copy(year = year)
        }
    }

}


enum class ArtistSort(val query: String) {
    LATEST_ARTISTS("latest"),
    NAME_A_TO_Z("az"),
    NAME_Z_TO_A("za")
}

data class ArtistSearchUiState(
    val query: String? = null,
    val sort: ArtistSort = ArtistSort.LATEST_ARTISTS,
    val year: Int? = null
)