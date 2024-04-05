package com.ccc.ncs.feature.artist

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ARTIST_ROUTE = "artist"

fun NavController.navigateToArtist(navOptions: NavOptions) = navigate(ARTIST_ROUTE, navOptions)

fun NavGraphBuilder.artistScreen() {
    composable(route = ARTIST_ROUTE) {
        ArtistRoute()
    }
}