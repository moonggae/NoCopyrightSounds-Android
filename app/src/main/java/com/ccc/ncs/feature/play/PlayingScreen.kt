package com.ccc.ncs.feature.play

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayingScreen(
    modifier: Modifier = Modifier,
    minHeight: Dp = 60.dp,
    onUpdateScreenSize: (percentage: Float) -> Unit
) {
    val maxHeight = calculateScreenHeight()
    val draggableState = rememberDraggableState(
        maxHeight = maxHeight,
        minHeight = minHeight
    )

    LaunchedEffect(draggableState.percentage) {
        onUpdateScreenSize(draggableState.percentage)
    }

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
                .background(Color.Yellow)
        )
    ) {

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