package com.ccc.ncs.feature.artist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ccc.ncs.model.Artist
import com.ccc.ncs.ui.component.TestArtistCard

@Composable
fun ArtistRoute(
    modifier: Modifier = Modifier,
    viewModel: ArtistViewModel = hiltViewModel()
) {
    ArtistScreen(
        testArtists = viewModel.artists.collectAsLazyPagingItems()
    )
}

@Composable
internal fun ArtistScreen(
    modifier: Modifier = Modifier,
    testArtists: LazyPagingItems<Artist>,
) {
    LazyColumn {
        items(count = testArtists.itemCount) { index ->
            testArtists[index]?.let { artist ->
                TestArtistCard(item = artist)
            }
        }
    }
}