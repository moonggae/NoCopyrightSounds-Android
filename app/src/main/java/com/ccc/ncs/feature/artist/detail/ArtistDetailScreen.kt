package com.ccc.ncs.feature.artist.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.ArtistDetail
import com.ccc.ncs.ui.component.mockMusics


@Composable
fun ArtistDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: ArtistDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is ArtistDetailUiState.Success -> {
            ArtistDetailScreen(
                modifier = modifier,
                artistDetail = state.artistDetail
            )
        }
        else -> {
            // TODO
        }
    }
}

@Composable
internal fun ArtistDetailScreen(
    modifier: Modifier = Modifier,
    artistDetail: ArtistDetail
) {

}


@Preview
@Composable
private fun ArtistDetailScreenPreview(

) {
    NcsTheme(darkTheme = true) {
        val artist = Artist(
            detailUrl = "",
            tags = "Drumstep",
            name = "Keepsake",
            photoUrl = ""
        )
        ArtistDetailScreen(
            artistDetail = ArtistDetail(
                artist = artist,
                similarArtists = listOf(artist),
                musics = mockMusics
            )
        )
    }
}