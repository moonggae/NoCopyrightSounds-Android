package com.ccc.ncs.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.TransparentTextField
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    placeholder: String = "",
    focusInitialize: Boolean = true,
    onSearch: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (focusInitialize) focusRequester.requestFocus()
    }

    Column {
        Row(
            modifier = modifier.then(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = NcsIcons.ArrowBack,
                contentDescription = stringResource(R.string.cd_back),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)
                    .clickable { onSearch(state.text.toString()) }
            )

            TransparentTextField(
                state = state,
                placeholder = placeholder,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                onFocusChanged = { isFocused = it },
                onKeyboardActions = {
                    onSearch(state.text.toString())
                }
            )

            if (state.text.isNotEmpty()) {
                Icon(
                    imageVector = NcsIcons.Close,
                    contentDescription = stringResource(R.string.cd_close),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp)
                        .clickable { state.clearText() }
                )
            }
        }

        HorizontalDivider(
            thickness = if (isFocused) 2.dp else 1.dp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.animateContentSize()
        )
    }
}

@Preview
@Composable
fun SearchBarPreview() {
    NcsTheme(darkTheme = true) {
        SearchBar(
            state = rememberTextFieldState(initialText = "awefethrthrt"),
            placeholder = stringResource(R.string.home_search_placeholder),
            onSearch = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun SearchBarEmptyPreview() {
    NcsTheme(darkTheme = true) {
        SearchBar(
            state = rememberTextFieldState(),
            placeholder = stringResource(R.string.home_search_placeholder),
            onSearch = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}