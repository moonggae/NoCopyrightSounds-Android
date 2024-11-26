package com.ccc.ncs.feature.artist.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ccc.ncs.feature.artist.ARTIST_ROUTE
import com.ccc.ncs.model.Artist
import com.ccc.ncs.navigation.noneTransitionComposable
import java.util.UUID


const val ARTIST_DETAIL_BASE_ROUTE = "$ARTIST_ROUTE/detail"
const val ARTIST_DETAIL_PATH_ARG = "artistDetailPath"
private const val ARTIST_DETAIL_ROUTE = "$ARTIST_DETAIL_BASE_ROUTE?$ARTIST_DETAIL_PATH_ARG={$ARTIST_DETAIL_PATH_ARG}"

fun NavController.navigateToArtistDetail(
    artistPath: String
) {
    val route = "$ARTIST_DETAIL_BASE_ROUTE?$ARTIST_DETAIL_PATH_ARG=$artistPath"
    navigate(route) {
        launchSingleTop = true

        val previousArtistPath = currentBackStackEntry?.arguments?.getString(ARTIST_DETAIL_PATH_ARG)
        if (previousArtistPath != artistPath) {
            launchSingleTop = false
        }
    }
}

fun NavGraphBuilder.artistDetailScreen(
    onBack: () -> Unit,
    onClose: () -> Unit,
    onNavigateToMusicDetail: (UUID) -> Unit,
    onNavigateToArtistDetail: (Artist) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    noneTransitionComposable(
        route = ARTIST_DETAIL_ROUTE,
        arguments = listOf(
            navArgument(ARTIST_DETAIL_PATH_ARG) {
                type = NavType.StringType
                nullable = false
            }
        )
    ) {
        ArtistDetailRoute(
            onBack = onBack,
            onClose = onClose,
            onMoveToMusicDetail = onNavigateToMusicDetail,
            onMoveToArtistDetail = onNavigateToArtistDetail,
            onShowSnackbar = onShowSnackbar
        )
    }
}