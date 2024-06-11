package com.ccc.ncs.network.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitLyricsNetworkApi {
    @GET("/wiki/{title}")
    suspend fun getLyrics(@Path("title") title: String): String
}