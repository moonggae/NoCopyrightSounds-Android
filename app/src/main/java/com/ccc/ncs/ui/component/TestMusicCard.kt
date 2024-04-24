package com.ccc.ncs.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ccc.ncs.model.Music

@Composable
fun TestMusicCard(
    modifier: Modifier = Modifier,
    item: Music,
    selected: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = if (selected) MaterialTheme.colorScheme.secondary else Color.Transparent)
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
            .height(56.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.coverThumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 12.dp)
                .fillMaxHeight()
                .aspectRatio(1f)
        )

        Column {
            Text(
                text = item.title,
                color = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = item.artist,
                style = TextStyle(
                    fontSize = 14.sp,
                    color =
                    if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}