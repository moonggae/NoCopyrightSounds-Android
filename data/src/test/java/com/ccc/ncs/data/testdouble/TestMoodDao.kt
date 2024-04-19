package com.ccc.ncs.data.testdouble

import com.ccc.ncs.database.dao.MoodDao
import com.ccc.ncs.database.model.MoodEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TestMoodDao: MoodDao {
    private val entitiesStateFLow = MutableStateFlow(emptyList<MoodEntity>())

    override suspend fun insertAllMoods(moods: List<MoodEntity>) {
        entitiesStateFLow.update { oldValues ->
            (moods + oldValues)
                .distinctBy(MoodEntity::id)
                .sortedWith(compareBy(MoodEntity::id))
        }
    }

    override fun getAllMoods(): Flow<List<MoodEntity>> = entitiesStateFLow

    override suspend fun deleteAllMoods() {
        entitiesStateFLow.update { emptyList() }
    }
}