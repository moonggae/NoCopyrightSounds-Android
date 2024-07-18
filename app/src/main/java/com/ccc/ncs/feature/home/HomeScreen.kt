package com.ccc.ncs.feature.home

import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.CommonModalBottomSheet
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.feature.home.addmusictoplaylistdialog.AddMusicsToPlaylistDialog
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import com.ccc.ncs.ui.component.BottomSheetMenuItem
import com.ccc.ncs.ui.component.ClickableSearchBar
import com.ccc.ncs.ui.component.DropDownButton
import com.ccc.ncs.ui.component.MusicCardList
import com.ccc.ncs.ui.component.MusicTagBottomSheet
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onUpdateSearchQuery: (String?) -> Unit,
    onClickSearchBar: (String?) -> Unit,
    onPlayMusics: (List<Music>) -> Unit,
    onAddToQueue: (List<Music>) -> Unit,
    navigateToMusicDetail: (Music) -> Unit,
) {
    val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        modifier = modifier,
        musics = viewModel.musics.collectAsLazyPagingItems(),
        homeUiState = homeUiState,
        onShowSnackbar = onShowSnackbar,
        updateSearchQuery = onUpdateSearchQuery,
        updateSearchGenre = viewModel::onSearchGenreChanged,
        updateSearchMood = viewModel::onSearchMoodChanged,
        updateSelectMode = viewModel::updateSelectMode,
        updateSelectMusic = viewModel::updateSelectMusic,
        onClickSearchBar = onClickSearchBar,
        onPlayMusics = onPlayMusics,
        onAddToQueue = onAddToQueue,
        onClickMusic = {
            viewModel.insertMusicToLocal(it)
            navigateToMusicDetail(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    musics: LazyPagingItems<Music>,
    homeUiState: HomeUiState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    updateSearchQuery: (query: String?) -> Unit,
    updateSearchGenre: (Genre?) -> Unit,
    updateSearchMood: (Mood?) -> Unit,
    updateSelectMode: (Boolean) -> Unit,
    updateSelectMusic: (Music) -> Unit,
    onClickSearchBar: (String?) -> Unit,
    onPlayMusics: (List<Music>) -> Unit,
    onAddToQueue: (List<Music>) -> Unit,
    onClickMusic: (Music) -> Unit
) {
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var appbarHeight by remember { mutableStateOf(120.dp) }
    var showSelectMusicMenu by remember { mutableStateOf(false) }
    var showAddMusicsToPlaylistDialog by remember { mutableStateOf(false) }

    val addedToQueueMessage = stringResource(R.string.message_added_to_queue)
    val addedToPlaylistMessage = stringResource(R.string.message_added_to_playlist)

    LaunchedEffect(musics.loadState.refresh) {
        if (musics.loadState.refresh is LoadState.Loading) {
            listState.scrollToItem(0)
        }
    }

    Box {
        Column {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

            if (homeUiState.isSelectMode) {
                SelectMusicAppBar(
                    onClickMenu = { showSelectMusicMenu = true },
                    onClickClose = { updateSelectMode(false) }
                )
            } else {
                SearchAppBar(
                    containerHeight = appbarHeight,
                    scrollBehavior = scrollBehavior
                ) {
                    MusicSearchBox(
                        uiState = homeUiState.searchUiState,
                        genres = homeUiState.genres,
                        moods = homeUiState.moods,
                        updateSearchQuery = updateSearchQuery,
                        updateSearchGenre = updateSearchGenre,
                        updateSearchMood = updateSearchMood,
                        onClickSearchBar = onClickSearchBar,
                        onMenuLineCountChanged = {
                            when (it) {
                                1 -> appbarHeight = 120.dp
                                2 -> appbarHeight = 168.dp
                            }
                        }
                    )
                }
            }

            MusicCardList(
                musicItems = musics,
                selectedMusics = homeUiState.selectedMusics,
                nestedScrollConnection = scrollBehavior.nestedScrollConnection,
                updateSelectMusic = updateSelectMusic,
                updateSelectMode = updateSelectMode,
                onClickMore = {
                    updateSelectMode(true)
                    updateSelectMusic(it)
                    showSelectMusicMenu = true
                },
                state = listState,
                onClick = onClickMusic,
                isSelectMode = homeUiState.isSelectMode
            )
        }
    }

    SelectMusicMenuBottomSheet(
        show = showSelectMusicMenu,
        onDismissRequest = { showSelectMusicMenu = false },
        onClickPlayNow = {
            onPlayMusics(homeUiState.selectedMusics)
            showSelectMusicMenu = false
            updateSelectMode(false)
        },
        onClickAddToPlayList = { showAddMusicsToPlaylistDialog = true },
        onClickAddToQueue = {
            onAddToQueue(homeUiState.selectedMusics)
            showSelectMusicMenu = false
            updateSelectMode(false)
            scope.launch { onShowSnackbar(addedToQueueMessage, null) }
        }
    )

    AddMusicsToPlaylistDialog(
        show = showAddMusicsToPlaylistDialog,
        onDismissRequest = { showAddMusicsToPlaylistDialog = false },
        musics = homeUiState.selectedMusics,
        onFinish = {
            showAddMusicsToPlaylistDialog = false
            showSelectMusicMenu = false
            updateSelectMode(false)
            scope.launch { onShowSnackbar(addedToPlaylistMessage, null) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMusicMenuBottomSheet(
    modifier: Modifier = Modifier,
    show: Boolean,
    onDismissRequest: () -> Unit,
    onClickPlayNow: () -> Unit,
    onClickAddToPlayList: () -> Unit,
    onClickAddToQueue: () -> Unit
) {
    if (show) {
        CommonModalBottomSheet(onDismissRequest = onDismissRequest) {
            SelectMusicMenuBottomSheetContent(
                onClickPlayNow = onClickPlayNow,
                onClickAddToPlayList = onClickAddToPlayList,
                onClickAddToQueue = onClickAddToQueue
            )
        }
    }
}

@Composable
fun SelectMusicMenuBottomSheetContent(
    modifier: Modifier = Modifier,
    onClickPlayNow: () -> Unit,
    onClickAddToPlayList: () -> Unit,
    onClickAddToQueue: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        BottomSheetMenuItem(icon = NcsIcons.PlayCircle, label = stringResource(R.string.home_select_musics_menu_play_now), onClick = onClickPlayNow)
        BottomSheetMenuItem(
            icon = NcsIcons.BookmarkAdd,
            label = stringResource(R.string.home_select_musics_menu_add_to_playlist),
            onClick = onClickAddToPlayList
        )
        BottomSheetMenuItem(
            icon = NcsIcons.PlaylistAdd,
            label = stringResource(R.string.home_select_musics_menu_add_to_queue),
            onClick = onClickAddToQueue
        )
    }
}

@Composable
fun SelectMusicAppBar(
    modifier: Modifier = Modifier,
    onClickMenu: () -> Unit,
    onClickClose: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onClickMenu) {
            Icon(imageVector = NcsIcons.MoreVertical, contentDescription = null)
        }

        IconButton(onClick = onClickClose) {
            Icon(imageVector = NcsIcons.Close, contentDescription = null)
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
fun MusicSearchBox(
    uiState: SearchUiState,
    genres: List<Genre>,
    moods: List<Mood>,
    onClickSearchBar: (String?) -> Unit,
    updateSearchQuery: (query: String?) -> Unit,
    updateSearchGenre: (Genre?) -> Unit,
    updateSearchMood: (Mood?) -> Unit,
    onMenuLineCountChanged: (Int) -> Unit = {}
) {
    var showGenreBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showMoodBottomSheet by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(12.dp)
    ) {
        ClickableSearchBar(
            query = uiState.query,
            placeholder = stringResource(R.string.home_search_placeholder),
            onClick = { onClickSearchBar(uiState.query) },
            onClickDelete = { updateSearchQuery(null) }
        )

        CustomFlowRow(
            onLineCountChanged = onMenuLineCountChanged
        ) {
            DropDownButton(
                label = "${stringResource(R.string.Genre)}${uiState.genre?.let { ": " + it.name } ?: ""}",
                onClick = { showGenreBottomSheet = true }
            )

            DropDownButton(
                label = "${stringResource(R.string.Mood)}${uiState.mood?.let { ": " + it.name } ?: ""}",
                onClick = { showMoodBottomSheet = true }
            )
        }
    }


    if (showGenreBottomSheet) {
        MusicTagBottomSheet(
            onDismissRequest = { showGenreBottomSheet = false },
            items = genres,
            onItemSelected = updateSearchGenre
        )
    }

    if (showMoodBottomSheet) {
        MusicTagBottomSheet(
            onDismissRequest = { showMoodBottomSheet = false },
            items = moods,
            onItemSelected = updateSearchMood
        )
    }
}

@Composable
fun CustomFlowRow(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 8.dp,
    verticalSpacing: Dp = 8.dp,
    onLineCountChanged: (Int) -> Unit = {},
    content: @Composable () -> Unit
) {
    var lineCount: Int by remember { mutableIntStateOf(0) }

    Layout(
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            var xPosition = 0
            var yPosition = 0
            var maxHeightInLine = 0
            var line = 1

            placeables.forEach { placeable ->
                if (xPosition + placeable.width > constraints.maxWidth) {
                    xPosition = 0
                    yPosition += maxHeightInLine
//                    yPosition += maxHeightInLine + verticalSpacing.roundToPx()
                    maxHeightInLine = 0
                    line += 1
                }

                placeable.placeRelative(x = xPosition, y = yPosition)

                xPosition += placeable.width + horizontalSpacing.roundToPx()
                maxHeightInLine = maxOf(maxHeightInLine, placeable.height)
            }

            if (lineCount != line) {
                lineCount = line
                onLineCountChanged(lineCount)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SearchAppBarPreview() {
    NcsTheme(darkTheme = true) {
        SearchAppBar(scrollBehavior = null) {
            MusicSearchBox(
                uiState = SearchUiState(),
                genres = emptyList(),
                moods = emptyList(),
                onClickSearchBar = {},
                updateSearchGenre = {},
                updateSearchMood = {},
                updateSearchQuery = {}
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
            MusicSearchBox(
                uiState = SearchUiState(
                    query = "Alan Walker",
                    genre = Genre(
                        id = 1,
                        name = "Long name genre",
                        colorInt = null,
                        backgroundColorInt = null
                    ),
                    mood = Mood(
                        id = 1,
                        name = "Long name Mood",
                        colorInt = null,
                        backgroundColorInt = null
                    )
                ),
                genres = emptyList(),
                moods = emptyList(),
                onClickSearchBar = {},
                updateSearchGenre = {},
                updateSearchMood = {},
                updateSearchQuery = {}
            )
        }
    }
}

@Preview
@Composable
fun SelectMusicMenuBottomSheetContentPreview(modifier: Modifier = Modifier) {
    NcsTheme(darkTheme = true) {
        SelectMusicMenuBottomSheetContent(
            onClickPlayNow = {},
            onClickAddToPlayList = {},
            onClickAddToQueue = {}
        )
    }
}