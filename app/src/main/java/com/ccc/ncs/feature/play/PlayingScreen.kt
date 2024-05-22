package com.ccc.ncs.feature.play

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ccc.ncs.designsystem.icon.NcsIcons


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayingScreen(
    modifier: Modifier = Modifier,
    minHeight: Dp = 60.dp,
    onUpdateScreenSize: (percentage: Float) -> Unit,
    playerUiState: PlayerUiState.Success,
    onPlay: () -> Unit,
) {
    val maxHeight = calculateScreenHeight()
    val draggableState = rememberDraggableState(
        maxHeight = maxHeight,
        minHeight = minHeight
    )

    LaunchedEffect(draggableState.percentage) {
        onUpdateScreenSize(draggableState.percentage)
    }

    val music by rememberUpdatedState(playerUiState.currentMusic)

    LaunchedEffect(music) {
        Log.d("TAG", "PlayingScreen - music: ${music}")
    }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val imageMaxSize = screenWidth - (16.dp * 2)


    Column(
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
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            playerUiState.currentMusic?.coverUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .padding(horizontal = (16.dp * draggableState.percentage))
                        .heightIn(0.dp, imageMaxSize),
                )
            }

            Icon(
                imageVector = NcsIcons.Play,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clickable(onClick = onPlay)
            )
        }
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