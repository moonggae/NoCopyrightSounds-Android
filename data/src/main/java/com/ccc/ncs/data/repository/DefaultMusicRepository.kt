package com.ccc.ncs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ccc.ncs.data.paging.MUSIC_LOAD_SIZE
import com.ccc.ncs.data.paging.MusicPagingSource
import com.ccc.ncs.database.dao.GenreDao
import com.ccc.ncs.database.dao.MoodDao
import com.ccc.ncs.database.dao.MusicDao
import com.ccc.ncs.database.model.asEntity
import com.ccc.ncs.database.model.asModel
import com.ccc.ncs.database.model.reference.MusicGenreCrossRef
import com.ccc.ncs.database.model.reference.MusicMoodCrossRef
import com.ccc.ncs.database.model.relation.MusicWithGenreAndMood
import com.ccc.ncs.database.model.relation.asModel
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import com.ccc.ncs.network.NcsNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

internal class DefaultMusicRepository @Inject constructor(
    private val network: NcsNetworkDataSource,
    private val genreDao: GenreDao,
    private val moodDao: MoodDao,
    private val musicDao: MusicDao
) : MusicRepository {
    override fun getSearchResultStream(
        query: String?,
        genreId: Int?,
        moodId: Int?,
        version: String?
    ): Flow<PagingData<Music>> = Pager(
        PagingConfig(
            pageSize = MUSIC_LOAD_SIZE,
            enablePlaceholders = false,
            prefetchDistance = MUSIC_LOAD_SIZE * 2
        )
    ) {
        MusicPagingSource(
            dataSource = network,
            syncLocalMusics = this::syncLocalMusics,
            query = query,
            genreId = genreId,
            moodId = moodId,
            version = version
        )
    }.flow

    override fun getMusics(musicIds: List<UUID>): Flow<List<Music>> = musicDao
        .getMusics(musicIds)
        .map {
            it.map { musicWithGenreAndMood ->
                musicWithGenreAndMood.asModel()
            }
        }

    override fun getMusic(musicId: UUID): Flow<Music?> = musicDao
        .getMusic(musicId)
        .map { it?.asModel() }

    override suspend fun initGenreAndMood() {
        val (genres, moods) = network.getAllGenreAndMood()
        moodDao.deleteAllMoods()
        moodDao.insertAllMoods(moods.map { it.asEntity() })

        genreDao.deleteAllGenres()
        genreDao.insertAllGenres(genres.map { it.asEntity() })
    }

    override fun insertMusics(musics: List<Music>): Flow<List<Music>> = flow {
        musicDao.insertMusics(musics.map { it.asEntity() })

        val musicGenreCrossRefs = mutableListOf<MusicGenreCrossRef>()
        val musicMoodCrossRefs = mutableListOf<MusicMoodCrossRef>()

        overrideMoodAndGenreColors(musics)

        musics.forEach { music ->
            music.genres.mapTo(musicGenreCrossRefs) { genre ->
                MusicGenreCrossRef(
                    musicId = music.id,
                    genreId = genre.id
                )
            }

            music.moods.mapTo(musicMoodCrossRefs) { mood ->
                MusicMoodCrossRef(
                    musicId = music.id,
                    moodId = mood.id
                )
            }
        }

        musicDao.linkMusicToGenre(musicGenreCrossRefs)
        musicDao.linkMusicToMood(musicMoodCrossRefs)

        val insertedMusics = musicDao
            .getMusics(musics.map(Music::id))
            .map { it.map(MusicWithGenreAndMood::asModel) }
            .first()

        emit(insertedMusics)
    }

    private suspend fun overrideMoodAndGenreColors(musics: List<Music>) {
        val moods = musics.map { it.moods }.flatten().distinctBy { it.id }
        val genres = musics.map { it.genres }.flatten().distinctBy { it.id }

        moodDao.insertAllMoods(moods.map { it.asEntity() })
        genreDao.insertAllGenres(genres.map { it.asEntity() })
    }

    override fun getGenres(): Flow<List<Genre>> = genreDao
        .getAllGenres()
        .map { list -> list.map { it.asModel() } }

    override fun getMoods(): Flow<List<Mood>> = moodDao
        .getAllMoods()
        .map { list -> list.map { it.asModel() } }

    override suspend fun syncLocalMusics(musics: List<Music>) {
        val localMusics = getMusics(musics.map(Music::id)).first()
        val localMusicsMap = localMusics.associateBy { it.id }

        val musicsToInsert = musics.filter { music ->
            val localMusic = localMusicsMap[music.id]
            localMusic == null || localMusic != music
        }

        insertMusics(musicsToInsert).first()
    }
}