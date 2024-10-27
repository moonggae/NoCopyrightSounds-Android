package com.ccc.ncs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
): ViewModel() {
    fun initGenreAndMood() {
        viewModelScope.launch {
            val genres = musicRepository.getGenres().firstOrNull()
            if (genres.isNullOrEmpty()) {
                musicRepository.initGenreAndMood().first()
            }
        }
    }
}