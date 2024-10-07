package com.ccc.ncs.designsystem.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.ccc.ncs.designsystem.theme.NcsTypography

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    placeholder: String = "",
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
    showUnderline: Boolean = false,
    underLineColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onFocusChanged: (Boolean) -> Unit = {},
    textStyle: TextStyle = NcsTypography.SearchTypography.placeholder.copy(
        color = MaterialTheme.colorScheme.onSurface
    )
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        onFocusChanged(isFocused)
    }

    BasicTextField2(
        state = state,
        modifier = modifier,
        textStyle = textStyle,
        lineLimits = TextFieldLineLimits.SingleLine,
        interactionSource = interactionSource,
        keyboardActions = keyboardActions,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        decorator = { innerTextField ->
            Column {
                Box {
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
                }

                if (showUnderline) {
                    HorizontalDivider(color = underLineColor)
                }
            }
        },
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface)
    )
}