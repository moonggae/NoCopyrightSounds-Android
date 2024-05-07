package com.ccc.ncs.feature.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.ui.component.SearchBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SearchRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onSearch: (String) -> Unit
) {
    SearchScreen(
        modifier = modifier,
        queryState = viewModel.queryState,
        onSearch = {
            onSearch(it)
            viewModel.onSearchTriggered(it)
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    queryState: TextFieldState,
    onSearch: (String) -> Unit,
) {
    BackHandler {
        onSearch(queryState.text.toString())
    }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

        SearchBar(
            state = queryState,
            placeholder = stringResource(R.string.home_search_placeholder),
            onSearch = onSearch
        )

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun SearchScreenPreview(

) {
    NcsTheme(darkTheme = true) {
        SearchScreen(
            queryState = rememberTextFieldState("asdfavfd"),
            onSearch = {}
        )
    }
}