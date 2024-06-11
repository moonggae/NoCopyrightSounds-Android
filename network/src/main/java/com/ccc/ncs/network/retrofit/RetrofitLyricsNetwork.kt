package com.ccc.ncs.network.retrofit

import com.ccc.ncs.network.BuildConfig
import com.ccc.ncs.network.LyricsNetworkDataSource
import com.ccc.ncs.network.converter.FandomHtmlConverterFactory
import okhttp3.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

private const val FANDOM_URL: String = BuildConfig.FANDOM_URL

@Singleton
class RetrofitLyricsNetwork @Inject constructor(
    okHttpCallFactory: Call.Factory
) : LyricsNetworkDataSource {
    private val networkApi by lazy {
        Retrofit.Builder()
            .baseUrl(FANDOM_URL)
            .addConverterFactory(FandomHtmlConverterFactory())
            .callFactory { okHttpCallFactory.newCall(it) }
            .build()
            .create(RetrofitLyricsNetworkApi::class.java)
    }
    override suspend fun getLyrics(title: String): String {
        val pathTitle = title
            .split("(").first().trim()
            .replace(" ", "_")

        return networkApi.getLyrics(pathTitle)
    }
}