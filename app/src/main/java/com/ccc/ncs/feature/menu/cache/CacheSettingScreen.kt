package com.ccc.ncs.feature.menu.cache

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccc.ncs.CacheUiState
import com.ccc.ncs.CacheViewModel
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.CommonAppBar
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.ui.component.LoadingScreen
import java.util.Locale


@Composable
fun CacheSettingRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    cacheViewModel: CacheViewModel = hiltViewModel(),
    onMoveToSettingSizeScreen: () -> Unit
) {
    val uiState by cacheViewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is CacheUiState.Success -> {
            CacheSettingScreen(
                modifier = modifier,
                onBack = onBack,
                uiState = uiState as CacheUiState.Success,
                onUpdateEnableCache = cacheViewModel::setCacheEnable,
                onMoveToSettingSize = onMoveToSettingSizeScreen,
                onClearCache = cacheViewModel::clearCache
            )
        }

        is CacheUiState.Loading -> LoadingScreen()
    }
}

@Composable
internal fun CacheSettingScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    uiState: CacheUiState.Success,
    onUpdateEnableCache: (Boolean) -> Unit,
    onMoveToSettingSize: () -> Unit,
    onClearCache: () -> Unit
) {
    Column {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

        CommonAppBar(
            title = stringResource(R.string.menu_cache_screen_title),
            onBack = onBack
        )

        Column {
            CacheSpaceInformation(
                usedCacheMb = uiState.usedCacheSizeMb,
                maxCacheMb = uiState.maxCacheSizeMb,
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 20.dp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
            

            SettingContentItem(
                label = stringResource(R.string.menu_cache_screen_enable_cache_label),
                padding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                onClick = { onUpdateEnableCache(!uiState.enableCache) }
            ) {
                Switch(
                    checked = uiState.enableCache,
                    onCheckedChange = onUpdateEnableCache,
                    modifier = Modifier.scale(0.9f)
                )
            }

            SettingContentTextItem(
                label = stringResource(R.string.menu_cache_screen_max_cache_size_label),
                content = "${uiState.maxCacheSizeMb}MB (${String.format (Locale.getDefault(), "%.2f", uiState.maxCacheSizeMb/1024F)}GB)",
                onClick = onMoveToSettingSize
            )

            SettingContentTextItem(
                label = stringResource(R.string.menu_cache_screen_clear_cache_label),
                content = "",
                onClick = onClearCache
            )

            Row(
                Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 28.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = NcsIcons.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = stringResource(R.string.menu_cache_screen_apply_description),
                    style = NcsTypography.Menu.description.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                )
            }
        }
    }
}

@Composable
private fun CacheSpaceInformation(
    modifier: Modifier = Modifier,
    usedCacheMb: Int,
    maxCacheMb: Int
) {
    Column(modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.menu_cache_screen_cached_size_label),
                style = NcsTypography.Menu.itemLabel.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = "$usedCacheMb / $maxCacheMb MB",
                style = NcsTypography.Menu.itemContent.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        LinearProgressIndicator(
            progress = { (usedCacheMb / maxCacheMb.toFloat()).coerceIn(0f, 1f) },
            trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SettingContentItem(
    modifier: Modifier = Modifier,
    label: String,
    padding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(padding)
    ) {
        Text(
            text = label,
            style = NcsTypography.Menu.itemLabel.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        content()
    }
}

@Composable
fun SettingContentTextItem(
    modifier: Modifier = Modifier,
    label: String,
    padding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
    onClick: () -> Unit = {},
    content: String
) {
    SettingContentItem(
        modifier = modifier,
        label = label,
        padding = padding,
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = content,
                style = NcsTypography.Menu.itemContent.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}


@Preview
@Composable
fun CacheSettingScreenPreview() {
    NcsTheme(darkTheme = true) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            CacheSettingScreen(
                onBack = { },
                uiState = CacheUiState.Success(
                    maxCacheSizeMb = 1024,
                    usedCacheSizeBytes = 512 * 1024 * 1024,
                    enableCache = true
                ),
                onUpdateEnableCache = {},
                onMoveToSettingSize = {},
                onClearCache = {}
            )
        }
    }
}