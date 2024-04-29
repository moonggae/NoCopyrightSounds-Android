package com.ccc.ncs.feature.menu

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val MENU_ROUTE = "menu"

fun NavController.navigateToMenu(navOptions: NavOptions) = navigate(MENU_ROUTE, navOptions)

fun NavGraphBuilder.menuScreen() {
    composable(route = MENU_ROUTE) {
        MenuRoute()
    }
}