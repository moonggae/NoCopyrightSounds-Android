package com.ccc.ncs.data.repository

import android.util.Log
import com.ccc.ncs.domain.repository.ArtistRepository
import com.ccc.ncs.domain.repository.MusicRepository
import com.ccc.ncs.model.ArtistDetail
import com.ccc.ncs.network.NcsNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class DefaultArtistRepository @Inject constructor(
    private val network: NcsNetworkDataSource,
    private val musicRepository: MusicRepository
) : ArtistRepository {
    override fun getArtistDetail(artistDetailPath: String): Flow<Result<ArtistDetail>> = flow {
        val artistDetail = network.getArtistDetail(artistDetailPath)
        artistDetail?.musics?.let { musicRepository.insertMusics(it) }
        emit(Result.success(artistDetail ?: throw Exception("fail to get artist detail")))
    }.catch {
        Log.e(TAG, "getArtistDetail", it)
        emit(Result.failure(it))
    }

    companion object {
        private const val TAG = "DefaultArtistRepository"
    }
}