package com.ccc.ncs.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ccc.ncs.R
import com.ccc.ncs.domain.model.RepeatMode

@Composable
fun RepeatMode.getContentDescription(): String = when (this) {
    RepeatMode.REPEAT_MODE_ALL -> stringResource(R.string.cd_player_repeat_all)
    RepeatMode.REPEAT_MODE_ONE -> stringResource(R.string.cd_player_repeat_one)
    RepeatMode.REPEAT_MODE_OFF -> stringResource(R.string.cd_player_repeat_off)
}