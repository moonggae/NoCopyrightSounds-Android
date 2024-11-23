package com.ccc.ncs.feature.menu.cache

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.ccc.ncs.navigation.noneTransitionComposable


private const val CACHE_SIZE_SETTING_ROUTE = "cacheSizeSetting"

fun NavController.navigateToCacheSizeSetting() = navigate(CACHE_SIZE_SETTING_ROUTE)

fun NavGraphBuilder.cacheSizeSettingScreen(
    onBack: () -> Unit
) {
    noneTransitionComposable(route = CACHE_SIZE_SETTING_ROUTE) {
        CacheSizeSettingRoute(
            onBack = onBack
        )
    }
}