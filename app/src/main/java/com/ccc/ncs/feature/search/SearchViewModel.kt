package com.ccc.ncs.feature.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalFoundationApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val searchQuery: StateFlow<String?> = savedStateHandle.getStateFlow(SEARCH_QUERY_ARG, null)

    @OptIn(ExperimentalFoundationApi::class)
    val queryState = TextFieldState()

    init {
        viewModelScope.launch {
            searchQuery.collectLatest {
                if (it == null) return@collectLatest
                queryState.setTextAndPlaceCursorAtEnd(it)
            }
        }
    }

    fun onSearchTriggered(query: String) {
        // todo
    }
}