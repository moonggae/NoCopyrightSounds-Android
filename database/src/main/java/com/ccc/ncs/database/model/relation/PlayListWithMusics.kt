package com.ccc.ncs.database.model.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.ccc.ncs.database.model.MusicEntity
import com.ccc.ncs.database.model.PlayListEntity
import com.ccc.ncs.database.model.reference.PlayListMusicCrossRef
import com.ccc.ncs.model.PlayList

data class PlayListWithMusics(
    @Embedded val playList: PlayListEntity,

    @Relation(
        parentColumn = "id",
        entity = MusicEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = PlayListMusicCrossRef::class,
            parentColumn = "playListId",
            entityColumn = "musicId"
        )
    )
    val musics: List<MusicWithGenreAndMood>,
)


fun PlayListWithMusics.asModel() = PlayList(
    id = playList.id,
    name = playList.name,
    musics = musics.map { it.asModel() },
    isUserCreated = playList.isUserCreated
)