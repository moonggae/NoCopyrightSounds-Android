package com.ccc.ncs.feature.play

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayingScreen(
    modifier: Modifier = Modifier,
    maxHeight: Dp
) {
    val density = LocalDensity.current

    LaunchedEffect(maxHeight) {
        Log.d("TAG", " - PlayingScreen - maxHeight: ${maxHeight}")
    }

    val state = remember {
        AnchoredDraggableState(
            initialValue = SwipeAnchors.Small,
            positionalThreshold = { distance: Float -> distance * 0.3f },
            velocityThreshold = { with(density) { 80.dp.toPx() } },
            animationSpec = tween()
        ).apply {
            updateAnchors(
                DraggableAnchors {
                    SwipeAnchors.Small at 60f
                    SwipeAnchors.Big at maxHeight.value
                }
            )
        }
    }

    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .height(state.requireOffset().dp)
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Vertical,
                    reverseDirection = true
                )
                .background(Color.Yellow)
        )
    ) {

    }
}

private enum class SwipeAnchors  {
    Small, Big
}