package com.ccc.ncs.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ccc.ncs.model.Genre

@Entity("genre")
data class GenreEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)

fun Genre.asEntity() = GenreEntity(
    id = id,
    name = name
)

fun GenreEntity.asModel() = Genre(
    id = id,
    name = name
)