package com.ccc.ncs.feature.play

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.util.calculateScreenHeight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerMenuBottomSheet(
    modifier: Modifier = Modifier,
    playerScreenVisibleProgress: Float,
    playlist: PlayList,
    currentMusic: Music?,
    lyrics: String?,
    onMusicOrderChanged: (Int, Int) -> Unit,
    onClickMusic: (Int) -> Unit = {},
    onDeleteMusicInList: (Music) -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetDragHandleHeight by remember { mutableStateOf(50.dp) }

    val menuSheetState = rememberPlayerMenuSheetState(
        sheetState = rememberBottomSheetScaffoldState(),
        sheetDragHandleHeight = sheetDragHandleHeight,
        screenHeight = calculateScreenHeight()
    )

    val tabPagerState = rememberPagerState(
        initialPage = PlayerMenuTabs.entries.first().index,
        pageCount = PlayerMenuTabs.entries::count
    )

    BackHandler(enabled = menuSheetState.sheetState.bottomSheetState.targetValue == SheetValue.Expanded) {
        scope.launch {
            menuSheetState.sheetState.bottomSheetState.partialExpand()
        }
    }

    BottomSheetScaffold(
        scaffoldState = menuSheetState.sheetState,
        sheetShape = RectangleShape,
        sheetContent = {
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
                            onClick = onClickMusic,
                            onDelete = onDeleteMusicInList
                        )
                        PlayerMenuTabs.LYRICS.index -> PlayerMenuLyricsTabView(
                            lyrics = lyrics,
                            title = currentMusic?.title,
                        )
                    }
                }
            }
        },
        sheetPeekHeight = (menuSheetState.sheetPeekHeight) * playerScreenVisibleProgress,
        sheetDragHandle = {
            PlayerMenuTabRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = menuSheetState.windowInsetPaddings.calculateTopPadding() * menuSheetState.offsetProgress),
                selectedIndex = tabPagerState.targetPage,
                indicatorVisibleProgress = menuSheetState.offsetProgress
            ) {
                PlayerMenuTabs.entries.forEach { tab ->
                    tab.Tab(
                        selectedIndex = tabPagerState.targetPage,
                        onClick = {
                            scope.launch {
                                tabPagerState.scrollToPage(tab.index)
                                menuSheetState.sheetState.bottomSheetState.expand()
                            }
                        }, textVisibleProgress = playerScreenVisibleProgress
                    )
                }
            }
        },
        sheetShadowElevation = 0.dp,
        sheetTonalElevation = 0.dp,
        sheetContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = playerScreenVisibleProgress),
        content = {}
    )
}

enum class PlayerMenuTabs(
    val index: Int,
    val labelRes: Int
) {
    PLAYLIST(0, R.string.player_menu_playlist),
    LYRICS(1, R.string.Lyrics);

    @Composable
    fun Tab(
        selectedIndex: Int,
        onClick: (PlayerMenuTabs) -> Unit,
        textVisibleProgress: Float
    ) {
        Tab(
            selected = selectedIndex == index,
            onClick = { onClick(this) }
        ) {
            Text(
                text = stringResource(labelRes),
                style = NcsTypography.Player.bottomMenuText.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = textVisibleProgress),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            )
        }
    }
}

@Composable
fun PlayerMenuTabRow(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    indicatorVisibleProgress: Float,
    tabs: @Composable () -> Unit
) {
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        divider = {},
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                color = MaterialTheme.colorScheme.primary.copy(alpha = indicatorVisibleProgress)
            )
        },
        modifier = modifier,
        tabs = tabs
    )
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
                } catch (e: IllegalStateException) {
                    0f
                }
            }.collectLatest { offset ->
                updateOffsetProgress(offset.coerceIn(minOffsetPx, maxOffsetPx))
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