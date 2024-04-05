package com.ccc.ncs.network

import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.Music

interface NcsNetworkDataSource {
    suspend fun getMusics(
        page: Int,
        query: String? = null,
        genreId: Int? = null,
        moodId: Int? = null,
        version: String? = null,
    ): List<Music>

    suspend fun getArtists(
        page: Int,
        query: String? = null,
        sort: String? = null,
        year: Int? = null
    ): List<Artist>
}