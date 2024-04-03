package com.ccc.ncs.network

import com.ccc.ncs.model.Music

interface NcsNetworkDataSource {
    suspend fun getMusics(
        page: Int,
        query: String? = null,
        genreId: Int? = null,
        moodId: Int? = null,
        version: String? = null,
    ): List<Music>
}