package com.ccc.ncs.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.NcsNavigationBar
import com.ccc.ncs.designsystem.component.NcsNavigationBarItem
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.feature.artist.detail.navigateToArtistDetail
import com.ccc.ncs.feature.music.navigateToMusicDetail
import com.ccc.ncs.feature.play.PlayerScreen
import com.ccc.ncs.feature.play.PlayerUiState
import com.ccc.ncs.feature.play.PlayerViewModel
import com.ccc.ncs.navigation.NcsNavHost
import com.ccc.ncs.navigation.TopLevelDestination

@Composable
fun NcsApp(
    appState: NcsAppState,
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var playingScreenHeightWeight by remember { mutableFloatStateOf(0f) }
    val playerUiState by playerViewModel.playerUiState.collectAsStateWithLifecycle()

    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    HandleOfflineStatus(isOffline, snackbarHostState)

    LaunchedEffect(playerUiState) {
        if (playerUiState is PlayerUiState.Loading) {
            playingScreenHeightWeight = 0f
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (appState.currentTopLevelDestination != null) {
                NcsBottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination,
                    visibility = 1 - playingScreenHeightWeight
                )
            }
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(Modifier.fillMaxSize()) {
                NcsNavHost(
                    appState = appState,
                    onShowSnackbar = { message, action ->
                        snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = action,
                            duration = Short,
                        ) == ActionPerformed
                    },
                    onPlayMusics = playerViewModel::playMusicWithRecentPlaylist,
                    onPlayPlaylist = playerViewModel::playPlayList,
                    onAddToQueue = playerViewModel::addQueueToCurrentPlaylist,
                    addToQueueAndPlayMusic = playerViewModel::addToQueueAndPlay
                )
            }

            if (appState.currentTopLevelDestination != null && playerUiState is PlayerUiState.Success) {
                PlayerScreen(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onUpdateScreenSize = { percentage ->
                        playingScreenHeightWeight = percentage
                    },
                    playerUiState = playerUiState as PlayerUiState.Success,
                    onPlay = playerViewModel::playPause,
                    onSkipPrevious = playerViewModel::prev,
                    onSkipNext = playerViewModel::next,
                    onSeekTo = playerViewModel::setPosition,
                    onChangeRepeatMode = playerViewModel::setRepeatMode,
                    onShuffle = playerViewModel::setShuffleMode,
                    onUpdateMusicOrder = playerViewModel::updateMusicOrder,
                    onClickOnList = playerViewModel::seekToMusic,
                    onClose = playerViewModel::closePlayer,
                    onMoveToMusicDetail = { appState.navController.navigateToMusicDetail(it.id) },
                    onMoveToArtistDetail = { appState.navController.navigateToArtistDetail(it.detailUrl) },
                    onDeleteMusicInPlaylist = playerViewModel::removeFromQueue
                )
            }
        }
    }
}

@Composable
private fun HandleOfflineStatus(isOffline: Boolean, snackbarHostState: SnackbarHostState) {
    val notConnectedMessage = stringResource(R.string.not_connected)
    LaunchedEffect(isOffline) {
        if (isOffline) {
            snackbarHostState.showSnackbar(
                message = notConnectedMessage,
                duration = SnackbarDuration.Indefinite
            )
        }
    }
}

@Composable
private fun NcsBottomBar(
    modifier: Modifier = Modifier,
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    visibility: Float = 1f,
) {
    val contentHeight = 60.dp
    val navigationBarInsertHeight = with(LocalDensity.current) { WindowInsets.navigationBars.getBottom(this).toDp() }

    val height = (contentHeight + navigationBarInsertHeight) * visibility

    NcsNavigationBar(modifier = modifier.then(
        Modifier
            .height(height)
            .alpha(visibility)
    )) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)

            NcsNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = { Icon(imageVector = destination.unselectedIcon, contentDescription = stringResource(destination.iconTextId)) },
                selectedIcon = { Icon(imageVector = destination.selectedIcon, contentDescription = stringResource(destination.iconTextId)) },
                label = { Text(text = stringResource(destination.iconTextId), style = NcsTypography.Label.navigationLabel) },
                modifier = modifier
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
