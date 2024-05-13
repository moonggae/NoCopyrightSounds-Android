package com.ccc.ncs.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.ListItemCard
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import java.time.LocalDate
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayListCard(
    modifier: Modifier = Modifier,
    item: PlayList,
    onClick: (PlayList) -> Unit = {}
) {
    val placeholder = painterResource(R.drawable.ncs_cover)

    val thumbnail =
        if (item.musics.isEmpty()) placeholder
        else rememberAsyncImagePainter(
            model = item.musics.first().coverThumbnailUrl,
            placeholder = placeholder
        )

    ListItemCard(
        thumbnail = thumbnail,
        label = item.name,
        description = stringResource(R.string.songs_count, item.musics.size),
        onMoreClick = null,
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick(item) }
            )
            .then(modifier)
    )
}

@Composable
fun PlayListColumnItem(
    modifier: Modifier = Modifier,
    playList: PlayList,
    onClick: (PlayList) -> Unit
) {
    Column {
        PlayListCard(
            item = playList,
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    bottom = 12.dp
                )
                .height(58.dp),
            onClick = onClick
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            thickness = 1.dp,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
    }
}

@Composable
fun PlayListColumn(
    modifier: Modifier = Modifier,
    playListItems: List<PlayList>,
    onClick: (PlayList) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.then(modifier)
    ) {
        items(count = playListItems.size) { index ->
            PlayListColumnItem(
                modifier = modifier,
                playList = playListItems[index],
                onClick = onClick
            )
        }
    }
}



private val mockMusics = listOf(
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

@Preview
@Composable
fun PlayListPreview() {
    NcsTheme(darkTheme = true) {
        PlayListCard(
            item = PlayList(
                id = UUID.randomUUID(),
                name = "PlayList",
                musics = mockMusics
            ),
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        )
    }
}

@Preview
@Composable
fun PlayListsPreview() {


    NcsTheme(darkTheme = true) {
        PlayListColumn(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            onClick = {},
            playListItems = listOf(
                PlayList(
                    id = UUID.randomUUID(),
                    name = "PlayList 1",
                    musics = mockMusics
                ),
                PlayList(
                    id = UUID.randomUUID(),
                    name = "PlayList 2",
                    musics = mockMusics
                ),
                PlayList(
                    id = UUID.randomUUID(),
                    name = "PlayList 3",
                    musics = mockMusics
                )
            ),
        )
    }
}