package com.ccc.ncs.data.testdouble

import com.ccc.ncs.database.dao.PlayListDao
import com.ccc.ncs.database.model.PlayListEntity
import com.ccc.ncs.database.model.reference.PlayListMusicCrossRef
import com.ccc.ncs.database.model.relation.PlayListWithMusics
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class TestPlayListDao: PlayListDao {
    override suspend fun insertPlayList(playList: PlayListEntity): Long {
        TODO("Not yet implemented")
    }

    override fun getPlayList(id: UUID): Flow<PlayListWithMusics?> {
        TODO("Not yet implemented")
    }

    override fun getPlayListByRowId(rowId: Long): Flow<PlayListWithMusics?> {
        TODO("Not yet implemented")
    }

    override fun getAllPlayList(): Flow<List<PlayListWithMusics>> {
        TODO("Not yet implemented")
    }

    override suspend fun linkMusicToPlayList(crossRef: List<PlayListMusicCrossRef>) {
        TODO("Not yet implemented")
    }

    override suspend fun unLinkAllMusic(playListId: UUID) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlayList(playList: PlayListEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlayList(id: UUID) {
        TODO("Not yet implemented")
    }
}