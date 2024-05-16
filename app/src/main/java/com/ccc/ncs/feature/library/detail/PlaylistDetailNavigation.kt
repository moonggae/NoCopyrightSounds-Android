package com.ccc.ncs.feature.library.detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import java.util.UUID


private const val PLAYLIST_DETAIL_BASE_ROUTE = "playlistdetail"
const val PLAYLIST_DETAIL_ID_ARG = "playlistId"
private const val PLAYLIST_DETAIL_ROUTE = "$PLAYLIST_DETAIL_BASE_ROUTE?$PLAYLIST_DETAIL_ID_ARG={$PLAYLIST_DETAIL_ID_ARG}"

fun NavController.navigateToPlaylistDetail(playlistId: UUID) =
    navigate("$PLAYLIST_DETAIL_BASE_ROUTE?$PLAYLIST_DETAIL_ID_ARG=$playlistId")

fun NavGraphBuilder.playlistDetailScreen(
    onBack: () -> Unit,
    navigateToEditPlaylist: (UUID) -> Unit
) {
    composable(
        route = PLAYLIST_DETAIL_ROUTE,
        arguments = listOf(
            navArgument(PLAYLIST_DETAIL_ID_ARG) {
                type = NavType.StringType
                nullable = false
            }
        )
    ) {
        PlaylistDetailRoute(
            onBack = onBack,
            onClickModifyName = navigateToEditPlaylist
        )
    }
}