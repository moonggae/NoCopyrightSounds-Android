package com.ccc.ncs.feature.home.addmusictoplaylistdialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.NcsDialog
import com.ccc.ncs.designsystem.component.NcsDialogTextButton
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
    onFinish: () -> Unit,
    musics: List<Music>,
    viewModel: AddMusicsToPlaylistViewModel = hiltViewModel()
) {
    val uiState: AddMusicsToPlaylistUiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is AddMusicsToPlaylistUiState.Loading -> {}
        is AddMusicsToPlaylistUiState.Success -> {
            AddMusicsToPlaylistDialogContent(
                modifier = modifier,
                playListItems = state.playlist,
                currentPlaylist = state.currentPlaylist,
                show = show,
                onDismissRequest = onDismissRequest,
                onClickPlayList = {
                    viewModel.addMusicToPlaylist(playList = it, musics = musics)
                    onFinish()
                }
            )
        }
    }
}

@Composable
fun AddMusicsToPlaylistDialogContent(
    modifier: Modifier = Modifier,
    playListItems: List<PlayList>,
    currentPlaylist: PlayList?,
    show: Boolean,
    onDismissRequest: () -> Unit,
    onClickPlayList: (PlayList) -> Unit
) {
    NcsDialog(
        show = show,
        onDismissRequest = onDismissRequest,
        title = stringResource(R.string.select_playlist_dialog_title),
        content = {
            if (playListItems.isEmpty()) {
                Text(
                    text = stringResource(R.string.select_playlist_dialog_no_playlist_available),
                    style = NcsTypography.Dialog.message.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            } else {
                PlayListColumn(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .heightIn(0.dp, 300.dp),
                    playListItems = playListItems,
                    onClick = onClickPlayList,
                    currentPlaylist = currentPlaylist
                )
            }
        },
        bottomContent = {
            Row(modifier = Modifier.height(40.dp)) {
                NcsDialogTextButton(
                    label = stringResource(R.string.Cancel),
                    onClick = onDismissRequest
                )
            }
        }
    )
}

@Preview
@Composable
fun AddMusicsToPlaylistDialogPreview(modifier: Modifier = Modifier) {
    NcsTheme(darkTheme = true) {
        AddMusicsToPlaylistDialogContent(
            modifier = modifier,
            playListItems = listOf(
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
            show = true,
            onDismissRequest = {},
            onClickPlayList = {},
            currentPlaylist = null
        )
    }
}

@Preview
@Composable
fun AddMusicsToPlaylistDialogEmptyPreview(modifier: Modifier = Modifier) {
    NcsTheme(darkTheme = true) {
        AddMusicsToPlaylistDialogContent(
            modifier = modifier,
            playListItems = listOf(),
            show = true,
            onDismissRequest = {},
            onClickPlayList = {},
            currentPlaylist = null
        )
    }
}