package com.ccc.ncs.feature.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import com.ccc.ncs.ui.component.DropDownButton
import com.ccc.ncs.ui.component.GenreModalBottomSheet
import com.ccc.ncs.ui.component.MoodModalBottomSheet
import com.ccc.ncs.ui.component.SearchBar
import com.ccc.ncs.ui.component.TestMusicCard

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        modifier = modifier,
        testMusics = viewModel.musics.collectAsLazyPagingItems(),
        homeUiState = homeUiState,
        onSearchMusic = viewModel::searchMusic,
        updateSelectMode = viewModel::updateSelectMode,
        updateSelectMusic = viewModel::updateSelectMusic
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    testMusics: LazyPagingItems<Music>,
    homeUiState: HomeUiState,
    onSearchMusic: (query: String?, genreId: Int?, moodId: Int?) -> Unit,
    updateSelectMode: (Boolean) -> Unit,
    updateSelectMusic: (Music) -> Unit
) {
    val listState = rememberLazyListState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Box {
        Column {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            SearchAppBar(scrollBehavior = scrollBehavior) {
                SearchBox(
                    uiState = homeUiState.searchUiState,
                    genres = homeUiState.genres,
                    moods = homeUiState.moods,
                    onUpdateSearch = onSearchMusic,
                    onClickSearchBar = {
                        // todo
                    }
                )
            }

            if (homeUiState.isSelectMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
//                        playerViewModel.setPlayList(
//                            PlayList(
//                                id = UUID.randomUUID(),
//                                name = "PlayList",
//                                musics = uiState.selectedMusics
//                            )
//                        )
                    }) {
                        Icon(imageVector = NcsIcons.Play, contentDescription = null)
                    }

                    IconButton(onClick = { updateSelectMode(false) }) {
                        Icon(imageVector = NcsIcons.Close, contentDescription = null)
                    }
                }
            }


            LazyColumn(
                state = listState,
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                items(count = testMusics.itemCount) { index ->
                    testMusics[index]?.let { music ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TestMusicCard(
                                item = music,
                                selected = homeUiState.selectedMusics.contains(music),
                                modifier = Modifier
                                    .combinedClickable(
                                        onClick = {
                                            updateSelectMusic(music)
                                        },
                                        onLongClick = {
                                            updateSelectMode(true)
                                            updateSelectMusic(music)
                                        }
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    scrollBehavior: TopAppBarScrollBehavior?,
    containerHeight: Dp = 120.dp,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit
) {
    val heightOffsetLimit =
        with(LocalDensity.current) { -containerHeight.toPx() }
    SideEffect {
        if (scrollBehavior?.state?.heightOffsetLimit != heightOffsetLimit) {
            scrollBehavior?.state?.heightOffsetLimit = heightOffsetLimit
        }
    }

    val height = LocalDensity.current.run {
        containerHeight.toPx() + (scrollBehavior?.state?.heightOffset ?: 0f)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(with(LocalDensity.current) { height.toDp() })
            .background(containerColor)
    ) {
        content()
    }
}

@Composable
fun SearchBox(
    uiState: SearchUiState,
    genres: List<Genre>,
    moods: List<Mood>,
    onClickSearchBar: () -> Unit,
    onUpdateSearch: (query: String?, genreId: Int?, moodId: Int?) -> Unit
) {
    var showGenreBottomSheet by remember { mutableStateOf(false) }
    var showMoodBottomSheet by remember { mutableStateOf(false) }

    var selectedGenre: Genre? by remember { mutableStateOf(genres.find { uiState.genreId == it.id }) }
    var selectedMood: Mood? by remember { mutableStateOf(moods.find { uiState.moodId == it.id }) }

    LaunchedEffect(selectedGenre, selectedMood) {
        onUpdateSearch(uiState.query, selectedGenre?.id, selectedMood?.id)
    }

    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SearchBar(
            query = uiState.query,
            placeholder = stringResource(R.string.home_search_placeholder),
            onClick = onClickSearchBar
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DropDownButton(
                label = "${stringResource(R.string.Genre)}${selectedGenre?.let { ": " + it.name } ?: ""}",
                onClick = { showGenreBottomSheet = true }
            )

            DropDownButton(
                label = "${stringResource(R.string.Mood)}${selectedMood?.let { ": " + it.name } ?: ""}",
                onClick = { showMoodBottomSheet = true }
            )
        }
    }


    if (showGenreBottomSheet) {
        GenreModalBottomSheet(
            onDismissRequest = { showGenreBottomSheet = false },
            genreItems = genres,
            onItemSelected = { selectedGenre = it }
        )
    }

    if (showMoodBottomSheet) {
        MoodModalBottomSheet(
            onDismissRequest = { showMoodBottomSheet = false },
            moodItems = moods,
            onItemSelected = { selectedMood = it }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SearchAppBarPreview() {
    NcsTheme(darkTheme = true) {
        SearchAppBar(scrollBehavior = null) {
            SearchBox(
                uiState = SearchUiState(),
                genres = emptyList(),
                moods = emptyList(),
                onUpdateSearch = { _, _, _ -> },
                onClickSearchBar = {}
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SearchAppBarQueryPreview() {
    NcsTheme(darkTheme = true) {
        SearchAppBar(scrollBehavior = null) {
            SearchBox(
                uiState = SearchUiState(query = "Alan Walker"),
                genres = emptyList(),
                moods = emptyList(),
                onUpdateSearch = { _, _, _ -> },
                onClickSearchBar = {}
            )
        }
    }
}