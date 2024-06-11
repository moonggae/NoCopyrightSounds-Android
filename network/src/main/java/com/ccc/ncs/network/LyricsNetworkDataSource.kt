package com.ccc.ncs.network

interface LyricsNetworkDataSource {
    suspend fun getLyrics(title: String): String
}