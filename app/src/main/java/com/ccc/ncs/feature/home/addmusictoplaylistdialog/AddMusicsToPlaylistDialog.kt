package com.ccc.ncs.feature.home.addmusictoplaylistdialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.ui.component.PlayListColumn
import java.util.UUID

@Composable
fun AddMusicsToPlaylistDialog(
    modifier: Modifier = Modifier,
    show: Boolean,
    onDismissRequest: () -> Unit,
    musics: List<Music>,
    viewModel: AddMusicsToPlaylistViewModel = hiltViewModel()
) {
    val uiState: AddMusicsToPlaylistUiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (show) {
        Dialog(onDismissRequest = onDismissRequest) {
            when (val state = uiState) {
                is AddMusicsToPlaylistUiState.Loading -> {}
                is AddMusicsToPlaylistUiState.Success -> {
                    AddMusicsToPlaylistDialogContent(
                        playlist = state.playlist,
                        onClickPlaylist = {
                            viewModel.addMusicToPlaylist(playList = it, musics = musics)
                            onDismissRequest()
                        },
                        onCancel = onDismissRequest
                    )
                }
            }
        }
    }
}

@Composable
fun AddMusicsToPlaylistDialogContent(
    modifier: Modifier = Modifier,
    playlist: List<PlayList>,
    onClickPlaylist: (PlayList) -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(
                shape = RoundedCornerShape(8.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
    ) {
        PlayListColumn(
            playListItems = playlist,
            onClick = onClickPlaylist,
            modifier = Modifier.padding(12.dp)
        )

        Row(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth()
                .clickable(onClick = onCancel),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.Cancel),
                style = NcsTypography.Label.textButton,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Preview
@Composable
fun AddMusicsToPlaylistDialogContentPreview(modifier: Modifier = Modifier) {
    NcsTheme(darkTheme = true) {
        AddMusicsToPlaylistDialogContent(
            playlist = listOf(
                PlayList(
                    id = UUID.randomUUID(),
                    name = "PlayList 1",
                    musics = listOf()
                ),
                PlayList(
                    id = UUID.randomUUID(),
                    name = "PlayList 2",
                    musics = listOf()
                )
            ),
            onClickPlaylist = {},
            onCancel = {}
        )
    }
}