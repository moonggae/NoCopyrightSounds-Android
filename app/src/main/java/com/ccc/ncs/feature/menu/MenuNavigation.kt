package com.ccc.ncs.feature.menu

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.ccc.ncs.navigation.noneTransitionComposable

const val MENU_ROUTE = "menu"

fun NavController.navigateToMenu(navOptions: NavOptions) = navigate(MENU_ROUTE, navOptions)

fun NavGraphBuilder.menuScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onMoveCacheScreen: () -> Unit
) {
    noneTransitionComposable(route = MENU_ROUTE) {
        MenuRoute(
            onShowSnackbar = onShowSnackbar,
            onMoveCacheScreen = onMoveCacheScreen
        )
    }
}