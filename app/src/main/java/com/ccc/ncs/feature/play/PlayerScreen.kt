package com.ccc.ncs.feature.play

import android.view.MotionEvent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.ListItemCardDefaults
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.ui.component.mockMusics
import com.ccc.ncs.util.calculateScreenHeight
import com.ccc.ncs.util.conditional
import com.ccc.ncs.util.toTimestampMMSS
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.reflect.KFunction2


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    minHeight: Dp = 60.dp,
    onUpdateScreenSize: (percentage: Float) -> Unit,
    playerUiState: PlayerUiState.Success,
    onPlay: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    onSeekTo: (position: Long) -> Unit,
    onShuffle: (Boolean) -> Unit,
    onRepeat: (Boolean) -> Unit,
    onClose: () -> Unit,
    onUpdateMusicOrder: KFunction2<Int, Int, Unit>
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

    val onClickSmallScreen: () -> Unit = {
        scope.launch {
            draggableState.animateTo(SwipeAnchors.Big)
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
                    clickable(onClick = onClickSmallScreen)
                }
        ),
    ) {
        playerUiState.currentMusic?.let { music ->
            Column {
                Row(Modifier.weight(1f)) {
                    PlayerScreenBigContent(
                        music = music,
                        draggableStatePercentage = draggableState.percentage,
                        uiState = playerUiState,
                        onSeekTo = onSeekTo,
                        onPlay = onPlay,
                        onSkipPrevious = onSkipPrevious,
                        onSkipNext = onSkipNext,
                        onShuffle = onShuffle,
                        onRepeat = onRepeat,
                        onClose = onClose,
                        modifier = Modifier
                    )

                    PlayerScreenSmallInformation(
                        title = music.title,
                        artist = music.artist,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .fillMaxHeight()
                            .weight(1f)
                    )

                    PlayerScreenSmallController(
                        isPlaying = playerUiState.isPlaying,
                        hasNext = playerUiState.hasNext,
                        onPlay = onPlay,
                        onSkipPrevious = onSkipPrevious,
                        onSkipNext = onSkipNext,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 12.dp)
                    )
                }

                LinearProgressIndicator(
                    progress = {
                        if (playerUiState.duration == 0L) 0f
                        else (playerUiState.position / playerUiState.duration.toFloat())
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    trackColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .alpha(1 - draggableState.percentage)
                )
            }
        }
    }

    PlayerMenuBottomSheet(
        draggableStatePercentage = draggableState.percentage,
        playlist = playerUiState.playlist,
        currentMusic = playerUiState.currentMusic,
        lyrics = playerUiState.lyrics,
        onMusicOrderChanged = onUpdateMusicOrder
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerScreenBigContent(
    modifier: Modifier = Modifier,
    draggableStatePercentage: Float,
    music: Music,
    uiState: PlayerUiState.Success,
    onSeekTo: (position: Long) -> Unit,
    onPlay: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    onShuffle: (Boolean) -> Unit,
    onRepeat: (Boolean) -> Unit,
    onClose: () -> Unit
) {
    val localConfiguration = LocalConfiguration.current
    val screenWidth = remember { localConfiguration.screenWidthDp.dp }

    Column {
        Column(modifier.heightIn(0.dp, (100.dp * draggableStatePercentage))) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

            Row(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 8.dp,
                        top = 4.dp
                    )
                    .widthIn(0.dp, screenWidth)
                    .width(screenWidth * draggableStatePercentage * 2),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = NcsIcons.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = onClose)
                )

                Icon(
                    imageVector = NcsIcons.MoreVertical,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = {})
                )
            }
        }

        PlayerScreenCoverImage(
            url = music.coverUrl,
            draggableStatePercentage = draggableStatePercentage
        )

        Column(
            modifier = Modifier
                .padding(top = 12.dp)
                .widthIn(0.dp, screenWidth)
                .width(screenWidth * draggableStatePercentage * 2),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = music.title,
                style = NcsTypography.Music.Title.large.copy(
                    color = ListItemCardDefaults.listItemCardColors().labelColor,
                ),
                modifier = Modifier.basicMarquee()
            )

            Text(
                text = music.artist,
                style = NcsTypography.Music.Artist.large.copy(
                    color = ListItemCardDefaults.listItemCardColors().descriptionColor,
                ),
                modifier = Modifier.basicMarquee()
            )

            PlayerPositionProgressBar(
                duration = uiState.duration,
                position = uiState.position,
                onSeekTo = onSeekTo,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp
                )
            )

            PlayerScreenBigController(
                isPlaying = uiState.isPlaying,
                hasNext = uiState.hasNext,
                isOnRepeat = uiState.isRepeatOn,
                isOnShuffle = uiState.isShuffleOn,
                onPlay = onPlay,
                onSkipPrevious = onSkipPrevious,
                onSkipNext = onSkipNext,
                onShuffle = onShuffle,
                onRepeat = onRepeat,
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
fun PlayerScreenBigController(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    isOnShuffle: Boolean,
    isOnRepeat: Boolean,
    hasNext: Boolean,
    onPlay: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    onShuffle: (Boolean) -> Unit,
    onRepeat: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        Icon(
            imageVector = NcsIcons.Shuffle,
            contentDescription = null,
            tint = if (isOnShuffle) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .clip(CircleShape)
                .size(32.dp)
                .clickable(onClick = { onShuffle(!isOnShuffle) })
        )

        Icon(
            imageVector = NcsIcons.SkipPrevious,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .clip(CircleShape)
                .size(32.dp)
                .clickable(onClick = onSkipPrevious)
        )

        Icon(
            imageVector = if (isPlaying) NcsIcons.Pause else NcsIcons.Play,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onPlay)
                .size(40.dp)
        )

        Icon(
            imageVector = NcsIcons.SkipNext,
            contentDescription = null,
            tint = if (hasNext) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .clip(CircleShape)
                .size(32.dp)
                .conditional(hasNext) {
                    clickable(onClick = onSkipNext)
                }
        )

        Icon(
            imageVector = NcsIcons.Repeat,
            contentDescription = null,
            tint = if (isOnRepeat) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .clip(CircleShape)
                .size(32.dp)
                .clickable(onClick = { onRepeat(!isOnRepeat) })
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
        thumbColor = MaterialTheme.colorScheme.onSurface
    )

    val thumbSize = 16.dp
    val trackHeight = 4.dp

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
                    interactionSource = interactionSource,
                    colors = colors,
                    thumbSize = DpSize(thumbSize, thumbSize)
                )
            },
            track = { sliderState ->
                Column {
                    SliderDefaults.Track(
                        sliderState = sliderState,
                        colors = colors,
                        enabled = true,
                    )
                    Spacer(
                        Modifier
                            .height(trackHeight)
                            .fillMaxWidth()
                    )
                }
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
    draggableStatePercentage: Float
) {
    val horizontalPadding = 16.dp

    AsyncImage(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(url)
            .size(Size.ORIGINAL)
            .build(),
        placeholder = painterResource(R.drawable.ncs_cover),
        contentDescription = null,
        contentScale = ContentScale.FillHeight,
        modifier = modifier
            .aspectRatio(1f)
            .padding(horizontal = (horizontalPadding * draggableStatePercentage))
    )
}

@OptIn(ExperimentalFoundationApi::class)
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
    isPlaying: Boolean,
    hasNext: Boolean,
    onPlay: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        Icon(
            imageVector = NcsIcons.SkipPrevious,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(20.dp)
                .clickable(onClick = onSkipPrevious)
        )

        Icon(
            imageVector = if (isPlaying) NcsIcons.Pause else NcsIcons.Play,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = onPlay)
                .size(28.dp)
        )

        Icon(
            imageVector = NcsIcons.SkipNext,
            contentDescription = null,
            tint = if (hasNext) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .clip(CircleShape)
                .size(20.dp)
                .conditional(hasNext) {
                    clickable(onClick = onSkipNext)
                }
        )
    }
}

private enum class SwipeAnchors {
    Small, Big
}


@OptIn(ExperimentalFoundationApi::class)
private val AnchoredDraggableState<SwipeAnchors>.percentage: Float
    get() = (
            (this.requireOffset() - this.anchors.minAnchor()) / (this.anchors.maxAnchor() - this.anchors.minAnchor())
            ).coerceIn(0f, 1f)


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun rememberDraggableState(maxHeight: Dp, minHeight: Dp): AnchoredDraggableState<SwipeAnchors> {
    val density = LocalDensity.current
    return remember(maxHeight, minHeight) {
        AnchoredDraggableState(
            initialValue = SwipeAnchors.Small,
            positionalThreshold = { distance -> distance * 0.3f },
            velocityThreshold = { with(density) { (minHeight * 1.3f).toPx() } },
            animationSpec = tween()
        ).apply {
            updateAnchors(
                DraggableAnchors {
                    SwipeAnchors.Small at minHeight.value
                    SwipeAnchors.Big at maxHeight.value
                }
            )
        }
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
                draggableStatePercentage = 1f,
                music = mockMusics.first(),
                uiState = PlayerUiState.Success(
                    isPlaying = true,
                    currentIndex = 0,
                    playlist = PlayList(
                        id = UUID.randomUUID(),
                        name = "My Playlist",
                        musics = mockMusics,
                        isUserCreated = true
                    )
                ),
                onSeekTo = {},
                onPlay = {},
                onSkipPrevious = {},
                onSkipNext = {},
                onShuffle = {},
                onRepeat = {},
                onClose = {},
                modifier = modifier
            )
        }
    }
}