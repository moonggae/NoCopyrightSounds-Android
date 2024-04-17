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

@Dao
interface PlayListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayList(playList: PlayListEntity)

    @Transaction
    @Query("SELECT * FROM play_list WHERE id = :id")
    fun getPlayList(id: Int): Flow<PlayListWithMusics?>

    @Transaction
    @Query("SELECT * FROM play_list")
    fun getAllPlayList(): Flow<List<PlayListWithMusics>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun linkMusicToPlayList(crossRef: List<PlayListMusicCrossRef>)

    @Query("DELETE FROM play_list_music_cross_ref WHERE playListId = :playListId")
    suspend fun unLinkAllMusic(playListId: Int)

    @Update
    suspend fun updatePlayList(playList: PlayListEntity)

    @Delete
    suspend fun deletePlayList(playList: PlayListEntity)
}