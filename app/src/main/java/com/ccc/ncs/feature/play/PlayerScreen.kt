package com.ccc.ncs.feature.play

import android.view.MotionEvent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.ListItemCardDefaults
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.domain.model.PlaybackState
import com.ccc.ncs.domain.model.PlayingStatus
import com.ccc.ncs.domain.model.RepeatMode
import com.ccc.ncs.feature.music.ArtistList
import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.artistText
import com.ccc.ncs.model.util.toTimestampMMSS
import com.ccc.ncs.ui.component.PlayerControllerType
import com.ccc.ncs.ui.component.PlayerPlayingButton
import com.ccc.ncs.ui.component.PlayerRepeatButton
import com.ccc.ncs.ui.component.PlayerShuffleButton
import com.ccc.ncs.ui.component.PlayerSkipNextButton
import com.ccc.ncs.ui.component.PlayerSkipPreviousButton
import com.ccc.ncs.ui.component.mockMusics
import com.ccc.ncs.util.calculateScreenHeight
import com.ccc.ncs.util.conditional
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    minHeight: Dp,
    onUpdateScreenSize: (percentage: Float) -> Unit,
    playerUiState: PlayerUiState.Success,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    onSeekTo: (position: Long) -> Unit,
    onShuffle: (Boolean) -> Unit,
    onChangeRepeatMode: (RepeatMode) -> Unit,
    onClose: () -> Unit,
    onUpdateMusicOrder: (Int, Int) -> Unit,
    onClickOnList: (Int) -> Unit,
    onMoveToMusicDetail: (Music) -> Unit,
    onMoveToArtistDetail: (Artist) -> Unit,
    onDeleteMusicInPlaylist: (Music) -> Unit,
    shouldExpandScreen: Boolean = false,
    onExpandComplete: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    val maxHeight = calculateScreenHeight()
    val draggableState = rememberDraggableState(
        maxHeight = maxHeight,
        minHeight = minHeight
    )

    BackHandler(enabled = draggableState.currentValue == SwipeAnchors.Big) {
        scope.launch {
            draggableState.animateTo(SwipeAnchors.Small)
        }
    }

    LaunchedEffect(draggableState.percentage) {
        onUpdateScreenSize(draggableState.percentage)
    }

    LaunchedEffect(shouldExpandScreen) {
        if (shouldExpandScreen) {
            scope.launch {
                draggableState.animateTo(SwipeAnchors.Big)
                onExpandComplete()
            }
        }
    }

    Row(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(draggableState.requireOffset().dp)
                .anchoredDraggable(
                    state = draggableState,
                    orientation = Orientation.Vertical,
                    reverseDirection = true
                )
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .conditional(draggableState.currentValue == SwipeAnchors.Small) {
                    clickable(onClick = {
                        scope.launch {
                            draggableState.animateTo(SwipeAnchors.Big)
                        }
                    })
                }
        ),
    ) {
        playerUiState.currentMusic?.let { music ->
            Column {
                Row(Modifier.weight(1f)) {
                    PlayerScreenBigContent(
                        music = music,
                        draggableStatePercentage = draggableState.percentage,
                        playbackState = playerUiState.playbackState,
                        onSeekTo = onSeekTo,
                        onPlay = onPlay,
                        onPause = onPause,
                        onSkipPrevious = onSkipPrevious,
                        onSkipNext = onSkipNext,
                        onShuffle = onShuffle,
                        onChangeRepeatMode = onChangeRepeatMode,
                        onClose = onClose,
                        onClickMusicTitle = onMoveToMusicDetail,
                        onClickArtist = onMoveToArtistDetail,
                        modifier = Modifier
                    )

                    if (draggableState.currentValue != SwipeAnchors.Big) {
                        PlayerScreenSmallInformation(
                            title = music.title,
                            artist = music.artistText,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .fillMaxHeight()
                                .weight(1f)
                        )

                        PlayerScreenSmallController(
                            playingStatus = playerUiState.playbackState.playingStatus,
                            hasNext = playerUiState.playbackState.hasNext,
                            onPlay = onPlay,
                            onPause = onPause,
                            onSkipPrevious = onSkipPrevious,
                            onSkipNext = onSkipNext,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(horizontal = 12.dp)
                        )
                    }
                }

                LinearProgressIndicator(
                    progress = {
                        if (playerUiState.playbackState.duration == 0L) 0f
                        else (playerUiState.playbackState.position / playerUiState.playbackState.duration.toFloat())
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    trackColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    gapSize = 0.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .alpha(1 - draggableState.percentage)
                )
            }
        }
    }

    if (draggableState.targetValue == SwipeAnchors.Big) {
        PlayerMenuBottomSheet(
            playerScreenVisibleProgress = draggableState.percentage,
            playlist = playerUiState.playlist,
            currentMusic = playerUiState.currentMusic,
            lyrics = playerUiState.lyrics,
            onMusicOrderChanged = onUpdateMusicOrder,
            onClickMusic = onClickOnList,
            onDeleteMusicInList = onDeleteMusicInPlaylist
        )
    }
}


@Composable
private fun PlayerScreenBigContent(
    modifier: Modifier = Modifier,
    draggableStatePercentage: Float,
    music: Music,
    playbackState: PlaybackState,
    onSeekTo: (position: Long) -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    onShuffle: (Boolean) -> Unit,
    onChangeRepeatMode: (RepeatMode) -> Unit,
    onClose: () -> Unit,
    onClickMusicTitle: (Music) -> Unit,
    onClickArtist: (Artist) -> Unit
) {
    val localConfiguration = LocalConfiguration.current
    val screenWidth = remember { localConfiguration.screenWidthDp.dp }

    Column {
        Box {
            PlayerScreenCoverImage(
                url = music.coverUrl,
                progress = draggableStatePercentage,
                modifier = Modifier.align(Alignment.Center)
            )

            if (draggableStatePercentage > .2f) {
                PlayerScreenAppBar(
                    modifier = modifier,
                    draggableStatePercentage = draggableStatePercentage,
                    screenWidth = screenWidth,
                    onClose = onClose
                )
            }
        }

        Column(
            modifier = Modifier
                .widthIn(0.dp, screenWidth)
                .width(screenWidth * draggableStatePercentage * 2),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = music.title,
                style = NcsTypography.Music.Title.large.copy(
                    color = ListItemCardDefaults.listItemCardColors().labelColor,
                ),
                modifier = Modifier
                    .basicMarquee()
                    .conditional(draggableStatePercentage == 1f) {
                        clickable { onClickMusicTitle(music) }
                    }
                    .padding(vertical = 8.dp)
            )

            ArtistList(
                modifier = Modifier.basicMarquee(),
                artists = music.artists,
                onClick = if (draggableStatePercentage == 1f) onClickArtist else null
            )

            PlayerPositionProgressBar(
                duration = playbackState.duration,
                position = playbackState.position,
                onSeekTo = onSeekTo,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 32.dp
                )
            )

            PlayerScreenBigController(
                playingStatus = playbackState.playingStatus,
                hasNext = playbackState.hasNext,
                repeatMode = playbackState.repeatMode,
                isOnShuffle = playbackState.isShuffleOn,
                onPlay = onPlay,
                onPause = onPause,
                onSkipPrevious = onSkipPrevious,
                onSkipNext = onSkipNext,
                onShuffle = onShuffle,
                onChangeRepeatMode = onChangeRepeatMode,
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 20.dp
                )
            )
        }
    }
}

@Composable
private fun PlayerScreenAppBar(
    modifier: Modifier,
    draggableStatePercentage: Float,
    screenWidth: Dp,
    onClose: () -> Unit
) {
    Column(modifier.heightIn(0.dp, (100.dp * draggableStatePercentage))) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        Row(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp,
                    top = 12.dp
                )
                .widthIn(0.dp, screenWidth)
                .width(screenWidth * draggableStatePercentage * 2),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = NcsIcons.Close,
                contentDescription = stringResource(R.string.cd_close),
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f))
                    .clickable(onClick = onClose)
                    .padding(12.dp)
                    .size(28.dp)
            )


            /*
            Icon(
                imageVector = NcsIcons.MoreVertical,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = {})
            )
             */
        }
    }
}


@Composable
fun PlayerScreenBigController(
    modifier: Modifier = Modifier,
    playingStatus: PlayingStatus,
    isOnShuffle: Boolean,
    repeatMode: RepeatMode,
    hasNext: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    onShuffle: (Boolean) -> Unit,
    onChangeRepeatMode: (RepeatMode) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        PlayerShuffleButton(
            isOnShuffle = isOnShuffle,
            onClick = { onShuffle(!isOnShuffle) }
        )
        
        PlayerSkipPreviousButton(
            onClick = onSkipPrevious
        )
        
        PlayerPlayingButton(
            playingStatus = playingStatus,
            onPlay = onPlay,
            onPause = onPause
        )

        PlayerSkipNextButton(
            hasNext = hasNext,
            onClick = onSkipNext
        )

        PlayerRepeatButton(
            repeatMode = repeatMode,
            onClick = { onChangeRepeatMode(repeatMode.next()) }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlayerPositionProgressBar(
    modifier: Modifier = Modifier,
    duration: Long,
    position: Long,
    onSeekTo: (position: Long) -> Unit
) {
    var isUserDrag by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(position) {
        if (!isUserDrag) {
            currentPosition = position.toFloat()
        }
    }

    val interactionSource = remember { MutableInteractionSource() }
    val colors = SliderDefaults.colors(
        activeTrackColor = MaterialTheme.colorScheme.onSurface,
        thumbColor = MaterialTheme.colorScheme.onSurface,
    )

    val thumbSize = 16.dp

    Column(modifier) {
        Slider(
            value = currentPosition,
            onValueChange = { value ->
                if (isUserDrag) {
                    currentPosition = value
                }
            },
            valueRange = 0f..duration.coerceAtLeast(0).toFloat(),
            onValueChangeFinished = {
                if (isUserDrag) {
                    onSeekTo(currentPosition.toLong())
                    isUserDrag = false
                }
            },
            colors = colors,
            interactionSource = interactionSource,
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = remember { MutableInteractionSource() },
                    colors = colors,
                    thumbSize = DpSize(thumbSize, thumbSize)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    colors = colors,
                    enabled = true,
                    drawStopIndicator = null,
                    thumbTrackGapSize = 0.dp,
                    modifier = Modifier.height(4.dp)
                )
            },
            modifier = Modifier
                .height(16.dp)
                .pointerInteropFilter { event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        isUserDrag = true
                    }
                    false
                }
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = position.toTimestampMMSS(),
                style = NcsTypography.Player.timestamp.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Text(
                text = duration.toTimestampMMSS(),
                style = NcsTypography.Player.timestamp.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun PlayerScreenCoverImage(
    modifier: Modifier = Modifier,
    url: String,
    progress: Float = 1f
) {
    val isExpanded = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED
    val surfaceColor = MaterialTheme.colorScheme.surfaceContainer

    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(url)
                .size(Size.ORIGINAL)
                .build(),
            placeholder = painterResource(R.drawable.ncs_cover),
            fallback = painterResource(R.drawable.ncs_cover)
        ),
        contentDescription = stringResource(R.string.cd_music_cover),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .drawWithCache {
                val gradient = Brush.verticalGradient(
                    colors = listOf(surfaceColor.copy(alpha = 0.1f), surfaceColor.copy(alpha = progress)),
                    startY = 0f,
                    endY = size.height
                )
                onDrawWithContent {
                    drawContent()
                    drawRect(gradient, blendMode = BlendMode.SrcAtop)
                }
            }
            .conditional(isExpanded) {
                widthIn(0.dp, 400.dp)
            }
            .aspectRatio(1f)
    )
}

@Composable
fun PlayerScreenSmallInformation(
    modifier: Modifier = Modifier,
    title: String,
    artist: String
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = ListItemCardDefaults.listItemCardStyle.medium().labelTextStyle.copy(
                color = ListItemCardDefaults.listItemCardColors().labelColor,
            ),
            modifier = Modifier.basicMarquee()
        )
        Text(
            text = artist,
            style = ListItemCardDefaults.listItemCardStyle.medium().descriptionTextStyle.copy(
                color = ListItemCardDefaults.listItemCardColors().descriptionColor,
            ),
            modifier = Modifier.basicMarquee()
        )
    }
}

@Composable
fun PlayerScreenSmallController(
    modifier: Modifier = Modifier,
    playingStatus: PlayingStatus,
    hasNext: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        PlayerSkipPreviousButton(
            type = PlayerControllerType.Small,
            onClick = onSkipPrevious
        )

        PlayerPlayingButton(
            type = PlayerControllerType.SmallCenter,
            playingStatus = playingStatus,
            onPlay = onPlay,
            onPause = onPause
        )

        PlayerSkipNextButton(
            type = PlayerControllerType.Small,
            hasNext = hasNext,
            onClick = onSkipNext
        )
    }
}

private enum class SwipeAnchors {
    Small, Big
}


@OptIn(ExperimentalFoundationApi::class)
private val AnchoredDraggableState<SwipeAnchors>.percentage: Float
    get() = (
            try {
                (this.requireOffset() - this.anchors.minAnchor()) / (this.anchors.maxAnchor() - this.anchors.minAnchor())
            } catch (th: Throwable) {
                0f
            }
            ).coerceIn(0f, 1f)


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun rememberDraggableState(maxHeight: Dp, minHeight: Dp): AnchoredDraggableState<SwipeAnchors> {
    // todo : navigation back 이후 초기화 되는 문제

    val density = LocalDensity.current

    val animationSpec = tween<Float>()
    val positionalThreadsHold = { distance: Float -> distance * 0.3f }
    val velocityThreshold = { with(density) { (minHeight * 1.3f).toPx() } }
    val decayAnimationSpec = exponentialDecay<Float>()

    return remember(maxHeight, minHeight) {
        AnchoredDraggableState(
            initialValue = SwipeAnchors.Small,
            positionalThreshold = positionalThreadsHold,
            velocityThreshold = velocityThreshold,
            snapAnimationSpec = animationSpec,
            decayAnimationSpec = decayAnimationSpec,
            anchors = DraggableAnchors {
                SwipeAnchors.Small at minHeight.value
                SwipeAnchors.Big at maxHeight.value
            }
        )
    }
}


@Preview
@Composable
fun PlayerScreenBigContentPreview(modifier: Modifier = Modifier) {
    NcsTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
        ) {
            PlayerScreenBigContent(
                modifier = modifier,
                draggableStatePercentage = 1f,
                music = mockMusics.first(),
                playbackState = PlaybackState(),
                onSeekTo = {},
                onPlay = {},
                onPause = {},
                onSkipPrevious = {},
                onSkipNext = {},
                onShuffle = {},
                onChangeRepeatMode = {},
                onClose = {},
                onClickMusicTitle = {},
                onClickArtist = { }
            )
        }
    }
}