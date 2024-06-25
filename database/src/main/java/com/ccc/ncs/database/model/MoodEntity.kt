package com.ccc.ncs.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ccc.ncs.model.Mood

@Entity("mood")
data class MoodEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val colorInt: Int? = null,
    val backgroundColorInt: Int? = null
)

fun Mood.asEntity() = MoodEntity(
    id = id,
    name = name,
    colorInt = colorInt,
    backgroundColorInt = backgroundColorInt
)

fun MoodEntity.asModel() = Mood(
    id = id,
    name = name,
    colorInt = colorInt,
    backgroundColorInt = backgroundColorInt
)