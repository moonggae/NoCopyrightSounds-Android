package com.ccc.ncs.data.testdouble

import com.ccc.ncs.database.dao.GenreDao
import com.ccc.ncs.database.dao.MoodDao
import com.ccc.ncs.database.dao.MusicDao
import com.ccc.ncs.database.model.MusicEntity
import com.ccc.ncs.database.model.reference.MusicGenreCrossRef
import com.ccc.ncs.database.model.reference.MusicMoodCrossRef
import com.ccc.ncs.database.model.relation.MusicWithGenreAndMood
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID

class TestMusicDao(
    private val genreDao: GenreDao,
    private val moodDao: MoodDao
) : MusicDao {
    private val entitiesStateFlow = MutableStateFlow(emptyList<MusicWithGenreAndMood>())

    override suspend fun insertMusic(music: MusicEntity) {
        entitiesStateFlow.update { currentList ->
            currentList.map {
                if (it.music.id == music.id) it.copy(music = music) else it
            }.let {
                if (it == currentList) it + MusicWithGenreAndMood(music = music, genres = setOf(), moods = setOf())
                else it
            }
        }
    }

    override suspend fun insertMusics(musics: List<MusicEntity>) {
        entitiesStateFlow.update { currentList ->
            musics.fold(currentList) { acc, music ->
                acc.map {
                    if (it.music.id == music.id) it.copy(music = music) else it
                }.let {
                    if (it.any { item -> item.music.id == music.id }) it
                    else it + MusicWithGenreAndMood(music = music, genres = setOf(), moods = setOf())
                }
            }
        }
    }

    override suspend fun linkMusicToGenre(crossRef: List<MusicGenreCrossRef>) {
        val genres = genreDao.getAllGenres().first()
        val musicToGenres = crossRef.groupBy { it.musicId }.mapValues { (_, refs) ->
            refs.mapNotNull { ref ->
                genres.find { ref.genreId == it.id }
            }.toSet()
        }

        entitiesStateFlow.update { currentList ->
            currentList.map { music ->
                musicToGenres[music.music.id]?.let {
                    music.copy(genres = it)
                } ?: music
            }
        }
    }

    override suspend fun linkMusicToMood(crossRef: List<MusicMoodCrossRef>) {
        val moods = moodDao.getAllMoods().first().associateBy { it.id }
        val musicToMoods = crossRef.groupBy { it.musicId }.mapValues { (_, refs) ->
            refs.mapNotNull { moods[it.moodId] }.toSet()
        }

        entitiesStateFlow.update { currentList ->
            currentList.map { music ->
                musicToMoods[music.music.id]?.let {
                    music.copy(moods = it)
                } ?: music
            }
        }
    }

    override fun getMusic(id: UUID): Flow<MusicWithGenreAndMood?> = entitiesStateFlow.map { entities ->
        entities.find { it.music.id == id }
    }

    override fun getMusics(ids: List<UUID>): Flow<List<MusicWithGenreAndMood>> = entitiesStateFlow.map { entities ->
        entities.filter { entity -> entity.music.id in ids }
    }

    override fun getMusicsByStatus(status: List<String>): Flow<List<MusicWithGenreAndMood>> = entitiesStateFlow.map { entities ->
        entities.filter { entity -> entity.music.status in status }
    }

    override fun getDownloadingMusics(): Flow<List<MusicWithGenreAndMood>> = entitiesStateFlow.map { entities ->
        entities.filter { entity -> entity.music.status == "Downloading" }
    }

    override fun getDownloadedMusics(): Flow<List<MusicWithGenreAndMood>> = entitiesStateFlow.map { entities ->
        entities.filter { entity -> entity.music.status == "Downloaded" }
    }

    override suspend fun updateMusic(music: MusicEntity) {
        entitiesStateFlow.update { currentList ->
            currentList.map { entity ->
                if (entity.music.id == music.id) {
                    entity.copy(music = music)
                } else {
                    entity
                }
            }
        }
    }

    override suspend fun deleteMusic(vararg musics: MusicEntity) {
        entitiesStateFlow.update { currentList ->
            currentList.filterNot { entity ->
                musics.any { music -> music.id == entity.music.id }
            }
        }
    }
}