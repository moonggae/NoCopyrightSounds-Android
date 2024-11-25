package com.ccc.ncs.data.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ccc.ncs.domain.repository.MusicRepository
import com.ccc.ncs.model.Music
import com.ccc.ncs.network.NcsNetworkDataSource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class MusicPagingData @Inject constructor(
    private val network: NcsNetworkDataSource,
    private val musicRepository: MusicRepository
){
    fun getData(
        query: String? = null,
        genreId: Int? = null,
        moodId: Int? = null,
        version: String? = null
    ): Flow<PagingData<Music>> = Pager(
        PagingConfig(
            pageSize = MUSIC_LOAD_SIZE,
            enablePlaceholders = false,
            prefetchDistance = MUSIC_LOAD_SIZE * 2
        )
    ) {
        MusicPagingSource(
            dataSource = network,
            syncLocalMusics = musicRepository::insertNotExistMusics,
            query = query,
            genreId = genreId,
            moodId = moodId,
            version = version
        )
    }.flow
}