package com.ccc.ncs.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.ccc.ncs.data.util.NetworkMonitor
import com.ccc.ncs.feature.artist.ARTIST_ROUTE
import com.ccc.ncs.feature.artist.navigateToArtist
import com.ccc.ncs.feature.home.HOME_ROUTE
import com.ccc.ncs.feature.home.navigateToHome
import com.ccc.ncs.feature.library.LIBRARY_ROUTE
import com.ccc.ncs.feature.library.navigateToLibrary
import com.ccc.ncs.feature.setting.SETTING_ROUTE
import com.ccc.ncs.feature.setting.navigateToSetting
import com.ccc.ncs.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberNcsAppState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): NcsAppState {
    return remember(
        navController,
        coroutineScope,
        networkMonitor
    ) {
        NcsAppState(
            navController = navController,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor
        )
    }
}

@Stable
class NcsAppState(
    val navController: NavHostController,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            HOME_ROUTE -> TopLevelDestination.HOME
            ARTIST_ROUTE -> TopLevelDestination.ARTIST
            LIBRARY_ROUTE -> TopLevelDestination.LIBRARY
            SETTING_ROUTE -> TopLevelDestination.SETTING
            else -> null
        }

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateToHome(topLevelNavOptions)
            TopLevelDestination.ARTIST -> navController.navigateToArtist(topLevelNavOptions)
            TopLevelDestination.LIBRARY -> navController.navigateToLibrary(topLevelNavOptions)
            TopLevelDestination.SETTING -> navController.navigateToSetting(topLevelNavOptions)
        }
    }
}