package com.ccc.ncs.data.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ccc.ncs.model.Artist
import com.ccc.ncs.network.NcsNetworkDataSource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ArtistPagingData @Inject constructor(
    private val network: NcsNetworkDataSource
) {
    fun getData(
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