package com.ccc.ncs.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.feature.artist.ARTIST_ROUTE
import com.ccc.ncs.feature.home.HOME_ROUTE
import com.ccc.ncs.feature.library.LIBRARY_ROUTE
import com.ccc.ncs.feature.menu.MENU_ROUTE

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val route: String
) {
    HOME(
        selectedIcon = NcsIcons.Home,
        unselectedIcon = NcsIcons.HomeBorder,
        iconTextId = R.string.feature_home_title,
        route = HOME_ROUTE
    ),

    ARTIST(
        selectedIcon = NcsIcons.People,
        unselectedIcon = NcsIcons.PeopleBorder,
        iconTextId = R.string.feature_artist_title,
        route = ARTIST_ROUTE
    ),

    LIBRARY(
        selectedIcon = NcsIcons.LibraryMusic,
        unselectedIcon = NcsIcons.LibraryMusicBorder,
        iconTextId = R.string.feature_library_title,
        route = LIBRARY_ROUTE
    ),

    MENU(
        selectedIcon = NcsIcons.Menu,
        unselectedIcon = NcsIcons.MenuBorder,
        iconTextId = R.string.feature_menu_title,
        route = MENU_ROUTE
    )
}