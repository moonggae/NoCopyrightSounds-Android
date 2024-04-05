package com.ccc.ncs.data.repository

import androidx.paging.PagingData
import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.Music
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {
    fun getSearchResultStream(
        query: String? = null,
        sort: String? = null,
        year: Int? = null
    ): Flow<PagingData<Artist>>
}