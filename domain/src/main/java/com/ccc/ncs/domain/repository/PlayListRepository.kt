package com.ccc.ncs.domain.repository

import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface PlayListRepository {
    fun getPlayLists(): Flow<List<PlayList>>

    fun getPlayList(id: UUID): Flow<PlayList?>

    suspend fun getAutoGeneratedPlayList(): PlayList

    suspend fun insertPlayList(name: String, isUserCreated: Boolean)

    suspend fun setPlayListMusics(playListId: UUID, musics: List<Music>)

    suspend fun setPlayListMusicsWithId(playListId: UUID, musicIds: List<UUID>)

    suspend fun updatePlayListName(playListId: UUID, name: String)

    suspend fun deletePlayList(id: UUID)
}