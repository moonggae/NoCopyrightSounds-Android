package com.ccc.ncs.data.repository

import androidx.paging.PagingData
import com.ccc.ncs.model.Music
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    fun getSearchResultStream(
        query: String? = null,
        genreId: Int? = null,
        moodId: Int? = null,
        version: String? = null
    ): Flow<PagingData<Music>>
}