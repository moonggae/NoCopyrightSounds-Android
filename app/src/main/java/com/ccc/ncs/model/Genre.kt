package com.ccc.ncs.model

import androidx.compose.ui.graphics.Color

fun Genre.color(): Color? = this.colorInt?.let {
    return Color(it)
}

fun Genre.backgroundColor(): Color? = this.backgroundColorInt?.let {
    return Color(it)
}