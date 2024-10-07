package com.ccc.ncs.feature.menu.cache

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


private const val CACHE_SETTING_ROUTE = "cacheSetting"

fun NavController.navigateToCacheSetting() = navigate(CACHE_SETTING_ROUTE)

fun NavGraphBuilder.cacheSettingScreen(
    onBack: () -> Unit,
    onMoveToSettingSizeScreen: () -> Unit
) {
    composable(route = CACHE_SETTING_ROUTE) {
        CacheSettingRoute(
            onBack = onBack,
            onMoveToSettingSizeScreen = onMoveToSettingSizeScreen
        )
    }
}