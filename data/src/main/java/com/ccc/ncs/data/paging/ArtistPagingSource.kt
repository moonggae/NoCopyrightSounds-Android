package com.ccc.ncs.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ccc.ncs.model.Artist
import com.ccc.ncs.network.NcsNetworkDataSource

const val ARTIST_LOAD_SIZE = 20

class ArtistPagingSource(
    private val dataSource: NcsNetworkDataSource,
    private val query: String?,
    private val sort: String?,
    private val year: Int?
): PagingSource<Int, Artist>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        val currentPage = params.key ?: 1

        try {
            val response = dataSource.getArtists(
                page = currentPage,
                query = query,
                sort = sort,
                year = year
            )

            val isLastPage = response.size < ARTIST_LOAD_SIZE

            return LoadResult.Page(
                data = response,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (isLastPage) null else currentPage + 1
            )
        } catch (th: Throwable) {
            return LoadResult.Error(th)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? = null
}