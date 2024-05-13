package com.ccc.ncs.feature.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.ui.component.PlayListColumn
import java.util.UUID

@Composable
fun LibraryRoute(
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val playListUiState by viewModel.playListUiState.collectAsStateWithLifecycle()

    LibraryScreen(
        modifier = modifier,
        playListUiState = playListUiState,
        onAddPlayList = viewModel::addPlayList
    )

}

@Composable
internal fun LibraryScreen(
    modifier: Modifier = Modifier,
    playListUiState: PlayListUiState,
    onAddPlayList: (String) -> Unit
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
            IconButton(
                onClick = {
                    // TODO
                    onAddPlayList("New Playlist ${System.currentTimeMillis()}")
                }
            ) {
                Icon(
                    imageVector = NcsIcons.AddCircle,
                    contentDescription = null,
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


        when (playListUiState) {
            is PlayListUiState.Loading -> {}
            is PlayListUiState.Success -> {
                PlayListColumn(
                    playListItems = playListUiState.playLists,
                    onClick = {
                        // TODO
                    }
                )
            }
        }
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
                            musics = listOf()
                        ),
                        PlayList(
                            id = UUID.randomUUID(),
                            name = "PlayList 2",
                            musics = listOf()
                        ),
                        PlayList(
                            id = UUID.randomUUID(),
                            name = "PlayList 3",
                            musics = listOf()
                        )
                    )
                ),
                onAddPlayList = {}
            )
        }
    }
}