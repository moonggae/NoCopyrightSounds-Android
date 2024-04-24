package com.ccc.ncs.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.G
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
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

    Box {
        LazyColumn(
            state = listState
        ) {
            item {
                Spacer(modifier = Modifier.height(140.dp))
            }
            items(count = testMusics.itemCount) { index ->
                testMusics[index]?.let { music ->
                    TestMusicCard(
                        item = music,
                        modifier = Modifier.clickable {
                            playerViewModel.setPlayList(
                                PlayList( // todo : play list logic
                                    id = UUID.randomUUID(),
                                    name = "PlayList of ${music.title}",
                                    musics = listOfNotNull(music, testMusics[index + 1])
                                )
                            )
                        }
                    )
                }
            }
        }


        AnimatedVisibility(
            visible = listState.isScrollingUp(),
            enter = slideInVertically(),
            exit = shrinkVertically()
        ) {

            SearchBox(
                uiState = uiState,
                genres = genres,
                moods = moods,
                onUpdateSearch = viewModel::searchMusic
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(
    uiState: HomeUiState,
    genres: List<Genre>,
    moods: List<Mood>,
    onUpdateSearch: (query: String?, genreId: Int?, moodId: Int?) -> Unit
) {
    var query by remember { mutableStateOf(uiState.query ?: "") }
    var isSearching by remember { mutableStateOf(false) }

    var showGenreBottomSheet by remember { mutableStateOf(false) }
    var showMoodBottomSheet by remember { mutableStateOf(false) }

    var searchedQuery: String? by remember { mutableStateOf(null) }
    var selectedGenre: Genre? by remember { mutableStateOf(genres.find { uiState.genreId == it.id }) }
    var selectedMood: Mood? by remember { mutableStateOf(moods.find { uiState.moodId == it.id }) }

    LaunchedEffect(searchedQuery, selectedGenre, selectedMood) {
        onUpdateSearch(searchedQuery, selectedGenre?.id, selectedMood?.id)
    }

    Card(
        modifier = Modifier.padding(
            vertical = 8.dp,
            horizontal = 12.dp
        ),
        colors = if (isSearching) {
            CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        } else {
            CardDefaults.cardColors()
        }


    ) {
        Column(
            modifier = Modifier.padding(
                vertical = 8.dp,
                horizontal = 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DockedSearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = {
                    searchedQuery = it
                    isSearching = false
                },
                active = isSearching,
                onActiveChange = { isSearching = it }) {}

            if (!isSearching) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { showGenreBottomSheet = true },
//                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = "Genre${selectedGenre?.let { ": " + it.name } ?: ""}")
                    }

                    OutlinedButton(
                        onClick = { showMoodBottomSheet = true },
//                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = "Mood${selectedMood?.let { ": " + it.name } ?: ""}")
                    }
                }
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

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}