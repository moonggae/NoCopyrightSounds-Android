package com.ccc.ncs.database.mock

import com.ccc.ncs.database.model.PlayListEntity
import com.ccc.ncs.database.model.relation.PlayListWithMusics

internal val MockPlayList = PlayListEntity(
    id = 1,
    name = "My PlayList"
)

internal val MockPlayListWithMusics: PlayListWithMusics = PlayListWithMusics(
    playList = MockPlayList,
    musics = listOf(MockMusicWithGenreAndMood)
)