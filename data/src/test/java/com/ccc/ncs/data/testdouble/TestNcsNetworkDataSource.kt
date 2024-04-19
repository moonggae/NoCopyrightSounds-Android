package com.ccc.ncs.data.testdouble


import com.ccc.ncs.data.mock.MockGenreEntityList
import com.ccc.ncs.data.mock.MockMoodEntityList
import com.ccc.ncs.data.mock.MockMusicList
import com.ccc.ncs.database.model.asModel
import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import com.ccc.ncs.network.NcsNetworkDataSource

class TestNcsNetworkDataSource : NcsNetworkDataSource {
    override suspend fun getMusics(page: Int, query: String?, genreId: Int?, moodId: Int?, version: String?): List<Music> = MockMusicList

    override suspend fun getArtists(page: Int, query: String?, sort: String?, year: Int?): List<Artist> = emptyList()

    override suspend fun getAllGenreAndMood(): Pair<List<Genre>, List<Mood>> =
        Pair(MockGenreEntityList.map { it.asModel() }, MockMoodEntityList.map { it.asModel() })
}