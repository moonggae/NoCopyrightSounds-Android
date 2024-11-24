package com.ccc.ncs.feature.library

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.ListItemCard
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.feature.library.offline.OfflineMusicUiState
import com.ccc.ncs.feature.library.offline.OfflineMusicViewModel
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.ui.component.LoadingScreen
import com.ccc.ncs.ui.component.PlayListColumn
import java.util.UUID

@Composable
fun LibraryRoute(
    viewModel: LibraryViewModel = hiltViewModel(),
    offlineMusicViewModel: OfflineMusicViewModel = hiltViewModel(),
    onClickPlaylist: (PlayList) -> Unit,
    onClickOfflineMusics: () -> Unit
) {
    val playListUiState by viewModel.playListUiState.collectAsStateWithLifecycle()
    val offlineMusicUiState by offlineMusicViewModel.uiState.collectAsStateWithLifecycle()

    val uiStateLoaded = remember(playListUiState, offlineMusicUiState) {
        playListUiState is PlayListUiState.Success && offlineMusicUiState is OfflineMusicUiState.Success
    }

    val emptyLibrary = remember(uiStateLoaded, playListUiState, offlineMusicUiState) {
        uiStateLoaded
                && (playListUiState as? PlayListUiState.Success)?.playLists?.isEmpty() ?: false
                && (offlineMusicUiState as? OfflineMusicUiState.Success)?.musics?.isEmpty() ?: false
    }

    when {
        emptyLibrary -> LibraryEmptyScreen()
        uiStateLoaded -> LibraryScreen(
            playListUiState = playListUiState as PlayListUiState.Success,
            onClickPlaylist = onClickPlaylist,
            offlineMusicCount = (offlineMusicUiState as OfflineMusicUiState.Success).musics.size,
            onClickOfflineMusics = onClickOfflineMusics
        )
        else -> LoadingScreen()
    }
}

@Composable
internal fun LibraryScreen(
    playListUiState: PlayListUiState.Success,
    onClickPlaylist: (PlayList) -> Unit,
    offlineMusicCount: Int,
    onClickOfflineMusics: () -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        if (offlineMusicCount > 0) {
            OfflinePlayListColumnItem(
                count = offlineMusicCount,
                onClick = onClickOfflineMusics
            )
        }

        PlayListColumn(
            playListItems = playListUiState.playLists,
            currentPlaylist = playListUiState.currentPlaylist,
            onClick = onClickPlaylist
        )
    }
}

@Composable
fun LibraryEmptyScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Create your first playlist",
            style = NcsTypography.Menu.description,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OfflinePlayListColumnItem(
    modifier: Modifier = Modifier,
    count: Int,
    onClick: () -> Unit
) {
    Column(modifier) {
        ListItemCard(
            prefix = {
                Image(
                    painter = painterResource(R.drawable.ncs_cover),
                    contentDescription = stringResource(R.string.cd_music_cover),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(4.dp))
                )
            },
            label = stringResource(R.string.playlist_name_offline_musics),
            description = stringResource(R.string.songs_count, count),
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onClick
                )
                .padding(
                    top = 8.dp,
                    bottom = 12.dp
                )
                .height(58.dp)
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            thickness = 1.dp,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
    }
}

@Preview
@Composable
fun LibraryScreenPreview() {
    NcsTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            LibraryScreen(
                playListUiState = PlayListUiState.Success(
                    playLists = listOf(
                        PlayList(
                            id = UUID.randomUUID(),
                            name = "PlayList 1",
                            musics = listOf(),
                            isUserCreated = true
                        ),
                        PlayList(
                            id = UUID.randomUUID(),
                            name = "PlayList 2",
                            musics = listOf(),
                            isUserCreated = true
                        ),
                        PlayList(
                            id = UUID.randomUUID(),
                            name = "PlayList 3",
                            musics = listOf(),
                            isUserCreated = true
                        )
                    )
                ),
                onClickPlaylist = {},
                offlineMusicCount = 10,
                onClickOfflineMusics = {}
            )
        }
    }
}