package com.ccc.ncs.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity("play_list")
data class PlayListEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val name: String
)