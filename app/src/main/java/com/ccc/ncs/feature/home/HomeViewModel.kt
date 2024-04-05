package com.ccc.ncs.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ccc.ncs.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicRepository: MusicRepository
): ViewModel() {
    val musics = musicRepository
        .getSearchResultStream()
        .cachedIn(viewModelScope)
}