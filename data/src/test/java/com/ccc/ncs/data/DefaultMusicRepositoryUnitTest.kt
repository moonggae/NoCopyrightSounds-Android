package com.ccc.ncs.data

import com.ccc.ncs.data.repository.DefaultMusicRepository
import com.ccc.ncs.data.testdouble.TestGenreDao
import com.ccc.ncs.data.testdouble.TestMoodDao
import com.ccc.ncs.data.testdouble.TestMusicDao
import com.ccc.ncs.data.testdouble.TestNcsNetworkDataSource
import com.ccc.ncs.data.testdouble.TestPlaylistDao
import com.ccc.ncs.database.model.asModel
import com.ccc.ncs.database.test.mock.MockGenreEntityList
import com.ccc.ncs.database.test.mock.MockMoodEntityList
import com.ccc.ncs.database.test.mock.MockMusicList
import com.ccc.ncs.model.MusicStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.io.path.createTempDirectory

class DefaultMusicRepositoryUnitTest {
    private lateinit var repository: DefaultMusicRepository
    private lateinit var musicDao: TestMusicDao
    private lateinit var playlistDao: TestPlaylistDao
    private lateinit var tempDir: File

    @Before
    fun initRepository() {
        tempDir = createTempDirectory().toFile()
        val genreDao = TestGenreDao()
        val moodDao = TestMoodDao()
        musicDao = TestMusicDao(genreDao, moodDao)
        playlistDao = TestPlaylistDao(musicDao)
        repository = DefaultMusicRepository(
            network = TestNcsNetworkDataSource(),
            genreDao = genreDao,
            moodDao = moodDao,
            musicDao = musicDao,
            playListDao = playlistDao,
            downloadDirectory = tempDir
        )
    }

    @After
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `test init mood and genre`() = runTest {
        // Given
        repository.initGenreAndMood().first()

        // When
        val insertedGenres = repository.getGenres().first()
        val insertedMoods = repository.getMoods().first()

        // Then
        assertTrue(
            MockGenreEntityList.map { it.asModel() }.sortedBy { it.id } == insertedGenres.sortedBy { it.id } &&
                    MockMoodEntityList.map { it.asModel() }.sortedBy { it.id } == insertedMoods.sortedBy { it.id }
        )
    }

    @Test
    fun `test insert musics`() = runTest {
        // Given
        repository.initGenreAndMood().first()

        // When
        val insertedMusics = repository.insertMusics(MockMusicList).first()

        // Then
        assertTrue(insertedMusics == MockMusicList)
    }

    @Test
    fun `test get musics by ids returns correct musics`() = runTest {
        // Given
        repository.initGenreAndMood().first()
        val insertedMusics = repository.insertMusics(MockMusicList).first()
        val targetMusics = insertedMusics.take(2)

        // When
        val result = repository.getMusics(targetMusics.map { it.id }).first()

        // Then
        assertEquals(targetMusics.sortedBy { it.id }, result.sortedBy { it.id })
    }

    @Test
    fun `test get single music returns correct music`() = runTest {
        // Given
        repository.initGenreAndMood().first()
        val insertedMusics = repository.insertMusics(MockMusicList).first()
        val targetMusic = insertedMusics.first()

        // When
        val result = repository.getMusic(targetMusic.id).first()

        // Then
        assertEquals(targetMusic, result)
    }

    @Test
    fun `test update music status updates correctly`() = runTest {
        // Given
        repository.initGenreAndMood().first()
        val music = repository.insertMusics(MockMusicList).first().first()
        val newStatus = MusicStatus.Downloaded("")

        // When
        repository.updateMusicStatus(music.id, newStatus)
        val updatedMusic = repository.getMusic(music.id).first()

        // Then
        assertEquals(newStatus, updatedMusic?.status)
    }

    @Test
    fun `test delete music`() = runTest {
        // Given
        repository.initGenreAndMood().first()
        val music = repository.insertMusics(MockMusicList).first().first()

        // When
        repository.deleteMusic(music.id)
        val deletedMusic = repository.getMusic(music.id).first()

        // Then
        assertNull(deletedMusic)
    }

    @Test
    fun `test insert not exist musics only inserts new musics`() = runTest {
        // Given
        repository.initGenreAndMood().first()
        val initialMusics = repository.insertMusics(MockMusicList.take(2)).first()
        val newMusics = MockMusicList.drop(2)

        // When
        repository.insertNotExistMusics(MockMusicList)
        val allMusics = repository.getMusics(MockMusicList.map { it.id }).first()

        // Then
        assertEquals(MockMusicList.size, allMusics.size)
        assertTrue(allMusics.containsAll(initialMusics))
    }

    @Test
    fun `test remove downloaded music updates status`() = runTest {
        // Given
        repository.initGenreAndMood().first()
        val music = repository.insertMusics(MockMusicList).first().first()
        val musicFile = File(tempDir, music.id.toString())
        musicFile.createNewFile()
        repository.updateMusicStatus(music.id, MusicStatus.Downloaded(musicFile.toURI().toString()))

        // When
        repository.removeDownloadedMusic(music.id)
        val updatedMusic = repository.getMusic(music.id).first()

        // Then
        assertEquals(MusicStatus.None, updatedMusic?.status)
        assertFalse(musicFile.exists())
    }
}