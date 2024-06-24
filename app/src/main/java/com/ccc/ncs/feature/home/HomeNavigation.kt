package com.ccc.ncs.feature.home

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ccc.ncs.feature.search.SEARCHED_QUERY
import com.ccc.ncs.model.Music

const val HOME_ROUTE = "home"

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(HOME_ROUTE, navOptions)

fun NavGraphBuilder.homeScreen(
    onMoveToSearchScreen: (String?) -> Unit,
    onPlayMusics: (List<Music>) -> Unit,
    onAddToQueue: (List<Music>) -> Unit
) {
    composable(route = HOME_ROUTE) {
        val searchedQuery: String? by it.savedStateHandle.getStateFlow(SEARCHED_QUERY, null).collectAsStateWithLifecycle()
        HomeRoute(
            onClickSearchBar = onMoveToSearchScreen,
            searchQuery = searchedQuery,
            onPlayMusics = onPlayMusics,
            onAddToQueue = onAddToQueue
        )
    }
}