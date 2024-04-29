package com.ccc.ncs.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.icon.NcsIcons

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
) {
    HOME(
        selectedIcon = NcsIcons.Home,
        unselectedIcon = NcsIcons.HomeBorder,
        iconTextId = R.string.feature_home_title,
    ),

    ARTIST(
        selectedIcon = NcsIcons.People,
        unselectedIcon = NcsIcons.PeopleBorder,
        iconTextId = R.string.feature_artist_title,
    ),

    LIBRARY(
        selectedIcon = NcsIcons.LibraryMusic,
        unselectedIcon = NcsIcons.LibraryMusicBorder,
        iconTextId = R.string.feature_library_title,
    ),

    MENU(
        selectedIcon = NcsIcons.Menu,
        unselectedIcon = NcsIcons.MenuBorder,
        iconTextId = R.string.feature_menu_title,
    )
}