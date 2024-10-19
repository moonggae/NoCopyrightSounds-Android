package com.ccc.ncs.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.ListItemCardDefaults
import com.ccc.ncs.designsystem.component.ListItemCardStyle
import com.ccc.ncs.designsystem.component.SwipeToDeleteCard
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.MusicStatus
import com.ccc.ncs.model.util.reorder
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import java.util.UUID

@Composable
fun ReorderableMusicList(
    modifier: Modifier = Modifier,
    playlistId: UUID? = null,
    musics: List<Music>,
    playingMusicId: UUID? = null,
    cardStyle: ListItemCardStyle = ListItemCardDefaults.listItemCardStyle.small(),
    unSelectedBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    onMusicOrderChanged: (prevIndex: Int, currentIndex: Int) -> Unit,
    onClick: (Int) -> Unit = {},
    onDelete: (Music) -> Unit,
    topLayout: @Composable () -> Unit = {}
) {
    var currentMusics by remember(playlistId, musics.toSet()) { mutableStateOf(musics) }

    var startIndex: Int? by remember { mutableStateOf(null) }
    var targetIndex: Int? by remember { mutableStateOf(null) }

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        // topLayout 인덱스가 포함됨
        val fromIndex = from.index - 1
        val toIndex = to.index - 1
        currentMusics = currentMusics.reorder(fromIndex, toIndex)
        targetIndex = toIndex
    }

    fun moveMusicItem() {
        if (startIndex != null && targetIndex != null) {
            if (startIndex == targetIndex) {
                startIndex = null
                targetIndex = null
                return
            } else {
                onMusicOrderChanged(startIndex!!, targetIndex!!)
                startIndex = null
                targetIndex = null
            }
        }
    }

    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            topLayout()
        }

        items(
            items = currentMusics,
            key = { it.id }
        ) { item ->
            ReorderableItem(state = reorderableLazyListState, key = item.id) { isDragging ->
                SwipeToDeleteCard(
                    onDelete = {
                        currentMusics = currentMusics.minus(item)
                        onDelete(item)
                    },
                    deleteText = stringResource(R.string.Delete)
                ) {
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
                                    .draggableHandle(
                                        onDragStarted = {
                                            startIndex = currentMusics.indexOfFirst { it.id == item.id }
                                        },
                                        onDragStopped = {
                                            moveMusicItem()
                                        }
                                    )
                            )
                        },
                        titlePrefix = {
                            when (item.status) {
                                is MusicStatus.Downloaded,
                                is MusicStatus.FullyCached -> {
                                    Icon(
                                        imageVector = NcsIcons.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .padding(end = 4.dp)
                                            .size(16.dp, 16.dp)
                                    )
                                }

                                else -> {}
                            }
                        },
                        style = cardStyle,
                        unSelectedBackgroundColor = unSelectedBackgroundColor,
                        onClick = { onClick(currentMusics.indexOf(item)) },
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }
            }
        }

        item {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }
    }
}