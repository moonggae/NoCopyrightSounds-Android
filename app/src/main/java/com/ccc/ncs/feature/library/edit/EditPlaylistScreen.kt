package com.ccc.ncs.feature.library.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.TransparentTextField
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.ui.component.LoadingScreen


@Composable
fun EditPlaylistRoute(
    modifier: Modifier = Modifier,
    viewModel: EditPlaylistViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is EditPlayListUiState.Loading -> LoadingScreen()
        is EditPlayListUiState.Success -> {
            EditPlaylistScreen(
                modifier = modifier.imePadding(),
                uiState = uiState as EditPlayListUiState.Success,
                onSave = { name ->
                    viewModel.savePlaylist(name)
                    onBack()
                },
                onCancel = onBack
            )
        }
    }
}

const val PLAYLIST_NAME_LENGTH_LIMIT = 100

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun EditPlaylistScreen(
    modifier: Modifier = Modifier,
    uiState: EditPlayListUiState.Success,
    onSave: (String) -> Unit,
    onCancel: () -> Unit
) {
    val textState = rememberTextFieldState(initialText = uiState.playList?.name ?: "")

    val tooLongMessage = stringResource(R.string.playlist_edit_error_too_long)
    val emptyMessage = stringResource(R.string.playlist_edit_error_empty)

    val errorMessage: String = remember(textState.text) {
        when {
            textState.text.length > PLAYLIST_NAME_LENGTH_LIMIT -> tooLongMessage
            textState.undoState.canUndo && textState.text.isEmpty() -> emptyMessage
            else -> ""
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp)
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        Text(
            text = if (uiState.playList == null) stringResource(R.string.playlist_edit_title_new) else stringResource(R.string.playlist_edit_title_update),
            style = NcsTypography.Search.content,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        TransparentTextField(
            state = textState,
            placeholder = stringResource(R.string.playlist_edit_name_placeholder),
            modifier = Modifier
                .padding(bottom = 4.dp)
                .fillMaxWidth()
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)


        Text(
            text = errorMessage,
            style = NcsTypography.Menu.description.copy(
                color = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
        ) {
            OutlinedButton(
                onClick = onCancel
            ) {
                Text(text = stringResource(R.string.Cancel))
            }

            Button(
                onClick = { onSave(textState.text.toString()) },
                enabled = errorMessage.isEmpty()
            ) {
                Text(text = stringResource(R.string.save))
            }
        }
    }
}