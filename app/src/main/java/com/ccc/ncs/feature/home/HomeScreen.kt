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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
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
import com.ccc.ncs.analytics.TrackScreenViewEvent
import com.ccc.ncs.designsystem.component.CommonModalBottomSheet
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.feature.home.addmusictoplaylistdialog.AddMusicsToPlaylistDialog
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import com.ccc.ncs.ui.component.BottomSheetMenuItem
import com.ccc.ncs.ui.component.ClickableSearchBar
import com.ccc.ncs.ui.component.DropDownButton
import com.ccc.ncs.ui.component.LoadingScreen
import com.ccc.ncs.ui.component.MusicCardList
import com.ccc.ncs.ui.component.MusicTagBottomSheet
import com.ccc.ncs.util.Const
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(FlowPreview::class)
@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onUpdateSearchQuery: (String?) -> Unit,
    onClickSearchBar: (String?) -> Unit,
    onPlayMusics: (List<UUID>) -> Unit,
    onAddToQueue: (List<UUID>) -> Unit,
    navigateToMusicDetail: (UUID) -> Unit,
) {
    val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val failToDownloadMusicErrorMessage  = stringResource(R.string.error_message_fail_to_download_music)

    LaunchedEffect(Unit) {
        viewModel.errorEvents
            .debounce(500L)
            .collect { error ->
            if (error == Const.ERROR_DOWNLOAD_MUSIC) {
                onShowSnackbar(failToDownloadMusicErrorMessage, null)
            }
        }
    }

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
        onMoveToDetailPage = navigateToMusicDetail,
        onClickMusic = { onPlayMusics(listOf(it)) },
        downloadMusic = viewModel::downloadMusic
    )


    TrackScreenViewEvent("Home")
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
    updateSelectMusic: (UUID) -> Unit,
    onClickSearchBar: (String?) -> Unit,
    onPlayMusics: (List<UUID>) -> Unit,
    onAddToQueue: (List<UUID>) -> Unit,
    onClickMusic: (UUID) -> Unit,
    downloadMusic: (UUID) -> Unit,
    onMoveToDetailPage: (UUID) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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

    fun retryLoad() {
        scope.launch(Dispatchers.Main) { musics.retry() }
    }

    Box {
        Column {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

            if (homeUiState.isSelectMode) {
                SelectMusicAppBar(
                    onClickMenu = { showSelectMusicMenu = true },
                    onClickClose = { updateSelectMode(false) },
                    selectedMusicCount = homeUiState.selectedMusicIds.size
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

            if (musics.loadState.hasError) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(R.string.error_failed_to_load_musics))
                    Button(onClick = ::retryLoad) {
                        Text(
                            text = stringResource(R.string.retry),
                            style = NcsTypography.Label.contentLabel.copy(
                                color = MaterialTheme.colorScheme.surface
                            )
                        )
                    }
                }
            }

            MusicCardList(
                musicItems = musics,
                selectedMusicIds = homeUiState.selectedMusicIds,
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
                isSelectMode = homeUiState.isSelectMode,
            )
        }

        if (musics.loadState.refresh is LoadState.Loading) {
            LoadingScreen()
        }
    }

    SelectMusicMenuBottomSheet(
        show = showSelectMusicMenu,
        onDismissRequest = { showSelectMusicMenu = false },
        onClickPlayNow = {
            onPlayMusics(homeUiState.selectedMusicIds)
            showSelectMusicMenu = false
            updateSelectMode(false)
        },
        onClickAddToPlayList = { showAddMusicsToPlaylistDialog = true },
        onClickAddToQueue = {
            onAddToQueue(homeUiState.selectedMusicIds)
            showSelectMusicMenu = false
            updateSelectMode(false)
            scope.launch { onShowSnackbar(addedToQueueMessage, null) }
        },
        onClickDownload = {
            homeUiState.selectedMusicIds.forEach { music ->
                downloadMusic(music)
            }

            showSelectMusicMenu = false
            updateSelectMode(false)
        },
        onClickDetail = {
            if (homeUiState.selectedMusicIds.size != 1) {
                onMoveToDetailPage(homeUiState.selectedMusicIds.first())
            }

            showSelectMusicMenu = false
            updateSelectMode(false)
        }
    )

    if (showAddMusicsToPlaylistDialog) {
        AddMusicsToPlaylistDialog(
            onDismissRequest = { showAddMusicsToPlaylistDialog = false },
            musicIds = homeUiState.selectedMusicIds,
            onFinish = {
                showAddMusicsToPlaylistDialog = false
                showSelectMusicMenu = false
                updateSelectMode(false)
                scope.launch { onShowSnackbar(addedToPlaylistMessage, null) }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMusicMenuBottomSheet(
    modifier: Modifier = Modifier,
    show: Boolean,
    onDismissRequest: () -> Unit,
    onClickPlayNow: () -> Unit,
    onClickAddToPlayList: () -> Unit,
    onClickAddToQueue: () -> Unit,
    onClickDownload: () -> Unit,
    onClickDetail: (() -> Unit)?
) {
    if (show) {
        CommonModalBottomSheet(onDismissRequest = onDismissRequest) {
            SelectMusicMenuBottomSheetContent(
                onClickPlayNow = onClickPlayNow,
                onClickAddToPlayList = onClickAddToPlayList,
                onClickAddToQueue = onClickAddToQueue,
                onClickDownload = onClickDownload,
                onClickMoveToDetail = onClickDetail
            )
        }
    }
}

@Composable
fun SelectMusicMenuBottomSheetContent(
    modifier: Modifier = Modifier,
    onClickPlayNow: () -> Unit,
    onClickAddToPlayList: () -> Unit,
    onClickAddToQueue: () -> Unit,
    onClickDownload: () -> Unit,
    onClickMoveToDetail: (() -> Unit)?,
) {
    Column {
        BottomSheetMenuItem(
            icon = NcsIcons.PlayCircle,
            label = stringResource(R.string.home_select_musics_menu_play_now),
            onClick = onClickPlayNow,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        if (onClickMoveToDetail != null) {
            BottomSheetMenuItem(
                icon = NcsIcons.LibraryMusic,
                label = stringResource(R.string.home_select_musics_menu_details),
                onClick = onClickMoveToDetail,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        BottomSheetMenuItem(
            icon = NcsIcons.BookmarkAdd,
            label = stringResource(R.string.home_select_musics_menu_add_to_playlist),
            onClick = onClickAddToPlayList,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        BottomSheetMenuItem(
            icon = NcsIcons.PlaylistAdd,
            label = stringResource(R.string.home_select_musics_menu_add_to_queue),
            onClick = onClickAddToQueue,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        BottomSheetMenuItem(
            icon = NcsIcons.Download,
            label = stringResource(R.string.home_select_musics_menu_download),
            onClick = onClickDownload,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

@Composable
fun SelectMusicAppBar(
    modifier: Modifier = Modifier,
    onClickMenu: () -> Unit,
    onClickClose: () -> Unit,
    selectedMusicCount: Int,
    label: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = " $selectedMusicCount ",
            style = NcsTypography.Label.contentLabel.copy(
                color = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .padding(start = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .padding(6.dp)
        )

        if (label != null) {
            Text(
                text = label,
                style = NcsTypography.Label.appbarTitle.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Row {
            IconButton(onClick = onClickMenu) {
                Icon(imageVector = NcsIcons.MoreVertical, contentDescription = stringResource(R.string.cd_select_music_app_bar_menu))
            }

            IconButton(onClick = onClickClose) {
                Icon(imageVector = NcsIcons.Close, contentDescription = stringResource(R.string.cd_select_music_app_bar_close))
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
                        name = "Long name genre"
                    ),
                    mood = Mood(
                        id = 1,
                        name = "Long name Mood"
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
            onClickAddToQueue = {},
            onClickDownload = {},
            onClickMoveToDetail = {}
        )
    }
}