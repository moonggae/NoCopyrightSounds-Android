package com.ccc.ncs.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ccc.ncs.model.Music

@Composable
fun TestMusicCard(
    modifier: Modifier = Modifier,
    item: Music
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ).then(modifier),
    ) {
        AsyncImage(
            model = item.coverThumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 12.dp)
                .size(56.dp)
        )

        Column {
            Text(text = item.title)
            Text(text = item.artist)
        }
    }
}