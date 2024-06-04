package com.ccc.ncs.feature.play

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ccc.ncs.designsystem.component.ListItemCardDefaults
import com.ccc.ncs.feature.library.detail.PlaylistDetailMusicList
import com.ccc.ncs.model.PlayList

@Composable
fun PlayerMenuPlaylistTabView(
    modifier: Modifier = Modifier,
    playlist: PlayList,
    onMusicOrderChanged: (prevIndex: Int, currentIndex: Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PlaylistDetailMusicList(
            modifier = Modifier
                .padding(
                    horizontal = 8.dp,
                    vertical = 12.dp
                )
                .fillMaxWidth(),
            playlistId = playlist.id,
            musics = playlist.musics,
            cardStyle = ListItemCardDefaults.listItemCardStyle.medium(),
            onMusicOrderChanged = onMusicOrderChanged
        )
    }
}