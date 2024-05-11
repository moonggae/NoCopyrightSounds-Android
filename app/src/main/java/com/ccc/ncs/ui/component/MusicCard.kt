package com.ccc.ncs.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.model.Music
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicCard(
    modifier: Modifier = Modifier,
    item: Music,
    selected: Boolean = false,
    onClick: (Music) -> Unit = {},
    onLongClick: (Music) -> Unit = {},
    onClickMore: (Music) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = if (selected) MaterialTheme.colorScheme.secondary else Color.Transparent)
            .combinedClickable(
                onClick = { onClick(item) },
                onLongClick = { onLongClick(item) }
            )
            .height(58.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.coverThumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(com.ccc.ncs.network.R.drawable.ncs_cover),
            modifier = Modifier
                .padding(end = 16.dp)
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(4.dp))
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = item.title,
                    style = NcsTypography.Music.Title.medium.copy(
                        color = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface,
                    ),
                    modifier = Modifier.basicMarquee()
                )
                Text(
                    text = item.artist,
                    style = NcsTypography.Music.Artist.medium.copy(
                        color = if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.basicMarquee()
                )
            }

            Icon(
                imageVector = NcsIcons.MoreVertical,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onClickMore(item) }
            )
        }
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
    state: LazyListState
) {
    LazyColumn(
        state = state,
        modifier = Modifier
            .nestedScroll(nestedScrollConnection)
            .then(modifier)
    ) {
        items(count = musicItems.itemCount) { index ->
            musicItems[index]?.let { music ->
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
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
                                ),
                            onClick = updateSelectMusic,
                            onLongClick = {
                                updateSelectMode(true)
                                updateSelectMusic(it)
                            }
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
                    title = "Title 3",
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