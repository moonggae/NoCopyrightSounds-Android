package com.ccc.ncs.network

import com.ccc.ncs.network.converter.NcsHtmlConverterFactory
import com.ccc.ncs.network.retrofit.RetrofitNcsNetworkApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.io.File

class ConverterTest {
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }


    @Test
    fun `test MusicListConverter`() = runTest {
        val htmlFile = File(javaClass.classLoader!!.getResource("music_page_1.html").toURI())

        server.enqueue(
            MockResponse()
                .setBody(htmlFile.readText())
                .addHeader("Content-Type", "text/html")
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
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
    fun `test ArtistListConverter`() = runTest {
        val htmlFile = File(javaClass.classLoader!!.getResource("artist_page_1.html").toURI())

        server.enqueue(
            MockResponse()
                .setBody(htmlFile.readText())
                .addHeader("Content-Type", "text/html")
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(NcsHtmlConverterFactory())
            .build()


        val service = retrofit.create(RetrofitNcsNetworkApi::class.java)

        val artist = service.getArtistList(1).body()
        assert(artist?.size == 20)
    }

    @Test
    @Throws
    fun `test genre, mood converter`() = runTest {
        val htmlFile = File(javaClass.classLoader!!.getResource("music_page_1.html").toURI())

        server.enqueue(
            MockResponse()
                .setBody(htmlFile.readText())
                .addHeader("Content-Type", "text/html")
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(NcsHtmlConverterFactory())
            .build()


        val service = retrofit.create(RetrofitNcsNetworkApi::class.java)

        val (genre, mood) = service.getAllGenreAndMood().body() ?: throw Exception("Fail to parse")
        assert(
            genre.size > 10 &&
            mood.size > 10
        )
    }
}
