package com.ccc.ncs.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("genre")
data class GenreEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)