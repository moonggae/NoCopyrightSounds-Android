package com.ccc.ncs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ccc.ncs.data.paging.MUSIC_LOAD_SIZE
import com.ccc.ncs.data.paging.MusicPagingSource
import com.ccc.ncs.model.Music
import com.ccc.ncs.network.NcsNetworkDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultMusicRepository @Inject constructor(
    private val network: NcsNetworkDataSource
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
}