package com.ccc.ncs.feature.home

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ccc.ncs.feature.search.SEARCHED_QUERY
import com.ccc.ncs.model.Music
import java.util.UUID

const val HOME_ROUTE = "home"

internal const val SELECTED_GENRE_ID = "_SELECTED_GENRE_ID_"
internal const val SELECTED_MOOD_ID = "_SELECTED_MOOD_ID_"

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(HOME_ROUTE, navOptions)

fun NavGraphBuilder.homeScreen(
    onMoveToSearchScreen: (String?) -> Unit,
    onPlayMusics: (List<Music>) -> Unit,
    onAddToQueue: (List<Music>) -> Unit,
    navigateToMusicDetail: (UUID) -> Unit
) {
    composable(route = HOME_ROUTE) {
        val searchedQuery: String? by it.savedStateHandle.getStateFlow(SEARCHED_QUERY, null).collectAsStateWithLifecycle()
        val selectedGenreId: Int? by it.savedStateHandle.getStateFlow(SELECTED_GENRE_ID, null).collectAsStateWithLifecycle()
        val selectedMooId: Int? by it.savedStateHandle.getStateFlow(SELECTED_MOOD_ID, null).collectAsStateWithLifecycle()

        HomeRoute(
            onClickSearchBar = onMoveToSearchScreen,
            searchQuery = searchedQuery,
            selectedGenreId = selectedGenreId,
            selectedMooId = selectedMooId,
            onPlayMusics = onPlayMusics,
            onAddToQueue = onAddToQueue,
            navigateToMusicDetail = { music -> navigateToMusicDetail(music.id) }
        )
    }
}