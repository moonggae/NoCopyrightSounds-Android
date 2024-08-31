package com.ccc.ncs.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ccc.ncs.database.model.MoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMoods(moods: List<MoodEntity>): List<Long>

    @Query("SELECT * FROM mood")
    fun getAllMoods(): Flow<List<MoodEntity>>

    @Query("DELETE FROM mood")
    suspend fun deleteAllMoods()
}