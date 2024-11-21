package com.ccc.ncs.feature.play

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ccc.ncs.designsystem.component.ListItemCardDefaults
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.ui.component.ReorderableMusicList

@Composable
fun PlayerMenuPlaylistTabView(
    modifier: Modifier = Modifier,
    playlist: PlayList,
    onMusicOrderChanged: (prevIndex: Int, currentIndex: Int) -> Unit,
    currentMusic: Music?,
    onClick: (Int) -> Unit,
    onDelete: (Music) -> Unit
) {
    ReorderableMusicList(
        modifier = modifier
            .padding(
                horizontal = 8.dp,
                vertical = 12.dp
            )
            .fillMaxSize(),
        playlistId = playlist.id,
        musics = playlist.musics,
        playingMusicId = currentMusic?.id,
        cardStyle = ListItemCardDefaults.listItemCardStyle.medium(),
        unSelectedBackgroundColor = MaterialTheme.colorScheme.surfaceContainer,
        onMusicOrderChanged = onMusicOrderChanged,
        onClick = onClick,
        onDelete = onDelete
    )
}