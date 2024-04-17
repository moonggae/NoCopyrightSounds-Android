package com.ccc.ncs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ccc.ncs.data.paging.MUSIC_LOAD_SIZE
import com.ccc.ncs.data.paging.MusicPagingSource
import com.ccc.ncs.database.dao.GenreDao
import com.ccc.ncs.database.dao.MoodDao
import com.ccc.ncs.database.model.asEntity
import com.ccc.ncs.database.model.asModel
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import com.ccc.ncs.network.NcsNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DefaultMusicRepository @Inject constructor(
    private val network: NcsNetworkDataSource,
    private val genreDao: GenreDao,
    private val moodDao: MoodDao
) : MusicRepository {
    override fun getSearchResultStream(
        query: String?,
        genreId: Int?,
        moodId: Int?,
        version: String?
    ): Flow<PagingData<Music>> = Pager(
        PagingConfig(
            pageSize = MUSIC_LOAD_SIZE,
            enablePlaceholders = false,
            prefetchDistance = MUSIC_LOAD_SIZE * 2
        )
    ) {
        MusicPagingSource(
            dataSource = network,
            query = query,
            genreId = genreId,
            moodId = moodId,
            version = version
        )
    }.flow

    override suspend fun initGenreAndMood() {
        val (genres, moods) = network.getAllGenreAndMood()
        moodDao.deleteAllMoods()
        moodDao.insertAllMoods(moods.map { it.asEntity() })

        genreDao.deleteAllGenres()
        genreDao.insertAllGenres(genres.map { it.asEntity() })
    }

    override fun getGenres(): Flow<List<Genre>> = genreDao
        .getAllGenres()
        .map { list -> list.map { it.asModel() } }

    override fun getMoods(): Flow<List<Mood>> = moodDao
        .getAllMoods()
        .map { list -> list.map { it.asModel() } }
}