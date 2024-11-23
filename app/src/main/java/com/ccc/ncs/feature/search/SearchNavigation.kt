package com.ccc.ncs.feature.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ccc.ncs.navigation.noneTransitionComposable


private const val SEARCH_BASE_ROUTE = "search"
const val SEARCH_QUERY_ARG = "query"
private const val SEARCH_ROUTE = "$SEARCH_BASE_ROUTE?$SEARCH_QUERY_ARG={$SEARCH_QUERY_ARG}"

const val SEARCHED_QUERY = "SEARCHED_QUERY"

fun NavController.navigateToSearch(
    query: String? = null
) {
    val route = if (query.isNullOrEmpty()) {
        SEARCH_BASE_ROUTE
    } else {
        "$SEARCH_BASE_ROUTE?$SEARCH_QUERY_ARG=$query"
    }

    navigate(route) { launchSingleTop = true }
}

fun NavController.backFromSearch(
    searchedQuery: String
) {
    previousBackStackEntry
        ?.savedStateHandle
        ?.set(SEARCHED_QUERY, searchedQuery)
    navigateUp()
}

fun NavGraphBuilder.searchScreen(
    onSearch: (String) -> Unit
) {
    noneTransitionComposable(
        route = SEARCH_ROUTE,
        arguments = listOf(
            navArgument(SEARCH_QUERY_ARG) {
                defaultValue = null
                nullable = true
                type = NavType.StringType
            }
        )
    ) {
        SearchRoute(
            onSearch = onSearch
        )
    }
}