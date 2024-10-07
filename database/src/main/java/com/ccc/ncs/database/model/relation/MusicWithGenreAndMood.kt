package com.ccc.ncs.database.model.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.ccc.ncs.database.model.GenreEntity
import com.ccc.ncs.database.model.MoodEntity
import com.ccc.ncs.database.model.MusicEntity
import com.ccc.ncs.database.model.asModel
import com.ccc.ncs.database.model.reference.MusicGenreCrossRef
import com.ccc.ncs.database.model.reference.MusicMoodCrossRef

data class MusicWithGenreAndMood(
    @Embedded val music: MusicEntity,

    @Relation(
        parentColumn = "id",
        entity = GenreEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = MusicGenreCrossRef::class,
            parentColumn = "musicId",
            entityColumn = "genreId"
        )
    )
    val genres: Set<GenreEntity>,

    @Relation(
        parentColumn = "id",
        entity = MoodEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = MusicMoodCrossRef::class,
            parentColumn = "musicId",
            entityColumn = "moodId"
        )
    )
    val moods: Set<MoodEntity>
)

fun MusicWithGenreAndMood.asModel() = music.asModel(
    genres.map { it.asModel() }.toSet(),
    moods.map { it.asModel() }.toSet()
)

fun List<MusicWithGenreAndMood>.asModel() = this.map { it.asModel() }