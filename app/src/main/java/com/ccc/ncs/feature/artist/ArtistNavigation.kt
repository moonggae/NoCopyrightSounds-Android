package com.ccc.ncs.feature.artist

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.ccc.ncs.feature.search.SEARCHED_QUERY
import com.ccc.ncs.navigation.noneTransitionComposable

const val ARTIST_ROUTE = "artist"

fun NavController.navigateToArtist(navOptions: NavOptions) = navigate(ARTIST_ROUTE, navOptions)

fun NavGraphBuilder.artistScreen(
    onMoveToSearchScreen: (String?) -> Unit,
    onMoveToDetail: (String) -> Unit
) {
    noneTransitionComposable(route = ARTIST_ROUTE) { backStackEntry ->
        val viewModel: ArtistViewModel = hiltViewModel()
        val searchedQuery: String? by backStackEntry.savedStateHandle.getStateFlow(SEARCHED_QUERY, null).collectAsStateWithLifecycle()

        LaunchedEffect(searchedQuery) {
            viewModel.updateSearchQuery(query = searchedQuery)
        }

        ArtistRoute(
            viewModel = viewModel,
            onClickSearchBar = onMoveToSearchScreen,
            onUpdateSearchQuery = { backStackEntry.savedStateHandle[SEARCHED_QUERY] = it },
            onClickArtist = { onMoveToDetail(it.detailUrl) }
        )
    }
}