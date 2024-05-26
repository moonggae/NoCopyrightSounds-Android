package com.ccc.ncs.feature.play

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.ListItemCardDefaults
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.util.conditional


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayingScreen(
    modifier: Modifier = Modifier,
    minHeight: Dp = 60.dp,
    onUpdateScreenSize: (percentage: Float) -> Unit,
    playerUiState: PlayerUiState.Success,
    onPlay: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
) {
    val maxHeight = calculateScreenHeight()
    val draggableState = rememberDraggableState(
        maxHeight = maxHeight,
        minHeight = minHeight
    )

    LaunchedEffect(draggableState.percentage) {
        onUpdateScreenSize(draggableState.percentage)
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
        ),
    ) {
        playerUiState.currentMusic?.let { music ->
            Column {
                Row(Modifier.weight(1f)) {
                    PlayingScreenCoverImage(
                        url = music.coverUrl,
                        draggableStatePercentage = draggableState.percentage
                    )

                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .fillMaxHeight()
                            .weight(1f),
                    ) {
                        Text(
                            text = music.title,
                            style = ListItemCardDefaults.listItemCardStyle.medium().labelTextStyle.copy(
                                color = ListItemCardDefaults.listItemCardColors().labelColor,
                            ),
                            modifier = Modifier.basicMarquee()
                        )
                        Text(
                            text = music.artist,
                            style = ListItemCardDefaults.listItemCardStyle.medium().descriptionTextStyle.copy(
                                color = ListItemCardDefaults.listItemCardColors().descriptionColor,
                            ),
                            modifier = Modifier.basicMarquee()
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 12.dp),
                    ) {
                        Icon(
                            imageVector = NcsIcons.SkipPrevious,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(28.dp)
                                .clickable(onClick = onSkipPrevious)
                        )

                        Icon(
                            imageVector = if (playerUiState.isPlaying) NcsIcons.Pause else NcsIcons.Play,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable(onClick = onPlay)
                                .size(28.dp)
                        )

                        Icon(
                            imageVector = NcsIcons.SkipNext,
                            contentDescription = null,
                            tint = if (playerUiState.hasNext) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(28.dp)
                                .conditional(playerUiState.hasNext) {
                                    clickable(onClick = onSkipNext)
                                }
                        )
                    }
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
                )
            }


        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlayingScreenCoverImage(
    modifier: Modifier = Modifier,
    url: String,
    draggableStatePercentage: Float
) {
    val horizontalPadding = 16.dp

    AsyncImage(
        model = url,
        placeholder = painterResource(R.drawable.ncs_cover),
        contentDescription = null,
        contentScale = ContentScale.FillHeight,
        modifier = modifier.padding(horizontal = (horizontalPadding * draggableStatePercentage))
    )
}

private enum class SwipeAnchors {
    Small, Big
}


@OptIn(ExperimentalFoundationApi::class)
private val AnchoredDraggableState<SwipeAnchors>.percentage: Float
    get() = (
            (this.requireOffset() - this.anchors.minAnchor()) / (this.anchors.maxAnchor() - this.anchors.minAnchor())
            ).coerceIn(0f, 1f)


@Composable
private fun calculateScreenHeight(): Dp {
    val configuration = LocalConfiguration.current
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    return configuration.screenHeightDp.dp + statusBarHeight + navigationBarHeight
}

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