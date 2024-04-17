package com.ccc.ncs.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ccc.ncs.database.model.GenreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGenres(genres: List<GenreEntity>)

    @Query("SELECT * FROM genre")
    fun getAllGenres(): Flow<List<GenreEntity>>

    @Query("DELETE FROM genre")
    suspend fun deleteAllGenres()
}