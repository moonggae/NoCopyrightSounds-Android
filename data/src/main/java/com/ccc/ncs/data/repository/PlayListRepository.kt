package com.ccc.ncs.data.repository

import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface PlayListRepository {
    fun getPlayLists(): Flow<List<PlayList>>

    fun getPlayList(id: UUID): Flow<PlayList?>

    suspend fun insertPlayList(name: String)

    fun setPlayListMusics(playListId: UUID, musics: List<Music>): Flow<PlayList>

    fun updatePlayListName(playListId: UUID, name: String): Flow<PlayList>

    suspend fun deletePlayList(id: UUID)
}