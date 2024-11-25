package com.ccc.ncs.domain.repository

import com.ccc.ncs.model.ArtistDetail
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {
    fun getArtistDetail(artistDetailPath: String): Flow<Result<ArtistDetail>>
}