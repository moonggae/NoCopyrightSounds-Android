package com.ccc.ncs.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ccc.ncs.database.model.MusicEntity
import com.ccc.ncs.database.model.reference.MusicGenreCrossRef
import com.ccc.ncs.database.model.reference.MusicMoodCrossRef
import com.ccc.ncs.database.model.relation.MusicWithGenreAndMood
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusic(music: MusicEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusics(musics: List<MusicEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun linkMusicToGenre(crossRef: List<MusicGenreCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun linkMusicToMood(crossRef: List<MusicMoodCrossRef>)

    @Transaction
    @Query("SELECT * FROM music WHERE id = :id")
    fun getMusic(id: UUID): Flow<MusicWithGenreAndMood?>

    @Transaction
    @Query("SELECT * FROM music WHERE id in (:ids) ORDER BY rowid")
    fun getMusics(ids: List<UUID>): Flow<List<MusicWithGenreAndMood>>

    @Transaction
    @Query("SELECT * FROM music WHERE status in (:status) ORDER BY rowid")
    fun getMusicsByStatus(status: List<String>): Flow<List<MusicWithGenreAndMood>>

    @Transaction
    @Query("SELECT * FROM music WHERE status = 'Downloading' ORDER BY rowid")
    fun getDownloadingMusics(): Flow<List<MusicWithGenreAndMood>>

    @Transaction
    @Query("SELECT * FROM music WHERE localUri is not null ORDER BY rowid")
    fun getDownloadedMusics(): Flow<List<MusicWithGenreAndMood>>

    @Update
    suspend fun updateMusic(music: MusicEntity)
}