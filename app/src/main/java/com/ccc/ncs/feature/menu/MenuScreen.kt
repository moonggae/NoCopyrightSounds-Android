package com.ccc.ncs.feature.menu

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.ccc.ncs.MainActivity
import com.ccc.ncs.R
import com.ccc.ncs.analytics.TrackScreenViewEvent
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.util.conditional
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

@Composable
fun MenuRoute(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onMoveCacheScreen: () -> Unit
) {
    MenuScreen(
        onMoveCacheScreen = onMoveCacheScreen
    )

    TrackScreenViewEvent("Menu")
}

@Composable
internal fun MenuScreen(
    onMoveCacheScreen: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val activity = LocalContext.current as MainActivity

    Column {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        MenuItem(label = stringResource(R.string.menu_open_source_licenses)) {
            activity.startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
        }

        MenuItem(label = stringResource(R.string.menu_cache)) {
            onMoveCacheScreen()
        }

        ExpandableMenuItem(label = stringResource(R.string.menu_contact)) {
            ContactContent()
        }
    }
}

@Composable
fun ContactContent() {
    val clipboardManager = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current
    val email = stringResource(id = R.string.ContactEmail)
    val githubRepo = stringResource(id = R.string.GithubRepositoryUrl)


    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(start = 20.dp, top = 4.dp, bottom = 4.dp)
    ) {
        CopyableRow(
            icon = { Icon(imageVector = NcsIcons.Email, contentDescription = stringResource(R.string.cd_email_icon)) },
            text = email,
            copyButtonContentDescription = stringResource(R.string.cd_copy_email_button),
            onCopy = {
                clipboardManager.setText(AnnotatedString(email))
            }
        )

        CopyableRow(
            icon = {
                Icon(
                    imageVector = NcsIcons.Code,
                    contentDescription = stringResource(R.string.cd_code_icon)
                )
            },
            text = stringResource(R.string.menu_github_repository),
            onClick = { uriHandler.openUri(githubRepo) },
            copyButtonContentDescription = stringResource(R.string.cd_copy_repository_button),
            onCopy = {
                clipboardManager.setText(AnnotatedString(githubRepo))
            }
        )
    }
}

@Composable
fun CopyableRow(
    icon: @Composable () -> Unit,
    text: String,
    onClick: (() -> Unit)? = null,
    copyButtonContentDescription: String?,
    onCopy: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        icon()
        Spacer(modifier = Modifier.width(8.dp))

        if (onClick == null) {
            Text(text)
        }

        else {
            Text(
                text = text,
                modifier = Modifier.clickable(onClick = onClick)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = NcsIcons.Copy,
            contentDescription = copyButtonContentDescription,
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onCopy)
                .padding(16.dp)
                .size(16.dp)
        )
    }
}

@Composable
fun MenuItem(
    label: String,
    onClick: () -> Unit = {}
) {
    Column {
        Text(
            text = label,
            modifier = Modifier
                .clickable(onClick = onClick)
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                ),
            style = NcsTypography.Menu.itemLabel.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            thickness = 0.5.dp
        )
    }
}

@Composable
fun ExpandableMenuItem(
    label: String,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = NcsTypography.Menu.itemLabel.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Icon(
                imageVector = NcsIcons.ArrowDropDown,
                contentDescription =
                if (expanded) stringResource(R.string.cd_shrink_menu_arrow)
                else stringResource(R.string.cd_expand_menu_arrow),
                modifier = Modifier.conditional(expanded) {
                    rotate(180f)
                }
            )
        }

        AnimatedVisibility(visible = expanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 8.dp
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                content()
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            thickness = 0.5.dp
        )
    }
}