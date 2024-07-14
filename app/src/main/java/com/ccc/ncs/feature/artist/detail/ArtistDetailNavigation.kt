package com.ccc.ncs.feature.artist.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


private const val ARTIST_DETAIL_BASE_ROUTE = "artistDetail"
const val ARTIST_DETAIL_PATH_ARG = "artistDetailPath"
private const val ARTIST_DETAIL_ROUTE = "$ARTIST_DETAIL_BASE_ROUTE?$ARTIST_DETAIL_PATH_ARG={$ARTIST_DETAIL_PATH_ARG}"

fun NavController.navigateToArtistDetail(
    artistPath: String
) = navigate("$ARTIST_DETAIL_BASE_ROUTE?$ARTIST_DETAIL_PATH_ARG=$artistPath") { launchSingleTop = true }

fun NavGraphBuilder.artistDetailScreen() {
    composable(
        route = ARTIST_DETAIL_ROUTE,
        arguments = listOf(
            navArgument(ARTIST_DETAIL_PATH_ARG) {
                type = NavType.StringType
                nullable = false
            }
        )
    ) {
        ArtistDetailRoute()
    }
}