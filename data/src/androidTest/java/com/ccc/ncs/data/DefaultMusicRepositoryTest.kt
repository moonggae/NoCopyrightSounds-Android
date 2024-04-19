package com.ccc.ncs.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ccc.ncs.data.repository.DefaultMusicRepository
import com.ccc.ncs.database.NcsDatabase
import com.ccc.ncs.database.test.mock.MockMusicList
import com.ccc.ncs.network.retrofit.RetrofitNcsNetwork
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import org.junit.Test

class DefaultMusicRepositoryTest {
    private lateinit var repository: DefaultMusicRepository

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var db: NcsDatabase

    @Before
    fun initRepository() {
        db = Room.inMemoryDatabaseBuilder(
            context,
            NcsDatabase::class.java
        ).build()

        val networkDataSource = RetrofitNcsNetwork(OkHttpClient.Builder().build())

        repository = DefaultMusicRepository(
            network = networkDataSource,
            genreDao = db.genreDao(),
            moodDao = db.moodDao(),
            musicDao = db.musicDao()
        )
    }

    @After
    fun closeDb() = db.close()

    @Test
    fun test_init_mood_and_genre() = runTest {
        repository.initGenreAndMood()
        val insertedGenres = repository.getGenres().first()
        val insertedMoods = repository.getMoods().first()

        println(insertedGenres)
        println(insertedMoods)

        assert(insertedGenres.size > 10 && insertedMoods.size > 10)
    }

    @Test
    fun test_insert_musics() = runTest {
        repository.initGenreAndMood()
        val insertedMusics = repository.insertMusics(MockMusicList).first()
        println(insertedMusics)
        println(MockMusicList)
        assert(insertedMusics == MockMusicList)
    }
}