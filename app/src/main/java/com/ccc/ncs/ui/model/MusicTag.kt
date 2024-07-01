package com.ccc.ncs.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ccc.ncs.model.MusicTag

@Composable
fun MusicTag.color(): Color = this.colorInt?.let {
    return Color(it)
} ?: Color(0xFFD6D6D6)

@Composable
fun MusicTag.backgroundColor(): Color = this.backgroundColorInt?.let {
    return Color(it)
} ?: Color.DarkGray