package com.ccc.ncs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ccc.ncs.data.paging.ARTIST_LOAD_SIZE
import com.ccc.ncs.data.paging.ArtistPagingSource
import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.ArtistDetail
import com.ccc.ncs.network.NcsNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class DefaultArtistRepository @Inject constructor(
    private val network: NcsNetworkDataSource,
    private val musicRepository: MusicRepository
) : ArtistRepository {
    override fun getSearchResultStream(
        query: String?,
        sort: String?,
        year: Int?
    ): Flow<PagingData<Artist>> = Pager(
        PagingConfig(
            pageSize = ARTIST_LOAD_SIZE,
            enablePlaceholders = false,
            prefetchDistance = ARTIST_LOAD_SIZE * 2
        )
    ) {
        ArtistPagingSource(
            dataSource = network,
            query = query,
            sort = sort,
            year = year
        )
    }.flow

    override fun getArtistDetail(artistDetailPath: String): Flow<ArtistDetail?> = flow {
        val artistDetail = network.getArtistDetail(artistDetailPath)
        artistDetail?.musics?.let { musicRepository.syncLocalMusics(it) }
        emit(artistDetail)
    }
}