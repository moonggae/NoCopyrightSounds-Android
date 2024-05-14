package com.ccc.ncs.feature.library.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccc.ncs.designsystem.component.TransparentTextField
import com.ccc.ncs.designsystem.theme.NcsTypography


@Composable
fun EditPlaylistRoute(
    modifier: Modifier = Modifier,
    viewModel: EditPlaylistViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EditPlaylistScreen(
        modifier = modifier,
        uiState = uiState,
        onConfirm = { name ->
            uiState.let { state ->
                if (state is EditPlayListUiState.Success) {
                    if (state.playList == null) {
                        viewModel.addPlaylist(name)
                    } else {
                        viewModel.updatePlaylistName(state.playList.id, name)
                    }
                    onBack()
                }
            }
        },
        onCancel = onBack
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun EditPlaylistScreen(
    modifier: Modifier = Modifier,
    uiState: EditPlayListUiState,
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit
) {
    when (uiState) {
        is EditPlayListUiState.Loading -> {}
        is EditPlayListUiState.Success -> {
            val textState = rememberTextFieldState(initialText = uiState.playList?.name ?: "")

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp)
            ) {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))


                Text(
                    text = if (uiState.playList == null) "New Playlist" else "Edit Playlist",
                    style = NcsTypography.Search.content,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                TransparentTextField(
                    state = textState,
                    placeholder = "My Playlist",
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .fillMaxWidth()
                )
                HorizontalDivider(
                    modifier = Modifier.padding(bottom = 36.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = onCancel) {
                        Text(text = "Cancel")
                    }

                    Button(onClick = { onConfirm(textState.text.toString()) }) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}