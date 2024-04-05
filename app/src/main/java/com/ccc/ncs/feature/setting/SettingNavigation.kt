package com.ccc.ncs.feature.setting

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val SETTING_ROUTE = "setting"

fun NavController.navigateToSetting(navOptions: NavOptions) = navigate(SETTING_ROUTE, navOptions)

fun NavGraphBuilder.settingScreen() {
    composable(route = SETTING_ROUTE) {
        SettingRoute()
    }
}