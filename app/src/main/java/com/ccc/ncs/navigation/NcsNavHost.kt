package com.ccc.ncs.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.ccc.ncs.feature.artist.artistScreen
import com.ccc.ncs.feature.home.HOME_ROUTE
import com.ccc.ncs.feature.home.homeScreen
import com.ccc.ncs.feature.library.libraryScreen
import com.ccc.ncs.feature.setting.settingScreen
import com.ccc.ncs.ui.NcsAppState

@Composable
fun NcsNavHost(
    appState: NcsAppState,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_ROUTE
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeScreen()
        artistScreen()
        libraryScreen()
        settingScreen()
    }
}