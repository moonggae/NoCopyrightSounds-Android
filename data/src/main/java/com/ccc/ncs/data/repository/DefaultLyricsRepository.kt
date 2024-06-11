package com.ccc.ncs.data.repository

import com.ccc.ncs.network.LyricsNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultLyricsRepository @Inject constructor(
    private val networkDataSource: LyricsNetworkDataSource
) : LyricsRepository {
    override fun getLyrics(title: String): Flow<String?> = flow {
        emit(networkDataSource.getLyrics(title).takeIf { it.isNotBlank() })
    }
}