package com.ccc.ncs.feature.library

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ccc.ncs.model.PlayList

const val LIBRARY_ROUTE = "library"

fun NavController.navigateToLibrary(navOptions: NavOptions) = navigate(LIBRARY_ROUTE, navOptions)

fun NavGraphBuilder.libraryScreen(
    navigateToEdit: () -> Unit,
    navigateToDetail: (PlayList) -> Unit,
    navigateToDetailOfflineMusics: () -> Unit
) {
    composable(route = LIBRARY_ROUTE) {
        LibraryRoute(
            onClickAddPlaylist = navigateToEdit,
            onClickPlaylist = navigateToDetail,
            onClickOfflineMusics = navigateToDetailOfflineMusics
        )
    }
}