package com.ccc.ncs.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.ccc.ncs.feature.artist.artistScreen
import com.ccc.ncs.feature.home.HOME_ROUTE
import com.ccc.ncs.feature.home.homeScreen
import com.ccc.ncs.feature.library.detail.navigateToPlaylistDetail
import com.ccc.ncs.feature.library.detail.playlistDetailScreen
import com.ccc.ncs.feature.library.edit.editPlaylistScreen
import com.ccc.ncs.feature.library.edit.navigateToEditPlaylist
import com.ccc.ncs.feature.library.libraryScreen
import com.ccc.ncs.feature.menu.menuScreen
import com.ccc.ncs.feature.search.backFromSearch
import com.ccc.ncs.feature.search.navigateToSearch
import com.ccc.ncs.feature.search.searchScreen
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.ui.NcsAppState

@Composable
fun NcsNavHost(
    appState: NcsAppState,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_ROUTE,
    onPlayMusics: (List<Music>) -> Unit,
    onPlayPlaylist: (PlayList) -> Unit,
    onAddToQueue: (List<Music>) -> Unit
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeScreen(
            onMoveToSearchScreen = { navController.navigateToSearch(it) },
            onPlayMusics = onPlayMusics,
            onAddToQueue = onAddToQueue
        )
        artistScreen()
        libraryScreen(
            navigateToEdit = navController::navigateToEditPlaylist,
            navigateToDetail = { navController.navigateToPlaylistDetail(it.id) }
        )
        menuScreen()
        searchScreen(onSearch = navController::backFromSearch)
        editPlaylistScreen(onBack = navController::popBackStack)
        playlistDetailScreen(
            onBack = navController::popBackStack,
            navigateToEditPlaylist = { navController.navigateToEditPlaylist(it) },
            onPlayPlaylist = onPlayPlaylist
        )
    }
}