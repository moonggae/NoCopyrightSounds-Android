package com.ccc.ncs.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.ListItemCard
import com.ccc.ncs.designsystem.component.ListItemCardDefaults
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.model.Music
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicCard(
    modifier: Modifier = Modifier,
    item: Music,
    isSelectMode: Boolean = false,
    selected: Boolean = false,
    onClick: (Music) -> Unit = {},
    onLongClick: (Music) -> Unit = {},
    onClickMore: (Music) -> Unit = {}
) {
    ListItemCard(
        thumbnail = item.coverThumbnailUrl,
        label = item.title,
        description = item.artist,
        onMoreClick = if (isSelectMode) null else {
            { onClickMore(item) }
        },
        color = ListItemCardDefaults.listItemCardColors(
            backgroundColor = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
            labelColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            descriptionColor = if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
            moreIconColor = MaterialTheme.colorScheme.onSurface
        ),
        thumbnailPlaceholder = painterResource(R.drawable.ncs_cover),
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick(item) },
                onLongClick = { onLongClick(item) }
            )
            .then(modifier)
    )
}

@Composable
fun MusicCardListItem(
    modifier: Modifier = Modifier,
    music: Music,
    selectedMusics: List<Music>,
    updateSelectMusic: (Music) -> Unit,
    updateSelectMode: (Boolean) -> Unit,
    onClickMore: (Music) -> Unit,
    isSelectMode: Boolean = false
) {
    Column {
        MusicCard(
            item = music,
            selected = selectedMusics.contains(music),
            onClickMore = onClickMore,
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    bottom = 12.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .height(58.dp),
            onClick = updateSelectMusic,
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
    selectedMusics: List<Music>,
    nestedScrollConnection: NestedScrollConnection,
    updateSelectMusic: (Music) -> Unit,
    updateSelectMode: (Boolean) -> Unit,
    onClickMore: (Music) -> Unit,
    state: LazyListState,
    isSelectMode: Boolean = false
) {
    LazyColumn(
        state = state,
        modifier = Modifier
            .nestedScroll(nestedScrollConnection)
            .then(modifier)
    ) {
        items(count = musicItems.itemCount) { index ->
            musicItems[index]?.let { music ->
                MusicCardListItem(
                    music = music,
                    selectedMusics = selectedMusics,
                    updateSelectMusic = updateSelectMusic,
                    updateSelectMode = updateSelectMode,
                    onClickMore = onClickMore,
                    isSelectMode = isSelectMode,
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
                artist = "Artist",
                coverThumbnailUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/678/325x325/fade-1715299258-UMAjk5s2UY.jpg",
                moods = setOf(),
                genres = setOf(),
                coverUrl = "",
                versions = setOf(),
                dataUrl = "",
                detailUrl = "",
                releaseDate = LocalDate.now(),
                artistDetailUrl = ""
            ),
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
                    artist = "Artist 1",
                    coverThumbnailUrl = "",
                    moods = setOf(),
                    genres = setOf(),
                    coverUrl = "",
                    versions = setOf(),
                    dataUrl = "",
                    detailUrl = "",
                    releaseDate = LocalDate.now(),
                    artistDetailUrl = ""
                ),
                Music(
                    id = UUID.randomUUID(),
                    title = "Title 2",
                    artist = "Artist 2",
                    coverThumbnailUrl = "",
                    moods = setOf(),
                    genres = setOf(),
                    coverUrl = "",
                    versions = setOf(),
                    dataUrl = "",
                    detailUrl = "",
                    releaseDate = LocalDate.now(),
                    artistDetailUrl = ""
                ),
                Music(
                    id = UUID.randomUUID(),
                    title = "Long Long Title Long Long Title Long Long Title",
                    artist = "Artist 3",
                    coverThumbnailUrl = "",
                    moods = setOf(),
                    genres = setOf(),
                    coverUrl = "",
                    versions = setOf(),
                    dataUrl = "",
                    detailUrl = "",
                    releaseDate = LocalDate.now(),
                    artistDetailUrl = ""
                )
            )
        )
    )

    NcsTheme(darkTheme = true) {
        MusicCardList(
            musicItems = musicItems.collectAsLazyPagingItems(),
            updateSelectMusic = {},
            selectedMusics = listOf(),
            state = rememberLazyListState(),
            updateSelectMode = {},
            nestedScrollConnection = rememberNestedScrollInteropConnection(),
            onClickMore = {},
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
        )
    }
}