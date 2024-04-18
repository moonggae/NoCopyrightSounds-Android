package com.ccc.ncs.data.repository

import com.ccc.ncs.database.dao.PlayListDao
import com.ccc.ncs.database.model.PlayListEntity
import com.ccc.ncs.database.model.reference.PlayListMusicCrossRef
import com.ccc.ncs.database.model.relation.asModel
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class DefaultPlayListRepository @Inject constructor(
    private val playListDao: PlayListDao,
    private val musicRepository: MusicRepository
): PlayListRepository {
    override fun getPlayLists(): Flow<List<PlayList>> = playListDao
        .getAllPlayList()
        .map { list -> list.map { it.asModel() } }

    override fun getPlayList(id: UUID): Flow<PlayList?> = playListDao
        .getPlayList(id)
        .map { it?.asModel() }

    override fun insertPlayList(name: String): Flow<PlayList> = flow {
        val rowId = playListDao.insertPlayList(PlayListEntity(name = name))
        val playList = playListDao.getPlayListByRowId(rowId)
            .firstOrNull()
            ?: throw IllegalStateException("Failed to insert playlist: No playlist found with row ID $rowId")

        emit(playList.asModel())
    }

    override fun setPlayListMusics(playListId: UUID, musics: List<Music>): Flow<PlayList> = flow {
        playListDao.unLinkAllMusic(playListId)
        val insertedMusics = musicRepository.insertMusics(musics).first()
        playListDao.linkMusicToPlayList(insertedMusics.map {
            PlayListMusicCrossRef(
                playListId = playListId,
                musicId = it.id
            )
        })

        emit(playListDao.getPlayList(playListId).first()?.asModel() ?: throw Exception("fail to set play list musics"))
    }

    override fun updatePlayListName(playListId: UUID, name: String): Flow<PlayList> = flow {
        playListDao.updatePlayList(PlayListEntity(playListId, name))
        emit(playListDao.getPlayList(playListId).first()?.asModel() ?: throw Exception("fail to update play list name"))
    }

    override suspend fun deletePlayList(id: UUID) = playListDao.deletePlayList(id)
}