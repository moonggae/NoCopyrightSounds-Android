package com.ccc.ncs.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ccc.ncs.designsystem.R
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography


@Composable
fun AlertDialog(
    modifier: Modifier = Modifier,
    show: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    message: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onCancel: (() -> Unit)? = null
) {
    NcsDialog(
        show = show,
        onDismissRequest = onDismissRequest,
        title = title,
        message = message,
        bottomContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                NcsDialogTextButton(
                    label = confirmLabel,
                    onClick = onConfirm,
                    color = MaterialTheme.colorScheme.error
                )

                VerticalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                )

                NcsDialogTextButton(
                    label = stringResource(R.string.Cancel),
                    onClick = { (onCancel ?: onDismissRequest).invoke() },
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}

@Composable
fun NcsDialog(
    modifier: Modifier = Modifier,
    show: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    message: String,
    bottomContent: @Composable () -> Unit,
) {
    NcsDialog(
        modifier = modifier,
        show = show,
        onDismissRequest = onDismissRequest,
        title = title,
        content = {
            Text(
                text = message,
                style = NcsTypography.Dialog.message.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
            )
        },
        bottomContent = bottomContent
    )
}

@Composable
fun NcsDialog(
    modifier: Modifier = Modifier,
    show: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    content: @Composable () -> Unit,
    bottomContent: @Composable () -> Unit,
) {
    if (show) {
        Dialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = title,
                        style = NcsTypography.Dialog.title.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        )
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    )

                    content()
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                )

                bottomContent()
            }
        }
    }
}

@Composable
fun RowScope.NcsDialogTextButton(
    modifier: Modifier = Modifier,
    label: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Text(
        text = label,
        style = NcsTypography.Dialog.button.copy(
            color = color
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxHeight()
            .wrapContentHeight()
            .weight(1f)
    )
}

@Preview
@Composable
fun AlertDialogPreview(modifier: Modifier = Modifier) {
    NcsTheme(darkTheme = true) {
        AlertDialog(
            show = true,
            onDismissRequest = {},
            title = "Delete Playlist",
            message = "Are you sure\nyou want to delete this playlist?",
            confirmLabel = "Delete",
            onConfirm = {}
        )
    }
}