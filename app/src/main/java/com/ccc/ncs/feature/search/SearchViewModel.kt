package com.ccc.ncs.feature.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.RecentSearchRepository
import com.ccc.ncs.model.RecentSearchQuery
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recentSearchRepository: RecentSearchRepository,
    private val analytics: FirebaseAnalytics
) : ViewModel() {
    private val searchQuery: StateFlow<String?> = savedStateHandle.getStateFlow(SEARCH_QUERY_ARG, null)

    val queryState = TextFieldState()

    val recentSearchQueriesUiState: StateFlow<RecentSearchQueriesUiState> =
        recentSearchRepository
            .getRecentSearchQueries(10)
            .map(RecentSearchQueriesUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = RecentSearchQueriesUiState.Loading,
            )

    init {
        viewModelScope.launch {
            searchQuery.collectLatest {
                if (it == null) return@collectLatest
                queryState.setTextAndPlaceCursorAtEnd(it)
            }
        }
    }

    fun onSearchTriggered(query: String) {
        analytics.logEvent("search_triggered") {
            param("query", query)
        }

        if (query.isBlank()) return
        viewModelScope.launch {
            recentSearchRepository.insertOrReplaceRecentSearch(searchQuery = query)
        }
    }

    fun deleteRecentSearch(query: String) {
        analytics.logEvent("search_delete") {
            param("query", query)
        }

        viewModelScope.launch {
            recentSearchRepository.deleteRecentSearch(query)
        }
    }
}

sealed interface RecentSearchQueriesUiState {
    data object Loading : RecentSearchQueriesUiState

    data class Success(
        val recentQueries: List<RecentSearchQuery> = emptyList(),
    ) : RecentSearchQueriesUiState
}
