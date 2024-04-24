package com.ccc.ncs.feature.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.feature.play.PlayerViewModel
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.ui.component.GenreModalBottomSheet
import com.ccc.ncs.ui.component.MoodModalBottomSheet
import com.ccc.ncs.ui.component.TestMusicCard
import java.util.UUID

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen(
        modifier = modifier,
        testMusics = viewModel.musics.collectAsLazyPagingItems()
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    testMusics: LazyPagingItems<Music>,
    playerViewModel: PlayerViewModel = hiltViewModel(),
    viewModel: HomeViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val genres by viewModel.getAllGenres().collectAsStateWithLifecycle(emptyList())
    val moods by viewModel.getAllMoods().collectAsStateWithLifecycle(emptyList())

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Box {
        Column {
            SearchAppBar(scrollBehavior = scrollBehavior) {
                SearchBox(
                    uiState = uiState.searchUiState,
                    genres = genres,
                    moods = moods,
                    onUpdateSearch = viewModel::searchMusic
                )
            }

            if (uiState.isSelectMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
                        playerViewModel.setPlayList(
                            PlayList(
                                id = UUID.randomUUID(),
                                name = "PlayList",
                                musics = uiState.selectedMusics
                            )
                        )
                    }) {
                        Icon(imageVector = NcsIcons.Play, contentDescription = null)
                    }

                    IconButton(onClick = { viewModel.updateSelectMode(false) }) {
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
                                selected = uiState.selectedMusics.contains(music),
                                modifier = Modifier
                                    .combinedClickable(
                                        onClick = {
                                            viewModel.updateSelectMusic(music)
                                        },
                                        onLongClick = {
                                            viewModel.updateSelectMode(true)
                                            viewModel.updateSelectMusic(music)
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
    containerHeight: Dp = 90.dp,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
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

    Box(
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
    onUpdateSearch: (query: String?, genreId: Int?, moodId: Int?) -> Unit
) {
    var query by remember { mutableStateOf(uiState.query ?: "") }

    var showGenreBottomSheet by remember { mutableStateOf(false) }
    var showMoodBottomSheet by remember { mutableStateOf(false) }

    var searchedQuery: String? by remember { mutableStateOf(null) }
    var selectedGenre: Genre? by remember { mutableStateOf(genres.find { uiState.genreId == it.id }) }
    var selectedMood: Mood? by remember { mutableStateOf(moods.find { uiState.moodId == it.id }) }

    LaunchedEffect(searchedQuery, selectedGenre, selectedMood) {
        onUpdateSearch(searchedQuery, selectedGenre?.id, selectedMood?.id)
    }

    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = searchedQuery ?: "",
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface
                )
                .padding(
                    horizontal = 8.dp,
                    vertical = 4.dp
                )
                .fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { showGenreBottomSheet = true },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Text(text = "Genre${selectedGenre?.let { ": " + it.name } ?: ""}")
            }

            OutlinedButton(
                onClick = { showMoodBottomSheet = true },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Text(text = "Mood${selectedMood?.let { ": " + it.name } ?: ""}")
            }
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