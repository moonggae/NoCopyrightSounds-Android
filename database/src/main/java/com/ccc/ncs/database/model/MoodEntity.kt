package com.ccc.ncs.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("mood")
data class MoodEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)
