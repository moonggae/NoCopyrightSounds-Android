package com.ccc.ncs.feature.music

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.ccc.ncs.feature.home.HOME_ROUTE
import com.ccc.ncs.feature.home.SELECTED_GENRE_ID
import com.ccc.ncs.feature.home.SELECTED_MOOD_ID
import com.ccc.ncs.feature.search.SEARCHED_QUERY
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.navigation.noneTransitionComposable
import java.util.UUID


const val MUSIC_DETAIL_BASE_ROUTE = "musicdetail"
const val MUSIC_DETAIL_ID_ARG = "musicId"
private const val MUSIC_DETAIL_ROUTE = "$MUSIC_DETAIL_BASE_ROUTE?$MUSIC_DETAIL_ID_ARG={$MUSIC_DETAIL_ID_ARG}"

fun NavController.navigateToMusicDetail(musicId: UUID) {
    currentBackStackEntry?.savedStateHandle?.apply {
        set(SELECTED_MOOD_ID, null)
        set(SELECTED_GENRE_ID, null)
    }
    navigate("$MUSIC_DETAIL_BASE_ROUTE?$MUSIC_DETAIL_ID_ARG=$musicId") { launchSingleTop = true }
}

fun NavController.backWithGenre(genreId: Int) {
    getBackStackEntry(HOME_ROUTE).savedStateHandle.apply {
        set(SELECTED_MOOD_ID, null)
        set(SEARCHED_QUERY, null)
        set(SELECTED_GENRE_ID, genreId)
    }
    popBackStack(
        route = HOME_ROUTE,
        inclusive = false,
        saveState = false
    )
}

fun NavController.backWithMood(moodId: Int) {
    getBackStackEntry(HOME_ROUTE).savedStateHandle.apply {
        set(SELECTED_GENRE_ID, null)
        set(SEARCHED_QUERY, null)
        set(SELECTED_MOOD_ID, moodId)
    }
    popBackStack(
        route = HOME_ROUTE,
        inclusive = false,
        saveState = false
    )
}

fun NavGraphBuilder.musicDetailScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onClickMood: (Mood) -> Unit,
    onClickGenre: (Genre) -> Unit,
    onPlayMusics: (List<UUID>) -> Unit,
    onAddToQueue: (List<UUID>) -> Unit,
    navigateToArtistDetail: (artistDetailPath: String) -> Unit,
    onBack: () -> Unit,
    onClose: () -> Unit
) {
    noneTransitionComposable(route = MUSIC_DETAIL_ROUTE) {
        MusicDetailRoute(
            onBack = onBack,
            onShowSnackbar = onShowSnackbar,
            onClickMood = onClickMood,
            onClickGenre = onClickGenre,
            onPlayMusic = { onPlayMusics(listOf(it)) },
            onAddToQueue = { onAddToQueue(listOf(it)) },
            onMoveToArtistDetail = { navigateToArtistDetail(it.detailUrl) },
            onClose = onClose
        )
    }
}