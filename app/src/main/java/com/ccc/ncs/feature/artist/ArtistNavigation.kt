package com.ccc.ncs.feature.artist

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ccc.ncs.feature.search.SEARCHED_QUERY

const val ARTIST_ROUTE = "artist"

fun NavController.navigateToArtist(navOptions: NavOptions) = navigate(ARTIST_ROUTE, navOptions)

fun NavGraphBuilder.artistScreen(
    onMoveToSearchScreen: (String?) -> Unit
) {
    composable(route = ARTIST_ROUTE) { backStackEntry ->
        val viewModel: ArtistViewModel = hiltViewModel()
        val searchedQuery: String? by backStackEntry.savedStateHandle.getStateFlow(SEARCHED_QUERY, null).collectAsStateWithLifecycle()

        LaunchedEffect(searchedQuery) {
            viewModel.updateSearchQuery(query = searchedQuery)
        }

        ArtistRoute(
            viewModel = viewModel,
            onClickSearchBar = onMoveToSearchScreen,
            onUpdateSearchQuery = { backStackEntry.savedStateHandle[SEARCHED_QUERY] = it },
            onClickArtist = { /* TODO: move to artist detail page */ }
        )
    }
}