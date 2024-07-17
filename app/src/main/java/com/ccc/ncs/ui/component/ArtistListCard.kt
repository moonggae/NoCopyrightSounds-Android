package com.ccc.ncs.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.ListItemCard
import com.ccc.ncs.designsystem.component.ListItemCardDefaults
import com.ccc.ncs.designsystem.component.ListItemCardStyle
import com.ccc.ncs.model.Artist

@Composable
fun ArtistListCard(
    modifier: Modifier = Modifier,
    item: Artist,
    showDivider: Boolean = true,
    style: ListItemCardStyle = ListItemCardDefaults.listItemCardStyle.medium(),
    onClick: (Artist) -> Unit
) {
    ListItemCard(
        thumbnail = item.photoUrl,
        thumbnailPlaceholder = painterResource(R.drawable.ncs_cover),
        label = item.name,
        description = item.tags,
        style = style,
        modifier = modifier.clickable { onClick(item) }
    )

    if (showDivider) {
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