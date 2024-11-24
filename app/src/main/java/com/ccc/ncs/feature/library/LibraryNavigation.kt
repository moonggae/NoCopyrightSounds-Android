package com.ccc.ncs.feature.library

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.navigation.noneTransitionComposable

const val LIBRARY_ROUTE = "library"

fun NavController.navigateToLibrary(navOptions: NavOptions) = navigate(LIBRARY_ROUTE, navOptions)

fun NavGraphBuilder.libraryScreen(
    navigateToDetail: (PlayList) -> Unit,
    navigateToDetailOfflineMusics: () -> Unit
) {
    noneTransitionComposable(route = LIBRARY_ROUTE) {
        LibraryRoute(
            onClickPlaylist = navigateToDetail,
            onClickOfflineMusics = navigateToDetailOfflineMusics
        )
    }
}