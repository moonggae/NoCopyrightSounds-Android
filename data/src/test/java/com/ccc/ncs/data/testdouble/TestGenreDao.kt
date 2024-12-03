package com.ccc.ncs.data.testdouble

import com.ccc.ncs.database.dao.GenreDao
import com.ccc.ncs.database.model.GenreEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

class TestGenreDao: GenreDao {
    private val entitiesStateFlow = MutableStateFlow(emptyList<GenreEntity>())

    override suspend fun insertAllGenres(genres: List<GenreEntity>): List<Long> {
        entitiesStateFlow.update { oldValues ->
            (genres + oldValues)
                .distinctBy(GenreEntity::id)
                .sortedWith(compareBy(GenreEntity::id))
        }

        return (0 .. entitiesStateFlow.first().size).map { it.toLong() }
    }

    override fun getAllGenres(): Flow<List<GenreEntity>> = entitiesStateFlow

    override suspend fun deleteAllGenres() {
        entitiesStateFlow.update { emptyList() }
    }
}