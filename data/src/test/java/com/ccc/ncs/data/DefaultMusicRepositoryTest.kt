package com.ccc.ncs.data

import com.ccc.ncs.data.repository.DefaultMusicRepository
import com.ccc.ncs.data.testdouble.TestGenreDao
import com.ccc.ncs.data.testdouble.TestMoodDao
import com.ccc.ncs.data.testdouble.TestMusicDao
import com.ccc.ncs.data.testdouble.TestNcsNetworkDataSource
import com.ccc.ncs.database.model.asModel
import com.ccc.ncs.database.test.mock.MockGenreEntityList
import com.ccc.ncs.database.test.mock.MockMoodEntityList
import com.ccc.ncs.database.test.mock.MockMusicList
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DefaultMusicRepositoryTest {
    private lateinit var repository: DefaultMusicRepository

    @Before
    fun initRepository() {
        val genreDao = TestGenreDao()
        val moodDao = TestMoodDao()
        repository = DefaultMusicRepository(
            network = TestNcsNetworkDataSource(),
            genreDao = genreDao,
            moodDao = moodDao,
            musicDao = TestMusicDao(
                genreDao = genreDao,
                moodDao = moodDao
            )
        )
    }

    @Test
    fun `test init mood and genre`() = runTest {
        repository.initGenreAndMood()
        val insertedGenres = repository.getGenres().first()
        val insertedMoods = repository.getMoods().first()

        assert(
            MockGenreEntityList.map { it.asModel() }.sortedBy { it.id } == insertedGenres.sortedBy { it.id } &&
                    MockMoodEntityList.map { it.asModel() }.sortedBy { it.id } == insertedMoods.sortedBy { it.id }
        )
    }

    @Test
    fun `test insert musics`() = runTest {
        repository.initGenreAndMood()
        val insertedMusics = repository.insertMusics(MockMusicList).first()
        println(insertedMusics)
        println(MockMusicList)
        assert(insertedMusics == MockMusicList)
    }
}