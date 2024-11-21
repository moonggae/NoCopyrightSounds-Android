package com.ccc.ncs.feature.artist.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.ArtistRepository
import com.ccc.ncs.model.ArtistDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val artistRepository: ArtistRepository
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ArtistDetailUiState> = savedStateHandle.getStateFlow<String?>(ARTIST_DETAIL_PATH_ARG, null)
        .flatMapLatest { path ->
            when (path) {
                null -> MutableStateFlow(ArtistDetailUiState.Loading)
                else -> artistRepository.getArtistDetail(path).map { artistDetail ->
                    if (artistDetail.isSuccess) {
                        ArtistDetailUiState.Success(artistDetail.getOrThrow())
                    } else {
                        ArtistDetailUiState.Fail
                    }
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ArtistDetailUiState.Loading
        )

    companion object {
        private const val TAG = "ArtistDetailViewModel"
    }
}

sealed interface ArtistDetailUiState {
    data object Loading : ArtistDetailUiState
    data object Fail : ArtistDetailUiState
    data class Success(
        val artistDetail: ArtistDetail
    ) : ArtistDetailUiState
}