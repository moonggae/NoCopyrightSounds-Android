package com.ccc.ncs.data.testdouble

import com.ccc.ncs.database.dao.MoodDao
import com.ccc.ncs.database.model.MoodEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

class TestMoodDao: MoodDao {
    private val entitiesStateFlow = MutableStateFlow(emptyList<MoodEntity>())

    override suspend fun insertAllMoods(moods: List<MoodEntity>): List<Long> {
        entitiesStateFlow.update { oldValues ->
            (moods + oldValues)
                .distinctBy(MoodEntity::id)
                .sortedWith(compareBy(MoodEntity::id))
        }

        return (0 .. entitiesStateFlow.first().size).map { it.toLong() }
    }

    override fun getAllMoods(): Flow<List<MoodEntity>> = entitiesStateFlow

    override suspend fun deleteAllMoods() {
        entitiesStateFlow.update { emptyList() }
    }
}