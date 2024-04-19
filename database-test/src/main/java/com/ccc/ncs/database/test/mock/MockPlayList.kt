package com.ccc.ncs.database.test.mock

import com.ccc.ncs.database.model.PlayListEntity
import com.ccc.ncs.database.model.relation.PlayListWithMusics

val MockPlayList = PlayListEntity(
    name = "My PlayList"
)

val MockPlayListWithMusics: PlayListWithMusics = PlayListWithMusics(
    playList = MockPlayList,
    musics = listOf(MockMusicWithGenreAndMoodList[0])
)