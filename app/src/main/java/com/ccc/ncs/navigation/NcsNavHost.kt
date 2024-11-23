package com.ccc.ncs.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.ccc.ncs.feature.artist.artistScreen
import com.ccc.ncs.feature.artist.detail.artistDetailScreen
import com.ccc.ncs.feature.artist.detail.navigateToArtistDetail
import com.ccc.ncs.feature.home.HOME_ROUTE
import com.ccc.ncs.feature.home.homeScreen
import com.ccc.ncs.feature.library.detail.navigateToPlaylistDetail
import com.ccc.ncs.feature.library.detail.playlistDetailScreen
import com.ccc.ncs.feature.library.edit.editPlaylistScreen
import com.ccc.ncs.feature.library.edit.navigateToEditPlaylist
import com.ccc.ncs.feature.library.libraryScreen
import com.ccc.ncs.feature.library.offline.navigateToOfflineMusic
import com.ccc.ncs.feature.library.offline.offlineMusicScreen
import com.ccc.ncs.feature.menu.cache.cacheSettingScreen
import com.ccc.ncs.feature.menu.cache.cacheSizeSettingScreen
import com.ccc.ncs.feature.menu.cache.navigateToCacheSetting
import com.ccc.ncs.feature.menu.cache.navigateToCacheSizeSetting
import com.ccc.ncs.feature.menu.menuScreen
import com.ccc.ncs.feature.music.backWithGenre
import com.ccc.ncs.feature.music.backWithMood
import com.ccc.ncs.feature.music.musicDetailScreen
import com.ccc.ncs.feature.music.navigateToMusicDetail
import com.ccc.ncs.feature.search.backFromSearch
import com.ccc.ncs.feature.search.navigateToSearch
import com.ccc.ncs.feature.search.searchScreen
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.ui.NcsAppState
import java.util.UUID

@Composable
fun NcsNavHost(
    appState: NcsAppState,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_ROUTE,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onPlayMusics: (List<UUID>) -> Unit,
    onPlayPlaylist: (PlayList, Int) -> Unit,
    onAddToQueue: (List<UUID>) -> Unit
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeScreen(
            onMoveToSearchScreen = { navController.navigateToSearch(it) },
            onShowSnackbar = onShowSnackbar,
            onPlayMusics = onPlayMusics,
            onAddToQueue = onAddToQueue,
            navigateToMusicDetail = { navController.navigateToMusicDetail(it) }
        )
        artistScreen(
            onMoveToSearchScreen = { navController.navigateToSearch(it) },
            onMoveToDetail = { navController.navigateToArtistDetail(it) }
        )
        artistDetailScreen(
            onBack = navController::navigateUp,
            onNavigateToMusicDetail = { navController.navigateToMusicDetail(it) },
            onNavigateToArtistDetail = { navController.navigateToArtistDetail(it.detailUrl) },
            onShowSnackbar = onShowSnackbar
        )
        libraryScreen(
            navigateToEdit = navController::navigateToEditPlaylist,
            navigateToDetail = { navController.navigateToPlaylistDetail(it.id) },
            navigateToDetailOfflineMusics = { navController.navigateToOfflineMusic() }
        )
        offlineMusicScreen(
            onBack = navController::navigateUp
        )
        menuScreen(
            onShowSnackbar = onShowSnackbar,
            onMoveCacheScreen = navController::navigateToCacheSetting
        )
        cacheSettingScreen(
            onBack = navController::navigateUp,
            onMoveToSettingSizeScreen = navController::navigateToCacheSizeSetting
        )
        cacheSizeSettingScreen(
            onBack = navController::navigateUp
        )
        searchScreen(onSearch = navController::backFromSearch)
        editPlaylistScreen(onBack = navController::navigateUp)
        playlistDetailScreen(
            onBack = navController::navigateUp,
            navigateToEditPlaylist = { navController.navigateToEditPlaylist(it) },
            onPlayPlaylist = onPlayPlaylist
        )
        musicDetailScreen(
            onBack = navController::navigateUp,
            onShowSnackbar = onShowSnackbar,
            onClickGenre = { navController.backWithGenre(it.id) },
            onClickMood = { navController.backWithMood(it.id) },
            onPlayMusics = onPlayMusics,
            onAddToQueue = onAddToQueue,
            navigateToArtistDetail = navController::navigateToArtistDetail
        )
    }
}