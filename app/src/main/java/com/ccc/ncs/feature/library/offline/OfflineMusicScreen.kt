package com.ccc.ncs.feature.library.offline

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.CommonAppBar
import com.ccc.ncs.designsystem.component.CommonModalBottomSheet
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.feature.home.SelectMusicAppBar
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.MusicStatus
import com.ccc.ncs.model.util.toggle
import com.ccc.ncs.ui.component.BottomSheetMenuItem
import com.ccc.ncs.ui.component.LoadingScreen
import com.ccc.ncs.ui.component.MusicCardListItem
import com.ccc.ncs.ui.component.mockMusics
import java.util.UUID


@Composable
fun OfflineMusicRoute(
    modifier: Modifier = Modifier,
    viewModel: OfflineMusicViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is OfflineMusicUiState.Loading -> LoadingScreen()
        is OfflineMusicUiState.Success -> {
            OfflineMusicScreen(
                modifier = modifier,
                musics = (uiState as OfflineMusicUiState.Success).musics,
                onBack = onBack,
                onDeleteMusics = viewModel::deleteMusics
            )
        }
    }
}

@Composable
internal fun OfflineMusicScreen(
    modifier: Modifier = Modifier,
    musics: List<Music>,
    onBack: () -> Unit,
    onDeleteMusics: (List<UUID>) -> Unit
) {
    var showDownloadedMusics by remember { mutableStateOf(true) }
    var showCachedMusics by remember { mutableStateOf(true) }
    var isSelectMode by remember { mutableStateOf(false) }
    var showSelectMusicMenu by remember { mutableStateOf(false) }

    val selectedMusicIds = remember { mutableStateListOf<UUID>() }

    val showingMusics = remember(showDownloadedMusics, showCachedMusics, musics) {
        musics.filter {
            (showDownloadedMusics || it.status !is MusicStatus.Downloaded) &&
                    (showCachedMusics || it.status !is MusicStatus.FullyCached)
        }
    }

    LaunchedEffect(isSelectMode) {
        if (!isSelectMode) {
            selectedMusicIds.clear()
        }
    }

    Column(modifier = modifier) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        TopAppBar(
            isSelectMode = isSelectMode,
            onBack = onBack,
            onSelectModeChanged = { isSelectMode = it },
            selectedMusicCount = selectedMusicIds.size,
            onClickMenu = { showSelectMusicMenu = true }
        )

        FilterOptions(
            showDownloadedMusics = showDownloadedMusics,
            showCachedMusics = showCachedMusics,
            onShowDownloadedChange = { showDownloadedMusics = it },
            onShowCachedChange = { showCachedMusics = it }
        )

        LazyColumn {
            items(
                items = showingMusics,
                key = { it.id }
            ) { music ->
                MusicCardListItem(
                    music = music,
                    selectedMusicIds = selectedMusicIds.toList(),
                    updateSelectMusic = selectedMusicIds::toggle,
                    updateSelectMode = { isSelectMode = it },
                    onClickMore = null,
                    onClick = {
                        if (isSelectMode) {
                            selectedMusicIds.toggle(it)
                        }
                    },
                    isSelectMode = isSelectMode
                )
            }
        }

        if (showSelectMusicMenu) {
            SelectMusicMenu(
                onDismissRequest = { showSelectMusicMenu = false },
                onDeleteMusics = {
                    onDeleteMusics(selectedMusicIds.toList())
                    showSelectMusicMenu = false
                    isSelectMode = false
                }
            )
        }
    }
}

@Composable
fun TopAppBar(
    isSelectMode: Boolean,
    onBack: () -> Unit,
    onSelectModeChanged: (Boolean) -> Unit,
    selectedMusicCount: Int,
    onClickMenu: () -> Unit
) {
    if (isSelectMode) {
        SelectMusicAppBar(
            onClickMenu = onClickMenu,
            onClickClose = { onSelectModeChanged(false) },
            selectedMusicCount = selectedMusicCount,
            label = "            ${stringResource(R.string.Delete)}",
            modifier = Modifier.padding(
                vertical = 6.dp,
                horizontal = 4.dp
            )
        )
    } else {
        CommonAppBar(
            onBack = onBack,
            padding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            title = stringResource(R.string.playlist_name_offline_musics),
            suffixIcon = NcsIcons.Delete,
            onClickSuffix = { onSelectModeChanged(true) }
        )
    }
}

@Composable
fun FilterOptions(
    showDownloadedMusics: Boolean,
    showCachedMusics: Boolean,
    onShowDownloadedChange: (Boolean) -> Unit,
    onShowCachedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        LabelCheckBox(
            label = "Download",
            checked = showDownloadedMusics,
            onCheckedChange = onShowDownloadedChange
        )
        Spacer(modifier = Modifier.width(16.dp))
        LabelCheckBox(
            label = "Cache",
            checked = showCachedMusics,
            onCheckedChange = onShowCachedChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMusicMenu(
    onDismissRequest: () -> Unit,
    onDeleteMusics: () -> Unit
) {
    CommonModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            BottomSheetMenuItem(
                icon = NcsIcons.Delete,
                label = stringResource(R.string.Delete),
                onClick = {
                    onDeleteMusics()
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
fun LabelCheckBox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = label,
            style = NcsTypography.Label.textButton.copy(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.padding(end = 6.dp)
        )
    }
}


@Preview
@Composable
fun OfflineMusicScreenPreview() {
    NcsTheme(darkTheme = true) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            OfflineMusicScreen(
                musics = mockMusics,
                onBack = {},
                onDeleteMusics = {}
            )
        }
    }
}