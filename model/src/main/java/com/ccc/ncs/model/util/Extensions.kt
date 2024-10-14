package com.ccc.ncs.model.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun <T> List<T>.swap(index1: Int, index2: Int): List<T> {
    return this.toMutableList().apply {
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }
}

fun <T> List<T>.replace(index: Int, element: T): List<T> {
    return this.toMutableList().apply {
        removeAt(index)
        add(index, element)
    }
}

fun Long.toTimestampMMSS(): String {
    val minutes = when (this > 0) {
        true -> this / 1000 / 60
        else -> 0
    }
    val seconds = when (this > 0) {
        true -> this / 1000 % 60
        else -> 0
    }
    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}

fun LocalDate.toString(pattern: String): String = this.format(DateTimeFormatter.ofPattern(pattern))