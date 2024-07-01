package com.ccc.ncs.model

data class Mood(
    override val id: Int,
    override val name: String,
    override val colorInt: Int?,
    override val backgroundColorInt: Int?
): MusicTag