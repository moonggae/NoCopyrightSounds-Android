package com.ccc.ncs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
): ViewModel() {
    val uiState: StateFlow<SplashUiState> = musicRepository.initGenreAndMood().map { success ->
        if (success) SplashUiState.Done
        else SplashUiState.Fail
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SplashUiState.Loading
    )
}


sealed class SplashUiState {
    data object Loading: SplashUiState()
    data object Done: SplashUiState()
    data object Fail: SplashUiState()
}