package com.ccc.ncs.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("play_list")
data class PlayListEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)