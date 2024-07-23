package com.ccc.ncs.feature.play

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.util.calculateScreenHeight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlayerMenuBottomSheet(
    modifier: Modifier = Modifier,
    draggableStatePercentage: Float,
    playlist: PlayList,
    currentMusic: Music?,
    lyrics: String?,
    onMusicOrderChanged: (Int, Int) -> Unit,
    onDeleteMusicInList: (Music) -> Unit
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    var sheetDragHandleHeight by remember { mutableStateOf(0.dp) }

    val menuSheetState = rememberPlayerMenuSheetState(
        sheetState = rememberBottomSheetScaffoldState(),
        sheetDragHandleHeight = sheetDragHandleHeight,
        screenHeight = calculateScreenHeight()
    )

    val tabPagerState = rememberPagerState(
        initialPage = PlayerMenuTabs.entries.first().index,
        pageCount = PlayerMenuTabs.entries::count
    )

    var selectedTabIndex by remember { mutableIntStateOf(PlayerMenuTabs.entries.first().index) }

    BackHandler(enabled = menuSheetState.sheetState.bottomSheetState.targetValue == SheetValue.Expanded) {
        scope.launch {
            menuSheetState.sheetState.bottomSheetState.partialExpand()
        }
    }

    LaunchedEffect(selectedTabIndex) {
        scope.launch {
            if (PlayerMenuTabs.entries.any { it.index == selectedTabIndex }) {
                tabPagerState.animateScrollToPage(selectedTabIndex)
            }
        }
    }

    LaunchedEffect(tabPagerState.targetPage) {
        selectedTabIndex = tabPagerState.targetPage
    }

    BottomSheetScaffold(
        scaffoldState = menuSheetState.sheetState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .heightIn(0.dp, menuSheetState.bodyHeight)
                    .fillMaxSize()
            ) {
                HorizontalPager(
                    state = tabPagerState,
                    flingBehavior = PagerDefaults.flingBehavior(
                        state = tabPagerState,
                        snapPositionalThreshold = 0.3f
                    )
                ) { index ->
                    Box(modifier = Modifier.alpha(menuSheetState.offsetProgress)) {
                        when (index) {
                            PlayerMenuTabs.PLAYLIST.index -> PlayerMenuPlaylistTabView(
                                playlist = playlist,
                                currentMusic = currentMusic,
                                onMusicOrderChanged = onMusicOrderChanged,
                                onDelete = onDeleteMusicInList
                            )
                            PlayerMenuTabs.LYRICS.index -> PlayerMenuLyricsTabView(
                                lyrics = lyrics,
                                title = currentMusic?.title,
                            )
                        }
                    }
                }
            }
        },
        sheetPeekHeight = (menuSheetState.sheetPeekHeight) * draggableStatePercentage,
        sheetDragHandle = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = draggableStatePercentage),
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = menuSheetState.offsetProgress)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged {
                        sheetDragHandleHeight = with(density) { it.height.toDp() }
                    }
            ) {
                PlayerMenuTabs.entries.forEach { tab ->
                    Tab(
                        selected = selectedTabIndex == tab.index,
                        onClick = {
                            scope.launch {
                                selectedTabIndex = tab.index
                                menuSheetState.sheetState.bottomSheetState.expand()
                            }
                        },
                    ) {
                        Row {
                            Text(
                                text = tab.label,
                                style = NcsTypography.PlayerTypography.bottomMenuText.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = draggableStatePercentage),
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .weight(1f)
                            )
                        }
                    }
                }
            }
        },
        sheetShadowElevation = 0.dp,
        sheetTonalElevation = 0.dp,
        sheetContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = draggableStatePercentage),
        content = {}
    )
}

enum class PlayerMenuTabs(
    val index: Int,
    val label: String
) {
    PLAYLIST(0, "Playlist"),
    LYRICS(1, "Lyrics")
}

@OptIn(ExperimentalMaterial3Api::class)
class PlayerMenuBottomSheetState(
    val sheetState: BottomSheetScaffoldState,
    val sheetDragHandleHeight: Dp,
    val windowInsetPaddings: PaddingValues,
    val screenHeight: Dp,
    val density: Density,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    val sheetPeekHeight: Dp get() = sheetDragHandleHeight + windowInsetPaddings.calculateBottomPadding()
    val bodyHeight: Dp get() = screenHeight - windowInsetPaddings.calculateTopPadding() - sheetDragHandleHeight
    val bottomSheetStateMinOffset: Dp get() = windowInsetPaddings.calculateTopPadding()
    val bottomSheetStateMaxOffset: Dp get() = screenHeight - sheetPeekHeight

    private val minOffsetPx = with(density) { bottomSheetStateMinOffset.toPx() }
    private val maxOffsetPx = with(density) { bottomSheetStateMaxOffset.toPx() }
    private val offsetRangePx = (maxOffsetPx - minOffsetPx).takeIf { it > 0 } ?: 1f

    var offsetProgress by mutableFloatStateOf(0f)
        private set

    init {
        observeOffsetChanges(coroutineScope)
    }

    private fun observeOffsetChanges(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            snapshotFlow {
                try {
                    sheetState.bottomSheetState.requireOffset()
                } catch (e: Throwable) {
                    0f
                }
            }.collectLatest { offset ->
                updateOffsetProgress(offset)
            }
        }
    }

    private fun updateOffsetProgress(offset: Float) {
        offsetProgress = 1 - ((offset - minOffsetPx) / offsetRangePx).coerceIn(0f, 1f)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberPlayerMenuSheetState(
    sheetState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    sheetDragHandleHeight: Dp,
    screenHeight: Dp,
): PlayerMenuBottomSheetState {
    val windowInsetPaddings = WindowInsets.safeDrawing.asPaddingValues()
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    return remember(sheetState, sheetDragHandleHeight, screenHeight) {
        PlayerMenuBottomSheetState(
            sheetState = sheetState,
            sheetDragHandleHeight = sheetDragHandleHeight,
            windowInsetPaddings = windowInsetPaddings,
            screenHeight = screenHeight,
            density = density,
            coroutineScope = coroutineScope
        )
    }
}