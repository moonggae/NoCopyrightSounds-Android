package com.ccc.ncs.model

import androidx.compose.ui.graphics.Color

fun Mood.color(): Color? = this.colorInt?.let {
    return Color(it)
}

fun Mood.backgroundColor(): Color? = this.backgroundColorInt?.let {
    return Color(it)
}