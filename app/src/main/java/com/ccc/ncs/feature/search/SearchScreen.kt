package com.ccc.ncs.feature.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.model.RecentSearchQuery
import com.ccc.ncs.ui.component.SearchBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SearchRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onSearch: (String) -> Unit
) {
    val recentSearchQueriesUiState by viewModel.recentSearchQueriesUiState.collectAsStateWithLifecycle()

    SearchScreen(
        modifier = modifier,
        queryState = viewModel.queryState,
        onSearch = {
            onSearch(it)
            viewModel.onSearchTriggered(it)
        },
        recentSearchesUiState = recentSearchQueriesUiState,
        onDeleteRecentSearch = viewModel::deleteRecentSearch
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    queryState: TextFieldState,
    onSearch: (String) -> Unit,
    recentSearchesUiState: RecentSearchQueriesUiState = RecentSearchQueriesUiState.Loading,
    onDeleteRecentSearch: (String) -> Unit
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
            onSearch = onSearch,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        if (recentSearchesUiState is RecentSearchQueriesUiState.Success) {
            RecentSearchesBody(
                onDeleteRecentSearch = onDeleteRecentSearch,
                onRecentSearchClicked = onSearch,
                recentSearchQueries = recentSearchesUiState.recentQueries.map { it.query },
                modifier = Modifier.padding(top = 28.dp)
            )
        }
    }
}

@Composable
private fun RecentSearchesBody(
    modifier: Modifier = Modifier,
    recentSearchQueries: List<String>,
    onDeleteRecentSearch: (String) -> Unit,
    onRecentSearchClicked: (String) -> Unit,
) {
    LazyColumn(modifier) {
        items(recentSearchQueries) { recentSearch ->
            RecentSearchItem(
                searchQuery = recentSearch,
                onClickItem = onRecentSearchClicked,
                onClickItemDelete = onDeleteRecentSearch,
                modifier = Modifier
                    .height(48.dp)
                    .padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun RecentSearchItem(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onClickItem: (String) -> Unit,
    onClickItemDelete: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.then(
            Modifier.clickable { onClickItem(searchQuery) }
        )
    ) {
        Icon(
            imageVector = NcsIcons.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(end = 6.dp)
                .size(24.dp)
        )

        Text(
            text = searchQuery,
            style = NcsTypography.Search.content.copy(
                color = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = NcsIcons.Close,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(start = 16.dp)
                .size(24.dp)
                .clickable { onClickItemDelete(searchQuery) }
        )
    }
}

@Composable
@Preview
fun RecentSearchItemPreview(

) {
    NcsTheme(darkTheme = true) {
        RecentSearchItem(
            searchQuery = "alan walker",
            onClickItem = {},
            onClickItemDelete = {}
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
            onSearch = {},
            onDeleteRecentSearch = {},
            recentSearchesUiState = RecentSearchQueriesUiState.Success(
                listOf(
                    RecentSearchQuery("alan walker"),
                    RecentSearchQuery("fried chicken"),
                    RecentSearchQuery("Pizza"),
                )
            )
        )
    }
}