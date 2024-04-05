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
        selectedIcon = NcsIcons.MusicNote,
        unselectedIcon = NcsIcons.MusicNoteBorder,
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

    SETTING(
        selectedIcon = NcsIcons.Settings,
        unselectedIcon = NcsIcons.SettingsBorder,
        iconTextId = R.string.feature_settings_title,
    )
}