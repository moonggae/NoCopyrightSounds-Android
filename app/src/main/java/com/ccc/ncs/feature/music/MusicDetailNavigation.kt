package com.ccc.ncs.feature.music

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ccc.ncs.feature.home.SELECTED_GENRE_ID
import com.ccc.ncs.feature.home.SELECTED_MOOD_ID
import com.ccc.ncs.feature.search.SEARCHED_QUERY
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import java.util.UUID


private const val MUSIC_DETAIL_BASE_ROUTE = "musicdetail"
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
    previousBackStackEntry?.savedStateHandle?.apply {
        set(SELECTED_MOOD_ID, null)
        set(SEARCHED_QUERY, null)
        set(SELECTED_GENRE_ID, genreId)
    }
    navigateUp()
}

fun NavController.backWithMood(moodId: Int) {
    previousBackStackEntry?.savedStateHandle?.apply {
        set(SELECTED_GENRE_ID, null)
        set(SEARCHED_QUERY, null)
        set(SELECTED_MOOD_ID, moodId)
    }
    navigateUp()
}

fun NavGraphBuilder.musicDetailScreen(
    onBack: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onClickMood: (Mood) -> Unit,
    onClickGenre: (Genre) -> Unit,
    onPlayMusics: (List<Music>) -> Unit,
    onAddToQueue: (List<Music>) -> Unit,
    navigateToArtistDetail: (artistDetailPath: String) -> Unit
) {
    composable(route = MUSIC_DETAIL_ROUTE) {
        MusicDetailRoute(
            onBack = onBack,
            onShowSnackbar = onShowSnackbar,
            onClickMood = onClickMood,
            onClickGenre = onClickGenre,
            onPlayMusic = { onPlayMusics(listOf(it)) },
            onAddToQueue = { onAddToQueue(listOf(it)) },
            onMoveToArtistDetail = { navigateToArtistDetail(it.detailUrl) }
        )
    }
}