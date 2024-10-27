package com.ccc.ncs.designsystem.component

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ccc.ncs.designsystem.R
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTypography
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeToDeleteCard(
    modifier: Modifier = Modifier,
    deleteText: String = "Delete",
    onDelete: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current
    val itemHeight = remember { mutableStateOf(0.dp) }
    val state = rememberSwipeState(density)

    LaunchedEffect(state.currentValue) {
        if (state.currentValue == DragAnchors.End) {
            onDelete()
        }
    }

    Box(modifier = modifier) {
        DeleteBackground(itemHeight.value, onDelete, deleteText)
        SwipeableContent(
            state = state,
            density = density,
            onHeightChanged = { itemHeight.value = it },
            content = content
        )
    }
}

@Composable
private fun DeleteBackground(
    height: Dp,
    onDelete: () -> Unit,
    deleteText: String
) {
    Box(
        Modifier
            .height(height)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.error)
    ) {
        DeleteAction(height, onDelete, deleteText)
    }
}

@Composable
private fun BoxScope.DeleteAction(
    height: Dp,
    onDelete: () -> Unit,
    deleteText: String
) {
    Column(
        modifier = Modifier
            .height(height)
            .width(ACTION_SIZE)
            .align(Alignment.CenterEnd)
            .clickable(onClick = onDelete),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = NcsIcons.Delete,
            contentDescription = stringResource(R.string.cd_delete_item),
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = deleteText,
            style = NcsTypography.ActionCard.text.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SwipeableContent(
    state: AnchoredDraggableState<DragAnchors>,
    density: Density,
    onHeightChanged: (Dp) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged {
                onHeightChanged(with(density) { it.height.toDp() })
            }
            .offset {
                IntOffset(
                    x = state
                        .requireOffset()
                        .roundToInt(),
                    y = 0,
                )
            }
            .anchoredDraggable(
                state = state,
                orientation = Orientation.Horizontal
            ),
        content = content
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun rememberSwipeState(density: Density): AnchoredDraggableState<DragAnchors> {
    val actionSizePx = with(density) { ACTION_SIZE.toPx() }
    val screenWidthPx = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }

    return remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { VELOCITY_THRESHOLD.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = exponentialDecay(),
            anchors = DraggableAnchors {
                DragAnchors.Start at 0f
                DragAnchors.Center at -actionSizePx
                DragAnchors.End at -screenWidthPx
            },
        )
    }
}

private enum class DragAnchors { Start, Center, End }

private val ACTION_SIZE = 80.dp
private val VELOCITY_THRESHOLD = 100.dp