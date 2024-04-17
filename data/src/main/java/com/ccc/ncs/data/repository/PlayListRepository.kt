package com.ccc.ncs.data.repository

import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    fun getPlayLists(): Flow<List<PlayList>>

    fun getPlayList(id: Int): Flow<PlayList>

    suspend fun insertPlayList(name: String)

    suspend fun setPlayListMusics(musics: List<Music>)

    suspend fun deletePlayList(id: Int)
}