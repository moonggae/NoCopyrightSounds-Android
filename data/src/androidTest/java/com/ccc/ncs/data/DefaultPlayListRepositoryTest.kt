package com.ccc.ncs.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ccc.ncs.data.repository.DefaultMusicRepository
import com.ccc.ncs.data.repository.DefaultPlayListRepository
import com.ccc.ncs.database.NcsDatabase
import com.ccc.ncs.database.test.mock.MockMusicList
import com.ccc.ncs.network.retrofit.RetrofitNcsNetwork
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import org.junit.Test

class DefaultPlayListRepositoryTest {
    private lateinit var repository: DefaultPlayListRepository
    private lateinit var musicRepository: DefaultMusicRepository

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var db: NcsDatabase

    @Before
    fun initRepository() {

        db = Room.inMemoryDatabaseBuilder(
            context,
            NcsDatabase::class.java
        )
//            .setQueryCallback(RoomDatabase.QueryCallback { sqlQuery, bindArgs ->
//                println("SQL Query: $sqlQuery")
//                println("SQL Args")
//                bindArgs.forEach {
//                    println(it.toString())
//                }
//            }, Executors.newSingleThreadExecutor())
            .build()



        val networkDataSource = RetrofitNcsNetwork(OkHttpClient.Builder().build())

        musicRepository = DefaultMusicRepository(
            network = networkDataSource,
            genreDao = db.genreDao(),
            moodDao = db.moodDao(),
            musicDao = db.musicDao()
        )

        repository = DefaultPlayListRepository(
            musicRepository = musicRepository,
            playListDao = db.playListDao()
        )
    }

    @After
    fun closeDb() = db.close()

    private fun insertPlayList() = repository.insertPlayList(TEST_PLAYLIST_NAME)

    @Test
    fun insert_play_list() = runTest {
        val insertedPlayList = insertPlayList().first()
        println(insertedPlayList)
        assert(insertedPlayList.name == TEST_PLAYLIST_NAME)
    }

    @Test
    fun set_play_list_music() = runTest {
        musicRepository.initGenreAndMood()

        val mockMusics = MockMusicList

        val insertedPlayList = insertPlayList().first()
        val musicInsertedPlayList = repository.setPlayListMusics(insertedPlayList.id, mockMusics).first()

        assert(
            insertedPlayList.id == musicInsertedPlayList.id &&
                    musicInsertedPlayList.musics == mockMusics
        )
    }

    @Test
    fun re_set_play_list_music() = runTest {
        musicRepository.initGenreAndMood()

        val mockMusics = MockMusicList

        val insertedPlayList = insertPlayList().first()
        val musicInsertedPlayList = repository.setPlayListMusics(insertedPlayList.id, mockMusics).first()
        val secondMockMusics = MockMusicList.reversed().take(2)
        val musicResetPlayList = repository.setPlayListMusics(insertedPlayList.id, secondMockMusics).first()

        assert(
            mockMusics == musicInsertedPlayList.musics &&
            secondMockMusics == musicResetPlayList.musics
        )
    }


    @Test
    fun update_play_list_name() = runTest {
        val insertedPlayList = insertPlayList().first()
        val updatedPlayList = repository.updatePlayListName(insertedPlayList.id, TEST_UPDATE_PLAYLIST_NAME).first()

        assert(
            insertedPlayList.id == updatedPlayList.id &&
            updatedPlayList.name == TEST_UPDATE_PLAYLIST_NAME
        )
    }

    @Test
    fun delete_play_list() = runTest {
        val insertedPlayList = insertPlayList().first()
        repository.deletePlayList(insertedPlayList.id)
        val deletedPlayList = repository.getPlayList(insertedPlayList.id).first()

        assert(deletedPlayList == null)
    }

    companion object {
        private const val TAG = "TestPlayListRepo"
    }
}

private const val TEST_PLAYLIST_NAME = "My Play List"
private const val TEST_UPDATE_PLAYLIST_NAME = "My Update Play List"
