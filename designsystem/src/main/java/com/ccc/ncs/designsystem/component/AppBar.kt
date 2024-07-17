package com.ccc.ncs.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTypography

@Composable
fun CommonAppBar(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    onBack: () -> Unit,
    onClickMenu: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(padding)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = NcsIcons.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .clickable(onClick = onBack)
        )


        content()


        if (onClickMenu == null) {
            Spacer(Modifier.size(28.dp))
        } else {
            Icon(
                imageVector = NcsIcons.MoreVertical,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onClickMenu)
            )
        }
    }
}

@Composable
fun CommonAppBar(
    modifier: Modifier = Modifier,
    title: String?,
    padding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    onBack: () -> Unit,
    onClickMenu: (() -> Unit)? = null
) = CommonAppBar(
    modifier = modifier,
    content = {
        Text(
            text = title ?: "",
            style = NcsTypography.Label.appbarTitle.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    },
    padding = padding,
    onBack = onBack,
    onClickMenu = onClickMenu
)