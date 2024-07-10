package com.ccc.ncs.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ccc.ncs.designsystem.component.ListItemCard
import com.ccc.ncs.model.Artist

@Composable
fun ArtistCard(
    modifier: Modifier = Modifier,
    item: Artist,
    onClick: (Artist) -> Unit
) {
    ListItemCard(
        thumbnail = item.photoUrl,
        label = item.name,
        description = null,
        modifier = Modifier
            .padding(
                top = 8.dp,
                bottom = 12.dp,
                start = 16.dp,
                end = 16.dp
            )
            .clickable { onClick(item) }
            .height(58.dp)
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