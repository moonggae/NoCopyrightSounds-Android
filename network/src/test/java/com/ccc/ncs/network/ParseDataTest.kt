package com.ccc.ncs.network

import com.ccc.ncs.network.converter.NcsHtmlConverterFactory
import com.ccc.ncs.network.retrofit.RetrofitNcsNetworkApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class ParseDataTest {
    private lateinit var service: RetrofitNcsNetworkApi

    @Before
    fun setUp() {
        service = Retrofit.Builder()
            .baseUrl(BuildConfig.WEB_URL)
            .addConverterFactory(NcsHtmlConverterFactory())
            .build()
            .create(RetrofitNcsNetworkApi::class.java)
    }

    @Test
    fun `test parse MusicList`() = runTest {
        val musics = service.getMusicList(1).body()
        musics?.forEach {
            println(it)
        }
        assert(musics?.size == 20)
    }

    @Test
    fun `test parse ArtistList`() = runTest {
        val artist = service.getArtistList(1).body()
        artist?.forEach {
            println(it)
        }
        assert(artist?.size == 20)
    }

    @Test
    @Throws
    fun `test parse genre and mood`() = runTest {
        val (genres, moods) = service.getAllGenreAndMood().body() ?: throw Exception("Fail to get html")
        assert(
            genres.size > 10 &&
                    moods.size > 10
        )
    }

    @Test
    @Throws
    fun `test parse artist detail`() = runTest {
        val artistDetail = service.getArtistDetail("https://ncs.io/artist/1114/keepsake").body() ?: throw Exception("Fail to get html")

        assert(
            artistDetail.artist.run {
                name.isNotBlank() && tags.isNotBlank() && detailUrl.isNotBlank()
            } &&
            artistDetail.musics.isNotEmpty() &&
            artistDetail.similarArtists.isNotEmpty()
        )
    }
}