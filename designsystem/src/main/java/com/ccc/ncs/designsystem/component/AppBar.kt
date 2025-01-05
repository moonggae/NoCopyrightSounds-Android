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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ccc.ncs.designsystem.R
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTypography

@Composable
fun CommonAppBar(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    onBack: () -> Unit,
    suffix: (@Composable () -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
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
            contentDescription = stringResource(R.string.cd_back),
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onBack)
                .padding(12.dp)
                .size(28.dp)
        )

        if (content == null) {
            Spacer(Modifier.size(28.dp))
        } else {
            content()
        }


        if (suffix == null) {
            Spacer(Modifier.size(28.dp))
        } else {
            suffix()
        }
    }
}

@Composable
fun CommonAppBar(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    onBack: () -> Unit,
    suffixIcon: ImageVector = NcsIcons.MoreVertical,
    onClickSuffix: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) = CommonAppBar(
    modifier = modifier,
    padding = padding,
    onBack = onBack,
    suffix = {
        if (onClickSuffix != null) {
            Icon(
                imageVector = suffixIcon,
                contentDescription = stringResource(R.string.cd_menu),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onClickSuffix)
            )
        }
    },
    content = content,
)

@Composable
fun CommonAppBar(
    modifier: Modifier = Modifier,
    title: String?,
    padding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    onBack: () -> Unit,
    suffixIcon: ImageVector = NcsIcons.MoreVertical,
    onClickSuffix: (() -> Unit)? = null
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
    suffixIcon = suffixIcon,
    onClickSuffix = onClickSuffix
)