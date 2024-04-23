package com.ccc.ncs.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ccc.ncs.feature.play.PlayerViewModel
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.ui.component.TestMusicCard
import java.util.UUID

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen(
        modifier = modifier,
        testMusics = viewModel.musics.collectAsLazyPagingItems()
    )
}

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    testMusics: LazyPagingItems<Music>,
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    LazyColumn {
        items(count = testMusics.itemCount) { index ->
            testMusics[index]?.let { music ->
                TestMusicCard(
                    item = music,
                    modifier = Modifier.clickable {
                        playerViewModel.setPlayList(PlayList( // todo : play list logic
                            id = UUID.randomUUID(),
                            name = "PlayList of ${music.title}",
                            musics = listOfNotNull(music, testMusics[index + 1])
                        ))
                    }
                )
            }
        }
    }
}