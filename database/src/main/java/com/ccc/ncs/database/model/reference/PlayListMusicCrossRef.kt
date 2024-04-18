package com.ccc.ncs.database.model.reference

import androidx.room.Entity
import androidx.room.ForeignKey
import com.ccc.ncs.database.model.PlayListEntity
import java.util.UUID


@Entity(
    tableName = "play_list_music_cross_ref",
    primaryKeys = ["playListId", "musicId"],
    foreignKeys = [
        ForeignKey(
            entity = PlayListEntity::class,
            parentColumns = ["id"],
            childColumns = ["playListId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlayListMusicCrossRef(
    val playListId: UUID,
    val musicId: UUID
)