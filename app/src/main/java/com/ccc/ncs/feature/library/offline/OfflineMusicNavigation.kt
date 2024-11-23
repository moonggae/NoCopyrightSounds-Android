package com.ccc.ncs.feature.library.offline

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.ccc.ncs.navigation.noneTransitionComposable


private const val OFFLINE_MUSIC_ROUTE = "offlineMusic"

fun NavController.navigateToOfflineMusic() = navigate(OFFLINE_MUSIC_ROUTE)

fun NavGraphBuilder.offlineMusicScreen(
    onBack: () -> Unit
) {
    noneTransitionComposable(route = OFFLINE_MUSIC_ROUTE) {
        OfflineMusicRoute(
            onBack = onBack
        )
    }
}