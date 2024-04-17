package com.ccc.ncs.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ccc.ncs.database.dao.GenreDao
import com.ccc.ncs.database.dao.MoodDao
import com.ccc.ncs.database.dao.MusicDao
import com.ccc.ncs.database.dao.PlayListDao
import com.ccc.ncs.database.mock.MockGenreList
import com.ccc.ncs.database.mock.MockMoodList
import com.ccc.ncs.database.mock.MockMusic
import com.ccc.ncs.database.mock.MockMusicWithGenreAndMood
import com.ccc.ncs.database.mock.MockPlayList
import com.ccc.ncs.database.mock.MockPlayListWithMusics
import com.ccc.ncs.database.model.reference.MusicGenreCrossRef
import com.ccc.ncs.database.model.reference.MusicMoodCrossRef
import com.ccc.ncs.database.model.reference.PlayListMusicCrossRef
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class DaoTest {
    private lateinit var moodDao: MoodDao
    private lateinit var genreDao: GenreDao
    private lateinit var musicDao: MusicDao
    private lateinit var playListDao: PlayListDao
    private lateinit var db: NcsDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            NcsDatabase::class.java
        ).build()
        moodDao = db.moodDao()
        genreDao = db.genreDao()
        musicDao = db.musicDao()
        playListDao = db.playListDao()
    }

    @After
    fun closeDb() = db.close()

    private suspend fun insertMockGenres() = genreDao.insertAllGenres(MockGenreList)
    private suspend fun insertMockMoods() = moodDao.insertAllMoods(MockMoodList)
    private suspend fun insertMockMusic() {
        insertMockGenres()
        insertMockMoods()

        MockMusicWithGenreAndMood.run {
            musicDao.insertMusic(music)
            musicDao.linkMusicToGenre(genres.map {
                MusicGenreCrossRef(
                    musicId = music.id,
                    genreId = it.id
                )
            })
            musicDao.linkMusicToMood(moods.map {
                MusicMoodCrossRef(
                    musicId = music.id,
                    moodId = it.id
                )
            })
        }
    }

    private suspend fun insertMockPlayList() {
        insertMockMusic()
        playListDao.insertPlayList(MockPlayList)
        playListDao.linkMusicToPlayList(listOf(
            PlayListMusicCrossRef(
                playListId = MockPlayList.id,
                musicId = MockMusic.id
            )
        ))
    }

    @Test
    fun genreDao_insert_and_fetch() = runTest {
        insertMockGenres()
        val insertedGenres = genreDao.getAllGenres().first()
        assert(insertedGenres == MockGenreList)
    }

    @Test
    fun genreDao_delete_all() = runTest {
        insertMockGenres()
        genreDao.deleteAllGenres()
        val insertedGenres = genreDao.getAllGenres().first()
        assert(insertedGenres.isEmpty())
    }

    @Test
    fun moodDao_insert_and_fetch() = runTest {
        insertMockMoods()
        val insertedMoods = moodDao.getAllMoods().first()
        assert(insertedMoods == MockMoodList)
    }

    @Test
    fun moodDao_delete_all() = runTest {
        insertMockMoods()
        moodDao.deleteAllMoods()
        val insertedMoods = moodDao.getAllMoods().first()
        assert(insertedMoods.isEmpty())
    }

    @Test
    fun musicDao_insert_and_fetch() = runTest {
        insertMockMusic()

        val insertedMusicWithGenreAndMood = musicDao.getMusic(MockMusicWithGenreAndMood.music.id).first()

        Log.d("TAG", "$insertedMusicWithGenreAndMood")

        assert(insertedMusicWithGenreAndMood == MockMusicWithGenreAndMood)
    }

    @Test
    fun playListDao_insert_and_fetch() = runTest {
        insertMockPlayList()

        val insertedPlayListWithMusics = playListDao.getPlayList(MockPlayList.id).first()
        assert(insertedPlayListWithMusics == MockPlayListWithMusics)
    }

    @Test
    @Throws
    fun playListDao_unlink_all() = runTest {
        insertMockPlayList()

        playListDao.unLinkAllMusic(MockPlayList.id)
        val insertedPlayListWithMusics = playListDao.getPlayList(MockPlayList.id).first() ?: throw Exception("Fail to get")
        assert(insertedPlayListWithMusics.musics.isEmpty())
    }

    @Test
    @Throws
    fun playListDao_update_name() = runTest {
        val changedName = "Test Changed Test"
        insertMockPlayList()

        playListDao.updatePlayList(MockPlayList.copy(name = changedName))
        val nameChangedEntity = playListDao.getPlayList(MockPlayList.id).first() ?: throw Exception("Fail to get")
        assert(nameChangedEntity.playList.name == changedName)
    }

    @Test
    @Throws
    fun playListDao_delete() = runTest {
        insertMockPlayList()

        val insertedPlayListWithMusics = playListDao.getPlayList(MockPlayList.id).first() ?: throw Exception("Fail to get")
        playListDao.deletePlayList(insertedPlayListWithMusics.playList)

        val deletedEntity = playListDao.getPlayList(insertedPlayListWithMusics.playList.id).first()
        assert(deletedEntity == null)
    }

}