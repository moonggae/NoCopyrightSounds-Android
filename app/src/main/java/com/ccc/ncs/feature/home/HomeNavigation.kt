package com.ccc.ncs.feature.home

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.ccc.ncs.feature.search.SEARCHED_QUERY
import com.ccc.ncs.navigation.noneTransitionComposable
import java.util.UUID

const val HOME_ROUTE = "home"

internal const val SELECTED_GENRE_ID = "_SELECTED_GENRE_ID_"
internal const val SELECTED_MOOD_ID = "_SELECTED_MOOD_ID_"

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(HOME_ROUTE, navOptions)

fun NavGraphBuilder.homeScreen(
    onMoveToSearchScreen: (String?) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onPlayMusics: (List<UUID>) -> Unit,
    onAddToQueue: (List<UUID>) -> Unit,
    navigateToMusicDetail: (UUID) -> Unit
) {
    noneTransitionComposable(route = HOME_ROUTE) { backStackEntry ->
        val viewModel: HomeViewModel = hiltViewModel()
        val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()

        val searchedQuery: String? by backStackEntry.savedStateHandle.getStateFlow(SEARCHED_QUERY, null).collectAsStateWithLifecycle()
        val selectedGenreId: Int? by backStackEntry.savedStateHandle.getStateFlow(SELECTED_GENRE_ID, null).collectAsStateWithLifecycle()
        val selectedMoodId: Int? by backStackEntry.savedStateHandle.getStateFlow(SELECTED_MOOD_ID, null).collectAsStateWithLifecycle()

        LaunchedEffect(searchedQuery) {
            viewModel.onSearchQueryChanged(query = searchedQuery)
        }

        LaunchedEffect(selectedGenreId) {
            if (selectedGenreId != null) {
                homeUiState.genres.find { it.id == selectedGenreId }?.let { genre ->
                    viewModel.onUpdateTagFromDetail(genre)
                }
                backStackEntry.savedStateHandle[SELECTED_GENRE_ID] = null
            }
        }

        LaunchedEffect(selectedMoodId) {
            if (selectedMoodId != null) {
                homeUiState.moods.find { it.id == selectedMoodId }?.let { mood ->
                    viewModel.onUpdateTagFromDetail(mood)
                }
                backStackEntry.savedStateHandle[SELECTED_MOOD_ID] = null
            }
        }

        HomeRoute(
            viewModel = viewModel,
            onShowSnackbar = onShowSnackbar,
            onUpdateSearchQuery = { backStackEntry.savedStateHandle[SEARCHED_QUERY] = it },
            onClickSearchBar = onMoveToSearchScreen,
            onPlayMusics = onPlayMusics,
            onAddToQueue = onAddToQueue,
            navigateToMusicDetail = { musicId -> navigateToMusicDetail(musicId) }
        )
    }
}