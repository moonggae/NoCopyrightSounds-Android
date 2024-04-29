package com.ccc.ncs.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.designsystem.theme.ncsTypography
import com.ccc.ncs.model.Music

@OptIn(ExperimentalFoundationApi::class)
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
                style = NcsTypography.Music.Title.medium.copy(
                    color = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface
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
    }
}