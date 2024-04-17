package com.ccc.ncs.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ccc.ncs.model.Mood

@Entity("mood")
data class MoodEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)

fun Mood.asEntity() = MoodEntity(
    id = id,
    name = name
)

fun MoodEntity.asModel() = Mood(
    id = id,
    name = name
)