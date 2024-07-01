package com.ccc.ncs.feature.music

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import java.util.UUID


private const val MUSIC_DETAIL_BASE_ROUTE = "musicdetail"
const val MUSIC_DETAIL_ID_ARG = "musicId"
private const val MUSIC_DETAIL_ROUTE = "$MUSIC_DETAIL_BASE_ROUTE?$MUSIC_DETAIL_ID_ARG={$MUSIC_DETAIL_ID_ARG}"

fun NavController.navigateToMusicDetail(musicId: UUID) =
    navigate("$MUSIC_DETAIL_BASE_ROUTE?$MUSIC_DETAIL_ID_ARG=$musicId")

fun NavGraphBuilder.musicDetailScreen(
    onBack: () -> Unit
) {
    composable(route = MUSIC_DETAIL_ROUTE) {
        MusicDetailRoute(
            onBack = onBack
        )
    }
}