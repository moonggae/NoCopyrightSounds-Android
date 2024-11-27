package com.ccc.ncs.database.model.reference

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ccc.ncs.database.model.PlayListEntity
import java.util.UUID


@Entity(
    tableName = "play_list_music_cross_ref",
    foreignKeys = [
        ForeignKey(
            entity = PlayListEntity::class,
            parentColumns = ["id"],
            childColumns = ["playListId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("playListId"),
        Index("musicId")
    ]
)
data class PlayListMusicCrossRef(
    val playListId: UUID,
    val musicId: UUID,
) {
    @PrimaryKey(autoGenerate = true)
    var order: Int = 0
}