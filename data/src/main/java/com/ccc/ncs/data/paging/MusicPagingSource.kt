package com.ccc.ncs.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ccc.ncs.model.Music
import com.ccc.ncs.network.NcsNetworkDataSource
import java.io.IOException


const val MUSIC_LOAD_SIZE = 20

class MusicPagingSource(
    private val dataSource: NcsNetworkDataSource,
    private val query: String?,
    private val genreId: Int?,
    private val moodId: Int?,
    private val version: String?
): PagingSource<Int, Music>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Music> {
        try {
            val currentPage = params.key ?: 1

            val response = dataSource.getMusics(
                page = currentPage,
                query = query,
                genreId = genreId,
                moodId = moodId,
                version = version
            )

            val isLastPage = response.size < MUSIC_LOAD_SIZE

            return LoadResult.Page(
                data = response,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (isLastPage) null else currentPage + 1
            )
        } catch (retryableError: IOException) {
            return LoadResult.Error(retryableError)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Music>): Int? = null
}