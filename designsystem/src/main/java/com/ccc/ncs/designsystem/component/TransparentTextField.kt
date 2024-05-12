package com.ccc.ncs.designsystem.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import com.ccc.ncs.designsystem.theme.NcsTypography

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    placeholder: String = "",
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onFocusChanged: (Boolean) -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        onFocusChanged(isFocused)
    }

    BasicTextField2(
        state = state,
        modifier = modifier,
        textStyle = NcsTypography.SearchTypography.placeholder.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        interactionSource = interactionSource,
        keyboardActions = keyboardActions,
        decorator = { innerTextField ->
            val isPlaceholderVisible = state.text.isEmpty() && placeholder.isNotEmpty()
            if (isPlaceholderVisible) {
                Text(
                    text = placeholder,
                    style = NcsTypography.SearchTypography.placeholder.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            innerTextField()
        },
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface)
    )
}