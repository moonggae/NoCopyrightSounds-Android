package com.ccc.ncs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ccc.ncs.data.paging.ARTIST_LOAD_SIZE
import com.ccc.ncs.data.paging.ArtistPagingSource
import com.ccc.ncs.model.Artist
import com.ccc.ncs.network.NcsNetworkDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultArtistRepository @Inject constructor(
    private val network: NcsNetworkDataSource
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

}