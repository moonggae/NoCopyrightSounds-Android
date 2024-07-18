package com.ccc.ncs.feature.library.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.AlertDialog
import com.ccc.ncs.designsystem.component.CommonAppBar
import com.ccc.ncs.designsystem.component.CommonModalBottomSheet
import com.ccc.ncs.designsystem.component.ListItemCardDefaults
import com.ccc.ncs.designsystem.component.ListItemCardStyle
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.ui.component.BottomSheetMenuItem
import com.ccc.ncs.ui.component.MusicCard
import com.ccc.ncs.ui.component.mockMusics
import com.ccc.ncs.util.swap
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import java.util.UUID


@Composable
fun PlaylistDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: PlaylistDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onClickModifyName: (UUID) -> Unit,
    onPlay: (PlayList) -> Unit
) {
    val playListUiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (playListUiState) {
        is PlaylistDetailUiState.Loading -> {}
        is PlaylistDetailUiState.Fail -> {}
        is PlaylistDetailUiState.Success -> {
            val uiState = playListUiState as PlaylistDetailUiState.Success
            PlaylistDetailScreen(
                modifier = modifier,
                playlist = uiState.playlist,
                playingMusic = uiState.playingMusic,
                onMusicOrderChanged = viewModel::updateMusicOrder,
                onBack = onBack,
                onClickModifyName = { onClickModifyName(uiState.playlist.id) },
                onDeletePlaylist = {
                    viewModel.deletePlaylist(uiState.playlist.id)
                    onBack()
                },
                onPlay = { onPlay(uiState.playlist) },
                isPlaying = uiState.isPlaying
            )
        }
    }
}

@Composable
internal fun PlaylistDetailScreen(
    modifier: Modifier = Modifier,
    playlist: PlayList,
    onMusicOrderChanged: (prevIndex: Int, currentIndex: Int) -> Unit,
    onBack: () -> Unit,
    onClickModifyName: () -> Unit,
    onDeletePlaylist: () -> Unit,
    playingMusic: Music?,
    onPlay: () -> Unit,
    isPlaying: Boolean
) {
    var showMenuBottomSheet by remember { mutableStateOf(false) }
    var showDeleteAlertDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        CommonAppBar(
            title = stringResource(R.string.feature_playlist_title),
            onBack = onBack,
            onClickMenu = { showMenuBottomSheet = true }
        )

        PlaylistDetailContent(
            modifier = modifier,
            name = if (playlist.isUserCreated) playlist.name else stringResource(R.string.auto_generated_playlist_name),
            coverUrl = playlist.musics.firstOrNull()?.coverUrl,
            isPlaying = isPlaying,
            onPlay = onPlay
        )

        PlaylistDetailMusicList(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 32.dp
                )
                .fillMaxWidth(),
            playlistId = playlist.id,
            musics = playlist.musics,
            playingMusicId = playingMusic?.id,
            onMusicOrderChanged = onMusicOrderChanged
        )
    }


    PlaylistDetailMenuBottomSheet(
        show = showMenuBottomSheet,
        isUserCreated = playlist.isUserCreated,
        onDismissRequest = { showMenuBottomSheet = false },
        onClickModifyName = onClickModifyName,
        onClickDelete = { showDeleteAlertDialog = true }
    )

    AlertDialog(
        show = showDeleteAlertDialog,
        onDismissRequest = { showDeleteAlertDialog = false },
        title = stringResource(R.string.delete_playlist_alert_title),
        message = stringResource(R.string.delete_playlist_alert_message),
        confirmLabel = stringResource(R.string.Delete),
        onConfirm = {
            showMenuBottomSheet = false
            showDeleteAlertDialog = false
            onDeletePlaylist()
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistDetailMusicList(
    modifier: Modifier = Modifier,
    playlistId: UUID? = null,
    musics: List<Music>,
    playingMusicId: UUID? = null,
    cardStyle: ListItemCardStyle = ListItemCardDefaults.listItemCardStyle.small(),
    onMusicOrderChanged: (prevIndex: Int, currentIndex: Int) -> Unit
) {
    var currentMusics by remember(playlistId, musics.toSet()) { mutableStateOf(musics) }

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        currentMusics = currentMusics.swap(from.index, to.index)
        onMusicOrderChanged(from.index, to.index)
    }

    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        items(count = currentMusics.size, key = { currentMusics[it].id }) {
            val item = currentMusics[it]
            ReorderableItem(state = reorderableLazyListState, key = item.id) { isDragging ->
                MusicCard(
                    item = item,
                    isPlaying = playingMusicId == item.id,
                    suffix = {
                        Icon(
                            imageVector = NcsIcons.Menu,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .draggableHandle()
                        )
                    },
                    style = cardStyle,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
        }
    }
}

@Composable
fun PlaylistDetailContent(
    modifier: Modifier = Modifier,
    name: String,
    coverUrl: String?,
    onPlay: () -> Unit,
    isPlaying: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .padding(horizontal = 70.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
        ) {
            CoverImage(url = coverUrl)

            if (!isPlaying) {
                Icon(
                    imageVector = NcsIcons.PlayCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                        .align(Alignment.Center)
                        .padding(72.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onPlay)
                )
            }
        }

        Text(
            text = name,
            style = NcsTypography.PlaylistDetailTypography.name.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}

@Composable
fun CoverImage(
    modifier: Modifier = Modifier,
    url: String?
) {
    val painter = if (url != null) rememberAsyncImagePainter(
        model = url,
        placeholder = painterResource(id = R.drawable.ncs_cover)
    ) else painterResource(id = R.drawable.ncs_cover)

    Image(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailMenuBottomSheet(
    modifier: Modifier = Modifier,
    show: Boolean,
    onDismissRequest: () -> Unit,
    onClickModifyName: () -> Unit,
    onClickDelete: () -> Unit,
    isUserCreated: Boolean,
) {
    if (show) {
        CommonModalBottomSheet(onDismissRequest = onDismissRequest) {
            PlaylistDetailMenuBottomSheetContent(
                isUserCreated = isUserCreated,
                onClickModifyName = onClickModifyName,
                onClickDelete = onClickDelete
            )
        }
    }
}

@Composable
fun PlaylistDetailMenuBottomSheetContent(
    modifier: Modifier = Modifier,
    onClickModifyName: () -> Unit,
    onClickDelete: () -> Unit,
    isUserCreated: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        BottomSheetMenuItem(
            icon = NcsIcons.Edit,
            label = stringResource(if (isUserCreated) R.string.playlist_detail_menu_modify_name else R.string.playlist_detail_menu_save_recent_playlist),
            onClick = onClickModifyName
        )
        BottomSheetMenuItem(icon = NcsIcons.Delete, label = stringResource(R.string.playlist_detail_menu_delete_playlist), onClick = onClickDelete)
    }
}

@Preview
@Composable
fun PlaylistDetailContentPreview() {
    NcsTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            PlaylistDetailScreen(
                playlist = PlayList(
                    id = UUID.randomUUID(),
                    name = "My Playlist",
                    musics = mockMusics,
                    isUserCreated = true
                ),
                onMusicOrderChanged = { _, _ -> },
                onBack = {},
                onClickModifyName = {},
                onDeletePlaylist = {},
                playingMusic = null,
                onPlay = {},
                isPlaying = true
            )
        }
    }
}

@Preview
@Composable
fun PlaylistDetailMenuBottomSheetContentPreview(modifier: Modifier = Modifier) {
    NcsTheme(darkTheme = true) {
        PlaylistDetailMenuBottomSheetContent(
            onClickModifyName = {},
            onClickDelete = {},
            isUserCreated = true
        )
    }
}