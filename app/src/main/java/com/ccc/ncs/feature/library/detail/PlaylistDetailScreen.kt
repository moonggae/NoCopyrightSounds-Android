package com.ccc.ncs.feature.library.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.ListItemCard
import com.ccc.ncs.designsystem.component.ListItemCardDefaults
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.ui.component.mockMusics
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import java.util.UUID


@Composable
fun PlaylistDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: PlaylistDetailViewModel = hiltViewModel()
) {
    val playListUiState by viewModel.playListUiState.collectAsStateWithLifecycle()

    when (val uiState = playListUiState) {
        is PlaylistDetailUiState.Loading -> {}
        is PlaylistDetailUiState.Fail -> {}
        is PlaylistDetailUiState.Success -> {
            PlaylistDetailScreen(
                modifier = modifier,
                playlist = uiState.playlist,
                onMusicOrderChanged = { viewModel.updateMusicList(uiState.playlist.id, it) }
            )
        }
    }
}

@Composable
internal fun PlaylistDetailScreen(
    modifier: Modifier = Modifier,
    playlist: PlayList,
    onMusicOrderChanged: (List<Music>) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        PlaylistDetailAppBar(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 20.dp
                )
                .fillMaxWidth()
        )

        PlaylistDetailContent(
            modifier = modifier,
            name = playlist.name,
            coverUrl = playlist.musics.firstOrNull()?.coverUrl
        )

        PlaylistDetailMusicList(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 32.dp
                )
                .fillMaxWidth(),
            musics = playlist.musics,
            onMusicOrderChanged = onMusicOrderChanged
        )
    }
}

fun <T> List<T>.swap(index1: Int, index2: Int): List<T> {
    return this.toMutableList().apply {
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistDetailMusicList(
    modifier: Modifier = Modifier,
    musics: List<Music>,
    onMusicOrderChanged: (List<Music>) -> Unit
) {
    var currentMusics by remember { mutableStateOf(musics) }

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        currentMusics = currentMusics.swap(from.index, to.index)
        onMusicOrderChanged(currentMusics)
    }

    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        items(count = currentMusics.size, key = { currentMusics[it].id }) {
            val item = currentMusics[it]
            ReorderableItem(state = reorderableLazyListState, key = item.id) { isDragging ->
                ListItemCard(
                    thumbnail = item.coverThumbnailUrl,
                    label = item.title,
                    description = item.artist,
                    thumbnailPlaceholder = painterResource(R.drawable.ncs_cover),
                    style = ListItemCardDefaults.listItemCardStyle.small(),
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
    coverUrl: String?
) {
    val coverImage = rememberAsyncImagePainter(
        model = coverUrl,
        placeholder = painterResource(id = R.drawable.ncs_cover)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = coverImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(horizontal = 70.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
        )

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
fun PlaylistDetailAppBar(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = NcsIcons.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Playlist",
            style = NcsTypography.Label.appbarTitle.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Icon(
            imageVector = NcsIcons.MoreVertical,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
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
                    musics = mockMusics
                ),
                onMusicOrderChanged = {}
            )
        }
    }
}