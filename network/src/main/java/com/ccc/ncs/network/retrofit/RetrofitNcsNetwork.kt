package com.ccc.ncs.network.retrofit


import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.ArtistDetail
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import com.ccc.ncs.network.BuildConfig
import com.ccc.ncs.network.NcsNetworkDataSource
import com.ccc.ncs.network.converter.NcsHtmlConverterFactory
import com.ccc.ncs.network.util.joinUri
import okhttp3.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


private const val WEB_URL = BuildConfig.WEB_URL

@Singleton
class RetrofitNcsNetwork @Inject constructor(
    okHttpCallFactory: Call.Factory
) : NcsNetworkDataSource {
    private val networkApi by lazy {
        Retrofit.Builder()
            .baseUrl(WEB_URL)
            .addConverterFactory(NcsHtmlConverterFactory())
            .callFactory { okHttpCallFactory.newCall(it) }
            .build()
            .create(RetrofitNcsNetworkApi::class.java)
    }

    override suspend fun getMusics(
        page: Int,
        query: String?,
        genreId: Int?,
        moodId: Int?,
        version: String?
    ): List<Music> = networkApi.getMusicList(page, query, genreId, moodId, version).body()
        ?.filter {
            it.dataUrl.isNotBlank()
        }
        ?: emptyList()

    override suspend fun getArtists(
        page: Int,
        query: String?,
        sort: String?,
        year: Int?
    ): List<Artist> = networkApi.getArtistList(page, query, sort, year).body() ?: emptyList()

    override suspend fun getArtistDetail(path: String): ArtistDetail? =
        networkApi.getArtistDetail(joinUri(WEB_URL, *path.split("/").toTypedArray()).toString()).body()

    override suspend fun getAllGenreAndMood(): Pair<List<Genre>, List<Mood>> =
        networkApi.getAllGenreAndMood().body() ?: Pair(emptyList(), emptyList())
}