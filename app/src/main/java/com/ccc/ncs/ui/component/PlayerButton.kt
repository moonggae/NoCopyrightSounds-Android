package com.ccc.ncs.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.domain.model.PlayingStatus
import com.ccc.ncs.domain.model.RepeatMode
import com.ccc.ncs.ui.model.getContentDescription
import com.ccc.ncs.util.conditional

@Composable
fun PlayerButton(
    icon: ImageVector,
    contentDescription: String,
    enabled: Boolean = true,
    tint: Color? = null,
    size: Dp = 32.dp,
    iconPadding: Dp = 0.dp,
    onClick: () -> Unit,
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = tint ?: if (enabled) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .conditional(enabled) {
                clickable(onClick = onClick)
            }
            .padding(iconPadding)
    )
}

enum class PlayerControllerType(val size: Dp, val iconPadding: Dp) {
    Big(32.dp, 0.dp),
    BigCenter(40.dp, 0.dp),
    Small(48.dp, 12.dp),
    SmallCenter(48.dp, 8.dp)
}


@Composable
fun PlayerPlayingButton(
    playingStatus: PlayingStatus,
    type: PlayerControllerType = PlayerControllerType.BigCenter,
    onPlay: () -> Unit,
    onPause: () -> Unit,
) {
    when (playingStatus) {
        PlayingStatus.BUFFERING -> {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onSurface,
                strokeWidth = 2.dp,
                modifier = Modifier
                    .size(type.size)
                    .padding(type.iconPadding)
            )
        }

        PlayingStatus.PLAYING -> {
            PlayerButton(
                icon = NcsIcons.Pause,
                contentDescription = stringResource(R.string.cd_player_pause),
                size = type.size,
                iconPadding = type.iconPadding,
                onClick = onPause
            )
        }

        PlayingStatus.PAUSED,
        PlayingStatus.IDLE,
        PlayingStatus.ENDED -> {
            PlayerButton(
                icon = NcsIcons.Play,
                contentDescription = stringResource(R.string.cd_player_play),
                size = type.size,
                iconPadding = type.iconPadding,
                onClick = onPlay
            )
        }
    }
}

@Composable
fun PlayerSkipPreviousButton(
    type: PlayerControllerType = PlayerControllerType.Big,
    onClick: () -> Unit
) {
    PlayerButton(
        icon = NcsIcons.SkipPrevious,
        contentDescription = stringResource(R.string.cd_player_skip_previous),
        size = type.size,
        iconPadding = type.iconPadding,
        onClick = onClick
    )
}

@Composable
fun PlayerSkipNextButton(
    type: PlayerControllerType = PlayerControllerType.Big,
    hasNext: Boolean,
    onClick: () -> Unit
) {
    PlayerButton(
        icon = NcsIcons.SkipNext,
        contentDescription = if (hasNext) stringResource(R.string.cd_player_skip_next) else stringResource(R.string.cd_player_skip_next_disabled),
        enabled = hasNext,
        size = type.size,
        iconPadding = type.iconPadding,
        onClick = onClick
    )
}

@Composable
fun PlayerShuffleButton(
    type: PlayerControllerType = PlayerControllerType.Big,
    isOnShuffle: Boolean,
    onClick: () -> Unit
) {
    PlayerButton(
        icon = NcsIcons.Shuffle,
        contentDescription = if (isOnShuffle) stringResource(R.string.cd_player_shuffle_on) else stringResource(R.string.cd_player_shuffle_off),
        tint = if (isOnShuffle) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        size = type.size,
        iconPadding = type.iconPadding,
        onClick = onClick
    )
}

@Composable
fun PlayerRepeatButton(
    type: PlayerControllerType = PlayerControllerType.Big,
    repeatMode: RepeatMode,
    onClick: () -> Unit
) {
    PlayerButton(
        icon = when (repeatMode) {
            RepeatMode.REPEAT_MODE_ONE -> NcsIcons.RepeatOne
            else -> NcsIcons.Repeat
        },
        contentDescription = repeatMode.getContentDescription(),
        tint = when (repeatMode) {
            RepeatMode.REPEAT_MODE_OFF -> MaterialTheme.colorScheme.onSurfaceVariant
            else -> MaterialTheme.colorScheme.onSurface
        },
        size = type.size,
        iconPadding = type.iconPadding,
        onClick = onClick
    )
}