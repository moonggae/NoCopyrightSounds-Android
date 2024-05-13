package com.ccc.ncs.designsystem.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTypography

data class ListItemCardColors(
    val labelColor: Color,
    val descriptionColor: Color,
    val moreIconColor: Color,
    val backgroundColor: Color
)

object ListItemCardDefaults {
    @Composable
    fun listItemCardColors(
        labelColor: Color = Color.Unspecified,
        descriptionColor: Color = Color.Unspecified,
        moreIconColor: Color = Color.Unspecified,
        backgroundColor: Color = Color.Unspecified,
    ): ListItemCardColors =
        ListItemCardColors(
            labelColor = labelColor.takeOrElse { MaterialTheme.colorScheme.onSurface },
            descriptionColor = descriptionColor.takeOrElse { MaterialTheme.colorScheme.onSurfaceVariant },
            moreIconColor = moreIconColor.takeOrElse { MaterialTheme.colorScheme.onSurface },
            backgroundColor = backgroundColor.takeOrElse { Color.Transparent }
        )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItemCard(
    modifier: Modifier = Modifier,
    thumbnail: Painter,
    label: String,
    description: String,
    color: ListItemCardColors = ListItemCardDefaults.listItemCardColors(),
    onMoreClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .background(color.backgroundColor)
            .then(modifier.height(58.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = thumbnail,
            contentDescription = null,
            contentScale = ContentScale.Crop,
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
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    style = NcsTypography.MusicTypography.TitleTypography.medium.copy(
                        color = color.labelColor,
                    ),
                    modifier = Modifier.basicMarquee()
                )
                Text(
                    text = description,
                    style = NcsTypography.MusicTypography.ArtistTypography.medium.copy(
                        color = color.descriptionColor
                    ),
                    modifier = Modifier.basicMarquee()
                )
            }

            onMoreClick?.let {
                Icon(
                    imageVector = NcsIcons.MoreVertical,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { it.invoke() }
                )
            }
        }
    }
}

@Composable
fun ListItemCard(
    modifier: Modifier = Modifier,
    thumbnail: String,
    thumbnailPlaceholder: Painter? = null,
    label: String,
    description: String,
    color: ListItemCardColors = ListItemCardDefaults.listItemCardColors(),
    onMoreClick: (() -> Unit)? = null
) {
    ListItemCard(
        modifier = modifier,
        thumbnail = rememberAsyncImagePainter(
            model = thumbnail,
            placeholder = thumbnailPlaceholder
        ),
        label = label,
        description = description,
        color = color,
        onMoreClick = onMoreClick
    )
}