package com.ccc.ncs.feature.menu.cache

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.foundation.text2.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccc.ncs.CacheUiState
import com.ccc.ncs.CacheViewModel
import com.ccc.ncs.designsystem.component.CommonAppBar
import com.ccc.ncs.designsystem.component.TransparentTextField
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.ui.component.LoadingScreen
import java.util.Locale


@Composable
fun CacheSizeSettingRoute(
    cacheViewModel: CacheViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by cacheViewModel.uiState.collectAsStateWithLifecycle()

    when (val immutableState = uiState) {
        is CacheUiState.Success -> {
            CacheSizeSettingScreen(
                onBack = onBack,
                currentMaxCacheSize = immutableState.maxCacheSizeMb,
                onUpdateCacheSize = cacheViewModel::setCacheSize
            )
        }

        is CacheUiState.Loading -> LoadingScreen()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CacheSizeSettingScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    currentMaxCacheSize: Int,
    onUpdateCacheSize: (Int) -> Unit,
) {
    val mbSizeState = rememberTextFieldState(initialText = "$currentMaxCacheSize")
    val gbSizeState = rememberTextFieldState(initialText = String.format(Locale.getDefault(), "%.2f", currentMaxCacheSize / 1024F))

    val enableToSave: Boolean = remember(mbSizeState.text.toString()) {
        val mbSize = mbSizeState.text.toString().toIntOrNull() ?: -1
        mbSize > 0
    }

    var mbTextFocus by remember { mutableStateOf(false) }
    var gbTextFocus by remember { mutableStateOf(false) }


    LaunchedEffect(mbSizeState.text) {
        if (mbTextFocus && !gbTextFocus) {
            gbSizeState.setTextAndPlaceCursorAtEnd(
                try {
                    String.format(Locale.getDefault(), "%.2f", (mbSizeState.text.toString().toInt() / 1024F).coerceAtLeast(0f))
                } catch (e: Throwable) {
                    ""
                }
            )
        }
    }

    LaunchedEffect(gbSizeState.text) {
        if (gbTextFocus && !mbTextFocus) {
            mbSizeState.setTextAndPlaceCursorAtEnd("${
                try {
                    (gbSizeState.text.toString().toFloat() * 1024F).toInt().coerceAtLeast(0)
                } catch (th: Throwable) {
                    ""
                }
            }")
        }
    }

    Column {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        CommonAppBar(title = "Max Cache Size", onBack = onBack)

        Column(Modifier.padding(horizontal = 20.dp, vertical = 32.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TransparentTextField(
                    state = mbSizeState,
                    placeholder = "1024",
                    keyboardType = KeyboardType.Number,
                    onFocusChanged = { mbTextFocus = it },
                    textStyle = NcsTypography.Menu.itemContent.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    showUnderline = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 20.dp)
                )

                Text(
                    text = "MB",
                    style = NcsTypography.Menu.itemLabel.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TransparentTextField(
                    state = gbSizeState,
                    placeholder = "1",
                    keyboardType = KeyboardType.Decimal,
                    onFocusChanged = { gbTextFocus = it },
                    textStyle = NcsTypography.Menu.itemContent.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    showUnderline = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 20.dp)
                )

                Text(
                    text = "GB",
                    style = NcsTypography.Menu.itemLabel.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))


            Button(
                onClick = {
                    val newSize = mbSizeState.text.toString().toIntOrNull()
                    if (newSize != null) {
                        onUpdateCacheSize(newSize)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = enableToSave
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Preview
@Composable
fun CacheSizeSettingScreenPreview() {
    NcsTheme(darkTheme = true) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)) {
            CacheSizeSettingScreen(
                onBack = {},
                currentMaxCacheSize = 1024,
                onUpdateCacheSize = {}
            )
        }
    }
}