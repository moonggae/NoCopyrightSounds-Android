package com.ccc.ncs.feature.library.offline

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


private const val OFFLINE_MUSIC_ROUTE = "offlineMusic"

fun NavController.navigateToOfflineMusic() = navigate(OFFLINE_MUSIC_ROUTE)

fun NavGraphBuilder.offlineMusicScreen(
    onBack: () -> Unit
) {
    composable(route = OFFLINE_MUSIC_ROUTE) {
        OfflineMusicRoute(
            onBack = onBack
        )
    }
}