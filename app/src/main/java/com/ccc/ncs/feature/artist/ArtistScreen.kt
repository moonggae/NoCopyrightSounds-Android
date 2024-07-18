package com.ccc.ncs.feature.artist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.CommonModalBottomSheet
import com.ccc.ncs.feature.home.CustomFlowRow
import com.ccc.ncs.feature.home.SearchAppBar
import com.ccc.ncs.model.Artist
import com.ccc.ncs.ui.component.ArtistListCard
import com.ccc.ncs.ui.component.ClickableSearchBar
import com.ccc.ncs.ui.component.DropDownButton
import java.time.LocalDate

@Composable
fun ArtistRoute(
    modifier: Modifier = Modifier,
    viewModel: ArtistViewModel = hiltViewModel(),
    onUpdateSearchQuery: (query: String?) -> Unit,
    onClickSearchBar: (String?) -> Unit,
    onClickArtist: (Artist) -> Unit
) {
    val searchUiState by viewModel.searchUiState.collectAsStateWithLifecycle()

    ArtistScreen(
        artists = viewModel.artists.collectAsLazyPagingItems(),
        searchUiState = searchUiState,
        onClickSearchBar = onClickSearchBar,
        onUpdateSearchQuery = onUpdateSearchQuery,
        onUpdateSort = viewModel::updateSort,
        onUpdateReleaseYears = viewModel::updateYear,
        onClickArtist = onClickArtist
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArtistScreen(
    modifier: Modifier = Modifier,
    artists: LazyPagingItems<Artist>,
    searchUiState: ArtistSearchUiState,
    onClickSearchBar: (String?) -> Unit,
    onUpdateSearchQuery: (query: String?) -> Unit,
    onUpdateSort: (ArtistSort) -> Unit,
    onUpdateReleaseYears: (Int?) -> Unit,
    onClickArtist: (Artist) -> Unit
) {
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var appbarHeight by remember { mutableStateOf(120.dp) }

    LaunchedEffect(artists.loadState.refresh) {
        if (artists.loadState.refresh is LoadState.Loading) {
            listState.scrollToItem(0)
        }
    }

    Column {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        SearchAppBar(
            containerHeight = appbarHeight,
            scrollBehavior = scrollBehavior
        ) {
            ArtistSearchBox(
                uiState = searchUiState,
                onClickSearchBar = onClickSearchBar,
                onUpdateSearchQuery = onUpdateSearchQuery,
                onUpdateSort = onUpdateSort,
                onUpdateReleaseYears = onUpdateReleaseYears,
                onMenuLineCountChanged = {
                    when (it) {
                        1 -> appbarHeight = 120.dp
                        2 -> appbarHeight = 168.dp
                    }
                }
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .then(modifier)
        ) {
            items(count = artists.itemCount) { index ->
                artists[index]?.let { artist ->
                    ArtistListCard(
                        item = artist,
                        onClick = onClickArtist,
                        modifier = Modifier
                            .padding(
                                top = 8.dp,
                                bottom = 12.dp,
                                start = 16.dp,
                                end = 16.dp
                            )
                            .height(58.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun ArtistSearchBox(
    uiState: ArtistSearchUiState,
    onClickSearchBar: (String?) -> Unit,
    onUpdateSearchQuery: (query: String?) -> Unit,
    onUpdateSort: (ArtistSort) -> Unit,
    onUpdateReleaseYears: (Int?) -> Unit,
    onMenuLineCountChanged: (Int) -> Unit = {}
) {
    var showAristSortModalBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showAristYearModalBottomSheet by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(12.dp)
    ) {
        ClickableSearchBar(
            query = uiState.query,
            placeholder = stringResource(R.string.artist_search_placeholder),
            onClick = { onClickSearchBar(uiState.query) },
            onClickDelete = { onUpdateSearchQuery(null) }
        )

        CustomFlowRow(
            onLineCountChanged = onMenuLineCountChanged
        ) {
            DropDownButton(
                label = uiState.sort.getArtistSortLabel(),
                onClick = { showAristSortModalBottomSheet = true }
            )

            DropDownButton(
                label = "${uiState.year ?: stringResource(R.string.artist_year_all_release_years)}",
                onClick = { showAristYearModalBottomSheet = true }
            )
        }
    }


    if (showAristSortModalBottomSheet) {
        AristSortModalBottomSheet(
            onDismissRequest = { showAristSortModalBottomSheet = false },
            onItemSelected = onUpdateSort
        )
    }

    if (showAristYearModalBottomSheet) {
        AristYearModalBottomSheet(
            onDismissRequest = { showAristYearModalBottomSheet = false },
            onItemSelected = onUpdateReleaseYears
        )
    }
}

@Composable
fun ArtistSort.getArtistSortLabel(): String =
    when (this) {
        ArtistSort.LATEST_ARTISTS -> stringResource(R.string.artist_sort_label_latest_artists)
        ArtistSort.NAME_A_TO_Z -> stringResource(R.string.artist_sort_label_a_to_z)
        ArtistSort.NAME_Z_TO_A -> stringResource(R.string.artist_sort_label_z_to_a)
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AristSortModalBottomSheet(
    onDismissRequest: () -> Unit,
    onItemSelected: (ArtistSort) -> Unit
) {
    CommonModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            ArtistSort.entries.forEach {
                ArtistSearchBottomSheetItem(
                    text = it.getArtistSortLabel(),
                    onClick = {
                        onItemSelected(it)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AristYearModalBottomSheet(
    onDismissRequest: () -> Unit,
    onItemSelected: (Int?) -> Unit
) {
    val years: List<Int?> = mutableListOf<Int?>(null).apply {
        addAll((2013..LocalDate.now().year).reversed())
    }

    CommonModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
                .verticalScroll(rememberScrollState()),
        ) {
            years.forEach {
                ArtistSearchBottomSheetItem(
                    text = "${it ?: stringResource(R.string.artist_year_all_release_years)}",
                    onClick = {
                        onItemSelected(it)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}

@Composable
private fun ArtistSearchBottomSheetItem(
    text: String,
    onClick: () -> Unit
) =
    Text(
        text = text,
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(
                vertical = 4.dp,
                horizontal = 12.dp
            )
    )