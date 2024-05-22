package com.ccc.ncs.data.repository

import androidx.paging.PagingData
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface MusicRepository {
    fun getSearchResultStream(
        query: String? = null,
        genreId: Int? = null,
        moodId: Int? = null,
        version: String? = null
    ): Flow<PagingData<Music>>

    fun getMusics(musicIds: List<UUID>): Flow<List<Music>>

    suspend fun initGenreAndMood()

    fun insertMusics(musics: List<Music>): Flow<List<Music>>

    fun getGenres(): Flow<List<Genre>>

    fun getMoods(): Flow<List<Mood>>
}