package com.ccc.ncs.util

import androidx.compose.ui.Modifier

fun <T> List<T>.swap(index1: Int, index2: Int): List<T> {
    return this.toMutableList().apply {
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }
}

fun Modifier.conditional(condition : Boolean, modifier : Modifier.() -> Modifier) : Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}