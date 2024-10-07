package com.ccc.ncs.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.ListItemCard
import com.ccc.ncs.designsystem.component.ListItemCardDefaults
import com.ccc.ncs.designsystem.component.ListItemCardStyle
import com.ccc.ncs.designsystem.component.SwipeToDeleteCard
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.MusicStatus
import com.ccc.ncs.model.artistText
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.util.UUID

@Composable
fun MusicCard(
    modifier: Modifier = Modifier,
    item: Music,
    style: ListItemCardStyle = ListItemCardDefaults.listItemCardStyle.medium(),
    isSelectMode: Boolean = false,
    selected: Boolean = false,
    isPlaying: Boolean = false,
    onClick: (UUID) -> Unit = {},
    onLongClick: (UUID) -> Unit = {},
    onClickMore: (UUID) -> Unit = {}
) {
    MusicCard(
        item = item,
        selected = selected,
        isPlaying = isPlaying,
        modifier = modifier,
        style = style,
        onClick = onClick,
        onLongClick = onLongClick,
        suffix = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (item.status is MusicStatus.Downloaded || item.status is MusicStatus.FullyCached) {
                    Icon(
                        imageVector = NcsIcons.DownloadDone,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }

                if (!isSelectMode) {
                    Icon(
                        imageVector = NcsIcons.MoreVertical,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable { onClickMore(item.id) }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicCard(
    modifier: Modifier = Modifier,
    item: Music,
    selected: Boolean = false,
    isPlaying: Boolean = false,
    style: ListItemCardStyle = ListItemCardDefaults.listItemCardStyle.medium(),
    unSelectedBackgroundColor: Color = Color.Transparent,
    onClick: (UUID) -> Unit = {},
    onLongClick: (UUID) -> Unit = {},
    suffix: @Composable () -> Unit = {}
) {
    ListItemCard(
        prefix = {
            AnimationMusicImage(
                url = item.coverThumbnailUrl,
                showAnimation = isPlaying || item.status is MusicStatus.Downloading,
                placeholder = painterResource(R.drawable.ncs_cover),
                lottieRawRes = if (item.status is MusicStatus.Downloading) R.raw.lottie_downloading else R.raw.lottie_playing_music,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(4.dp))
            )
        },
        label = item.title,
        description = item.artistText,
        suffix = suffix,
        style = style,
        color = ListItemCardDefaults.listItemCardColors(
            backgroundColor = if (selected) MaterialTheme.colorScheme.primary else unSelectedBackgroundColor,
            labelColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            descriptionColor = if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
            moreIconColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick(item.id) },
                onLongClick = { onLongClick(item.id) }
            )
    )
}

@Composable
fun MusicCardListItem(
    modifier: Modifier = Modifier,
    music: Music,
    selectedMusicIds: List<UUID>,
    playingMusic: Music? = null,
    updateSelectMusic: (UUID) -> Unit,
    updateSelectMode: (Boolean) -> Unit,
    onClickMore: (UUID) -> Unit,
    onClick: (UUID) -> Unit,
    isSelectMode: Boolean = false
) {
    Column {
        MusicCard(
            item = music,
            selected = selectedMusicIds.any { it == music.id },
            onClickMore = onClickMore,
            isPlaying = music.id == playingMusic?.id,
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    bottom = 12.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .height(58.dp),
            onClick = if (isSelectMode) updateSelectMusic else onClick,
            onLongClick = {
                updateSelectMode(true)
                updateSelectMusic(it)
            },
            isSelectMode = isSelectMode
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            thickness = 1.dp,
            modifier = Modifier
                .padding(
                    bottom = 8.dp,
                    start = 16.dp,
                    end = 16.dp
                )
        )
    }
}

@Composable
fun MusicCardList(
    modifier: Modifier = Modifier,
    musicItems: LazyPagingItems<Music>,
    selectedMusicIds: List<UUID>,
    nestedScrollConnection: NestedScrollConnection,
    updateSelectMusic: (UUID) -> Unit,
    updateSelectMode: (Boolean) -> Unit,
    onClickMore: (UUID) -> Unit,
    onClick: (UUID) -> Unit,
    state: LazyListState,
    isSelectMode: Boolean = false
) {
    LazyColumn(
        state = state,
        modifier = Modifier
            .nestedScroll(nestedScrollConnection)
            .then(modifier)
    ) {
        items(
            count = musicItems.itemCount,
            key = musicItems.itemKey { it.id }
        ) { index ->
            musicItems[index]?.let { music ->
                MusicCardListItem(
                    music = music,
                    selectedMusicIds = selectedMusicIds,
                    updateSelectMusic = updateSelectMusic,
                    updateSelectMode = updateSelectMode,
                    onClickMore = onClickMore,
                    onClick = onClick,
                    isSelectMode = isSelectMode
                )
            }
        }
    }
}

@Preview
@Composable
fun MusicCardPreview() {
    NcsTheme(darkTheme = true) {
        MusicCard(
            item = Music(
                id = UUID.randomUUID(),
                title = "Title",
                artists = listOf(),
                coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/678/325x325/fade-1715299258-UMAjk5s2UY.jpg",
                moods = setOf(),
                genres = setOf(),
                coverUrl = "",
                versions = setOf(),
                dataUrl = "",
                detailUrl = "",
                releaseDate = LocalDate.now()
            ),
            onClickMore = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        )
    }
}

@Preview
@Composable
fun MusicCardListPreview() {
    val musicItems = flowOf(
        PagingData.from(
            listOf(
                Music(
                    id = UUID.randomUUID(),
                    title = "Title 1",
                    artists = listOf(),
                    coverThumbnailUrl = "",
                    moods = setOf(),
                    genres = setOf(),
                    coverUrl = "",
                    versions = setOf(),
                    dataUrl = "",
                    detailUrl = "",
                    releaseDate = LocalDate.now()
                ),
                Music(
                    id = UUID.randomUUID(),
                    title = "Title 2",
                    artists = listOf(),
                    coverThumbnailUrl = "",
                    moods = setOf(),
                    genres = setOf(),
                    coverUrl = "",
                    versions = setOf(),
                    dataUrl = "",
                    detailUrl = "",
                    releaseDate = LocalDate.now()
                ),
                Music(
                    id = UUID.randomUUID(),
                    title = "Long Long Title Long Long Title Long Long Title",
                    artists = listOf(),
                    coverThumbnailUrl = "",
                    moods = setOf(),
                    genres = setOf(),
                    coverUrl = "",
                    versions = setOf(),
                    dataUrl = "",
                    detailUrl = "",
                    releaseDate = LocalDate.now()
                )
            )
        )
    )

    NcsTheme(darkTheme = true) {
        MusicCardList(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            musicItems = musicItems.collectAsLazyPagingItems(),
            selectedMusicIds = listOf(),
            nestedScrollConnection = rememberNestedScrollInteropConnection(),
            updateSelectMusic = {},
            updateSelectMode = {},
            onClickMore = {},
            onClick = {},
            state = rememberLazyListState()
        )
    }
}

@Preview
@Composable
fun SwipeToDeleteMusicCardPreview(modifier: Modifier = Modifier) {
    NcsTheme(darkTheme = true) {
        SwipeToDeleteCard(onDelete = {}) {
            MusicCard(
                item = mockMusics[0],
                suffix = {},
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            )
        }
    }
}