package com.ccc.ncs.network

import com.ccc.ncs.network.converter.NcsHtmlConverterFactory
import com.ccc.ncs.network.retrofit.RetrofitNcsNetworkApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Retrofit

class ParseDataTest {
    @Test
    fun `test parse MusicList`() = runTest {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.WEB_URL)
            .addConverterFactory(NcsHtmlConverterFactory())
            .build()

        val service = retrofit.create(RetrofitNcsNetworkApi::class.java)

        val musics = service.getMusicList(1).body()
        musics?.forEach {
            println(it)
        }
        assert(musics?.size == 20)
    }

    @Test
    fun `test parse ArtistList`() = runTest {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.WEB_URL)
            .addConverterFactory(NcsHtmlConverterFactory())
            .build()

        val service = retrofit.create(RetrofitNcsNetworkApi::class.java)

        val artist = service.getArtistList(1).body()
        artist?.forEach {
            println(it)
        }
        assert(artist?.size == 20)
    }
}