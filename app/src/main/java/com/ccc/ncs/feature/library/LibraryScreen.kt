package com.ccc.ncs.feature.library

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.feature.library.offline.OfflineMusicUiState
import com.ccc.ncs.feature.library.offline.OfflineMusicViewModel
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.ui.component.PlayListColumn
import java.util.UUID

@Composable
fun LibraryRoute(
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = hiltViewModel(),
    offlineMusicViewModel: OfflineMusicViewModel = hiltViewModel(),
    onClickAddPlaylist: () -> Unit,
    onClickPlaylist: (PlayList) -> Unit,
    onClickOfflineMusics: () -> Unit
) {
    val playListUiState by viewModel.playListUiState.collectAsStateWithLifecycle()
    val offlineMusicUiState by offlineMusicViewModel.uiState.collectAsStateWithLifecycle()

    LibraryScreen(
        modifier = modifier,
        playListUiState = playListUiState,
        onClickAddPlaylist = onClickAddPlaylist,
        onClickPlaylist = onClickPlaylist,
        offlineMusicCount = (offlineMusicUiState as? OfflineMusicUiState.Success)?.musics?.size ?: 0,
        onClickOfflineMusics = onClickOfflineMusics
    )
}

@Composable
internal fun LibraryScreen(
    modifier: Modifier = Modifier,
    playListUiState: PlayListUiState,
    onClickAddPlaylist: () -> Unit,
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            IconButton(onClick = onClickAddPlaylist) {
                Icon(
                    imageVector = NcsIcons.AddCircle,
                    contentDescription = stringResource(R.string.cd_add_playlist),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(60.dp)
                )
            }

            Text(
                text = stringResource(R.string.add_new_playlist),
                style = NcsTypography.Label.button.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        if (offlineMusicCount > 0) {
            OfflinePlayListColumnItem(
                count = offlineMusicCount,
                onClick = onClickOfflineMusics
            )
        }

        when (playListUiState) {
            is PlayListUiState.Loading -> {}
            is PlayListUiState.Success -> {
                PlayListColumn(
                    playListItems = playListUiState.playLists,
                    currentPlaylist = playListUiState.currentPlaylist,
                    onClick = onClickPlaylist
                )
            }
        }
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
                onClickAddPlaylist = {},
                onClickPlaylist = {},
                offlineMusicCount = 10,
                onClickOfflineMusics = {}
            )
        }
    }
}