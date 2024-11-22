package com.ccc.ncs.domain.model

enum class RepeatMode(val value: Int) {
    REPEAT_MODE_OFF(0),
    REPEAT_MODE_ONE(1),
    REPEAT_MODE_ALL(2);

    fun next(): RepeatMode =
        if (this.value == RepeatMode.entries.maxOf { it.value }) RepeatMode.entries.minBy { it.value }
        else RepeatMode.valueOf(this.value + 1)

    companion object {
        fun valueOf(value: Int): RepeatMode =
            RepeatMode.entries.firstOrNull { it.value == value }
            ?: throw IllegalArgumentException("Not exists repeat mode")
    }
}