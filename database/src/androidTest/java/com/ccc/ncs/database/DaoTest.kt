package com.ccc.ncs.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ccc.ncs.database.dao.GenreDao
import com.ccc.ncs.database.dao.MoodDao
import com.ccc.ncs.database.dao.MusicDao
import com.ccc.ncs.database.dao.PlayListDao
import com.ccc.ncs.database.model.MusicEntity
import com.ccc.ncs.database.model.asEntity
import com.ccc.ncs.database.model.reference.MusicGenreCrossRef
import com.ccc.ncs.database.model.reference.MusicMoodCrossRef
import com.ccc.ncs.database.model.reference.PlayListMusicCrossRef
import com.ccc.ncs.database.model.relation.PlayListWithMusics
import com.ccc.ncs.database.test.mock.MockGenreEntityList
import com.ccc.ncs.database.test.mock.MockMoodEntityList
import com.ccc.ncs.database.test.mock.MockMusicList
import com.ccc.ncs.database.test.mock.MockMusicWithGenreAndMoodList
import com.ccc.ncs.database.test.mock.MockPlayList
import com.ccc.ncs.database.util.ArtistConverter
import com.ccc.ncs.model.Music
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DaoTest {
    private lateinit var moodDao: MoodDao
    private lateinit var genreDao: GenreDao
    private lateinit var musicDao: MusicDao
    private lateinit var playListDao: PlayListDao
    private lateinit var db: NcsDatabase

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val testDbName = "test-database"

    @Before
    fun createDb() {
        db = Room.databaseBuilder(
            context,
            NcsDatabase::class.java,
            testDbName
        )
            .addTypeConverter(ArtistConverter(Gson()))
            .build()
        moodDao = db.moodDao()
        genreDao = db.genreDao()
        musicDao = db.musicDao()
        playListDao = db.playListDao()
    }

    @After
    fun closeDb() {
        context.deleteFile(testDbName)
        db.close()
    }

    private suspend fun insertMockGenres() = genreDao.insertAllGenres(MockGenreEntityList)
    private suspend fun insertMockMoods() = moodDao.insertAllMoods(MockMoodEntityList)
    private suspend fun insertMockMusic() {
        insertMockGenres()
        insertMockMoods()

        MockMusicWithGenreAndMoodList[0].run {
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

    private suspend fun insertMockMusicList() {
        insertMockGenres()
        insertMockMoods()

        val musicEntities = mutableListOf<MusicEntity>()
        val genreCrossRefs = mutableListOf<MusicGenreCrossRef>()
        val moodCrossRefs = mutableListOf<MusicMoodCrossRef>()

        MockMusicList.forEach { music ->
            musicEntities.add(music.asEntity())
            genreCrossRefs.addAll(music.genres.map {
                MusicGenreCrossRef(
                    musicId = music.id,
                    genreId = it.id
                )
            })
            moodCrossRefs.addAll(music.moods.map {
                MusicMoodCrossRef(
                    musicId = music.id,
                    moodId = it.id
                )
            })
        }

        musicDao.insertMusics(MockMusicList.map { it.asEntity() })
        musicDao.linkMusicToGenre(genreCrossRefs)
        musicDao.linkMusicToMood(moodCrossRefs)
    }

    private suspend fun insertMockPlayList(): PlayListWithMusics {
        insertMockMusic()
        val rowId = playListDao.insertPlayList(MockPlayList)
        playListDao.linkMusicToPlayList(listOf(
            PlayListMusicCrossRef(
                playListId = MockPlayList.id,
                musicId = MockMusicList[0].id
            )
        ))

        val insertedPlayList = playListDao.getPlayListByRowId(rowId).first() ?: throw Exception("fail to insert play list")

        println("rowId: $rowId")
        println(insertedPlayList)

        return insertedPlayList
    }

    @Test
    fun genreDao_insert_and_fetch() = runTest {
        insertMockGenres()
        val insertedGenres = genreDao.getAllGenres().first()
        assert(insertedGenres == MockGenreEntityList)
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
        assert(insertedMoods == MockMoodEntityList)
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

        val insertedMusicWithGenreAndMood = musicDao.getMusic(MockMusicWithGenreAndMoodList[0].music.id).first()

        Log.d("TAG", "$insertedMusicWithGenreAndMood")

        assert(insertedMusicWithGenreAndMood == MockMusicWithGenreAndMoodList[0])
    }

    @Test
    fun musicDao_insert_music_list() = runTest {
        insertMockMusicList()
        val musics = musicDao.getMusics(MockMusicList.map { it.id }).first()
        musics.forEachIndexed { index, _ ->
            println(musics[index])
            println(MockMusicWithGenreAndMoodList[index])
        }
        assert(musics == MockMusicWithGenreAndMoodList)
    }

    @Test
    fun playListDao_insert_and_fetch() = runTest {
        val insertedPlayList = insertMockPlayList()
        assert(insertedPlayList.playList == MockPlayList)
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
        playListDao.deletePlayList(insertedPlayListWithMusics.playList.id)

        val deletedEntity = playListDao.getPlayList(insertedPlayListWithMusics.playList.id).first()
        assert(deletedEntity == null)
    }


    private val someDifferenceMockMusics = MockMusicList.mapIndexed { index, music ->
        if (index % 7 == 0) {
            music.copy(
                artists = music.artists.map { artist ->
                    artist.copy(
                        name = "update ${artist.name}"
                    )
                }
            )
        } else music
    }

    @Test
    fun measure_time_to_replace_all_musics() = runTest {
        insertMockMusicList()

        val time = measureTimeMillis {
            musicDao.insertMusics(someDifferenceMockMusics.map(Music::asEntity))
        }


        // 검증 로직 추가
        val updatedMusics = musicDao.getMusics(MockMusicList.map(Music::id)).first()

        // 모든 음악이 업데이트되었는지 확인
        assertEquals(someDifferenceMockMusics.size, updatedMusics.size)

        // 각 음악이 예상대로 업데이트되었는지 확인
        updatedMusics.forEach { musicWithData ->
            val expectedMusic = someDifferenceMockMusics.find { it.id == musicWithData.music.id }
                ?.asEntity()
            assertEquals(expectedMusic, musicWithData.music)
        }

        println("replace all music time: $time")
    }

    @Test
    fun measure_time_to_replace_difference_musics() = runTest {
        insertMockMusicList()

        val time = measureTimeMillis {
            val insertedMusics = musicDao.getMusics(MockMusicList.map(Music::id))
                .first()
                .associate { it.music.id to it.music }

            someDifferenceMockMusics
                .map(Music::asEntity)
                .filter { newMusic ->
                    val oldMusic = insertedMusics[newMusic.id]
                    oldMusic != newMusic
                }
                .forEach { musicDao.updateMusic(it) }
        }

        // 검증 로직 추가
        val updatedMusics = musicDao.getMusics(MockMusicList.map(Music::id)).first()
        updatedMusics.forEach { musicWithData ->
            val expectedMusic = someDifferenceMockMusics.find { it.id == musicWithData.music.id }
                ?.asEntity()
            assertEquals(expectedMusic, musicWithData.music)
        }

        println("replace difference item time: $time")
    }

    private inline fun measureTimeMillis(block: () -> Unit): Long {
        val start = System.nanoTime()
        block()
        val end = System.nanoTime()
        return (end - start) / 1_000_000
    }
}