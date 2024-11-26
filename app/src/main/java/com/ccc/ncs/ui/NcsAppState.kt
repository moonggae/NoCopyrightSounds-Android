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
import com.ccc.ncs.feature.artist.navigateToArtist
import com.ccc.ncs.feature.home.navigateToHome
import com.ccc.ncs.feature.library.navigateToLibrary
import com.ccc.ncs.feature.menu.navigateToMenu
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
        @Composable get() = TopLevelDestination.entries.find { it.route == currentDestination?.route }

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
            TopLevelDestination.MENU -> navController.navigateToMenu(topLevelNavOptions)
        }
    }

    fun popNearestTopLevelDestination() {
        // HOME_ROUTE 는 항상 첫번째에 위치하기 떄문에 마지막에 검색
        val topLevelRoutes = TopLevelDestination
            .entries
            .reversed()
            .map(TopLevelDestination::route)

        val destination = topLevelRoutes
            .firstNotNullOfOrNull { route ->
                try { navController.getBackStackEntry(route) }
                catch (e: IllegalArgumentException) { null }
            }

        destination?.let {
            navController.popBackStack(it.destination.id, false)
        }
    }
}