package com.ccc.ncs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ccc.ncs.data.paging.MUSIC_LOAD_SIZE
import com.ccc.ncs.data.paging.MusicPagingSource
import com.ccc.ncs.database.dao.GenreDao
import com.ccc.ncs.database.dao.MoodDao
import com.ccc.ncs.database.dao.MusicDao
import com.ccc.ncs.database.dao.PlayListDao
import com.ccc.ncs.database.model.asEntity
import com.ccc.ncs.database.model.asModel
import com.ccc.ncs.database.model.reference.MusicGenreCrossRef
import com.ccc.ncs.database.model.reference.MusicMoodCrossRef
import com.ccc.ncs.database.model.relation.MusicWithGenreAndMood
import com.ccc.ncs.database.model.relation.asModel
import com.ccc.ncs.database.model.toStatusString
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.MusicStatus
import com.ccc.ncs.network.NcsNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.util.UUID
import javax.inject.Inject

internal class DefaultMusicRepository @Inject constructor(
    private val network: NcsNetworkDataSource,
    private val genreDao: GenreDao,
    private val moodDao: MoodDao,
    private val musicDao: MusicDao,
    private val playListDao: PlayListDao,
    private val downloadDirectory: File?
): MusicRepository {
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
            syncLocalMusics = this::insertNotExistMusics,
            query = query,
            genreId = genreId,
            moodId = moodId,
            version = version
        )
    }.flow

    override fun getMusics(musicIds: List<UUID>): Flow<List<Music>> = musicDao
        .getMusics(musicIds)
        .map(List<MusicWithGenreAndMood>::asModel)

    override fun getMusic(musicId: UUID): Flow<Music?> = musicDao
        .getMusic(musicId)
        .map { it?.asModel() }

    override fun initGenreAndMood(): Flow<Boolean> = flow {
        val (genres, moods) = network.getAllGenreAndMood()
        val moodsRowIds = moodDao.insertAllMoods(moods.map { it.asEntity() })
        val genresRowIds = genreDao.insertAllGenres(genres.map { it.asEntity() })

        emit(moodsRowIds.isNotEmpty() && genresRowIds.isNotEmpty())
    }.catch {
        emit(false)
    }

    override fun insertMusics(musics: List<Music>): Flow<List<Music>> = flow {
        musicDao.insertMusics(musics.map { it.asEntity() })

        val musicGenreCrossRefs = mutableListOf<MusicGenreCrossRef>()
        val musicMoodCrossRefs = mutableListOf<MusicMoodCrossRef>()

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

    override fun getGenres(): Flow<List<Genre>> = genreDao
        .getAllGenres()
        .map { list -> list.map { it.asModel() } }

    override fun getMoods(): Flow<List<Mood>> = moodDao
        .getAllMoods()
        .map { list -> list.map { it.asModel() } }

    override suspend fun insertNotExistMusics(musics: List<Music>) {
        val localMusics = getMusics(musics.map(Music::id)).first()
        val localMusicsMap = localMusics.associateBy { it.id }

        val musicsToInsert = musics.filter { music ->
            val localMusic = localMusicsMap[music.id]
            localMusic == null
        }

        insertMusics(musicsToInsert).first()
    }

    override fun getMusicsByStatus(status: List<MusicStatus>): Flow<List<Music>> =
        musicDao.getMusicsByStatus(status.map(MusicStatus::toStatusString)).map(List<MusicWithGenreAndMood>::asModel)

    override suspend fun updateMusicStatus(musicId: UUID, status: MusicStatus) {
        getMusic(musicId).first()?.let { music ->
            if (music.status == status) return
            musicDao.updateMusic(
                music.copy(
                    status = status
                ).asEntity()
            )
        }
    }

    override suspend fun deleteMusic(musicId: UUID) {
        musicDao.getMusic(musicId).first()?.asModel()?.asEntity()?.let {
            playListDao.unLinkMusic(musicId)
            musicDao.deleteMusic(it)
        }
    }

    override suspend fun removeDownloadedMusic(vararg musicId: UUID) {
        musicId.forEach { id ->
            val musicFile = File(downloadDirectory, id.toString())
            if (musicFile.exists()) {
                musicFile.delete()
            }
            updateMusicStatus(id, MusicStatus.None)
        }
    }
}