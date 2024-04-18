package com.ccc.ncs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ccc.ncs.database.model.PlayListEntity
import com.ccc.ncs.database.model.reference.PlayListMusicCrossRef
import com.ccc.ncs.database.model.relation.PlayListWithMusics
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface PlayListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayList(playList: PlayListEntity): Long

    @Transaction
    @Query("SELECT * FROM play_list WHERE id = :id")
    fun getPlayList(id: UUID): Flow<PlayListWithMusics?>

    @Transaction
    @Query("SELECT * FROM play_list WHERE rowid = :rowId")
    fun getPlayListByRowId(rowId: Long): Flow<PlayListWithMusics?>

    @Transaction
    @Query("SELECT * FROM play_list ORDER BY rowid")
    fun getAllPlayList(): Flow<List<PlayListWithMusics>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun linkMusicToPlayList(crossRef: List<PlayListMusicCrossRef>)

    @Query("DELETE FROM play_list_music_cross_ref WHERE playListId = :playListId")
    suspend fun unLinkAllMusic(playListId: UUID)

    @Update
    suspend fun updatePlayList(playList: PlayListEntity)

    @Query("DELETE FROM play_list WHERE id = :id")
    suspend fun deletePlayList(id: UUID)
}