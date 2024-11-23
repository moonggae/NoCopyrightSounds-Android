package com.ccc.ncs.feature.library.edit

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ccc.ncs.navigation.noneTransitionComposable
import java.util.UUID

private const val EDIT_PLAYLIST_BASE_ROUTE = "editplaylist"
const val EDIT_PLAYLIST_ID_ARG = "id"
private const val EDIT_PLAYLIST_ROUTE = "$EDIT_PLAYLIST_BASE_ROUTE?$EDIT_PLAYLIST_ID_ARG={$EDIT_PLAYLIST_ID_ARG}"

fun NavController.navigateToEditPlaylist(
    id: UUID? = null
) {
    if (currentBackStackEntry?.destination?.route?.startsWith(EDIT_PLAYLIST_BASE_ROUTE) == true) return

    val route = if (id == null) {
        EDIT_PLAYLIST_BASE_ROUTE
    } else {
        "$EDIT_PLAYLIST_BASE_ROUTE?$EDIT_PLAYLIST_ID_ARG=$id"
    }

    navigate(route) { launchSingleTop = true }
}

fun NavGraphBuilder.editPlaylistScreen(
    onBack: () -> Unit
) {
    noneTransitionComposable(
        route = EDIT_PLAYLIST_ROUTE,
        arguments = listOf(
            navArgument(EDIT_PLAYLIST_ID_ARG) {
                defaultValue = null
                nullable = true
                type = NavType.StringType
            }
        )
    ) {
        EditPlaylistRoute(
            onBack = onBack
        )
    }
}