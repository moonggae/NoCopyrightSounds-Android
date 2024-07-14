package com.ccc.ncs.network.retrofit

import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.ArtistDetail
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface RetrofitNcsNetworkApi {
    @GET("/music-search")
    suspend fun getMusicList(
        @Query("page") page: Int,
        @Query("q") query: String? = null,
        @Query("genre") genreId: Int? = null,
        @Query("mood") moodId: Int? = null,
        @Query("version") version: String? = null,
        @Query("display") display: String = "list"
    ): Response<List<Music>>

    @GET("/artists")
    suspend fun getArtistList(
        @Query("page") page: Int,
        @Query("q") query: String? = null,
        @Query("sort") sort: String? = null,
        @Query("year") year: Int? = null
    ): Response<List<Artist>>

    @GET("/music-search")
    suspend fun getAllGenreAndMood(
        @Query("page") page: Int = 1,
        @Query("display") display: String = "list"
    ): Response<Pair<List<Genre>, List<Mood>>>

    @GET
    suspend fun getArtistDetail(
        @Url url: String
    ): Response<ArtistDetail?>
}