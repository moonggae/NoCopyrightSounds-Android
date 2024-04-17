package com.ccc.ncs.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ccc.ncs.database.dao.GenreDao
import com.ccc.ncs.database.dao.MoodDao
import com.ccc.ncs.database.dao.MusicDao
import com.ccc.ncs.database.dao.PlayListDao
import com.ccc.ncs.database.model.GenreEntity
import com.ccc.ncs.database.model.MoodEntity
import com.ccc.ncs.database.model.MusicEntity
import com.ccc.ncs.database.model.PlayListEntity
import com.ccc.ncs.database.model.reference.MusicGenreCrossRef
import com.ccc.ncs.database.model.reference.MusicMoodCrossRef
import com.ccc.ncs.database.model.reference.PlayListMusicCrossRef

@Database(
    entities = [
        PlayListEntity::class,
        MusicEntity::class,
        GenreEntity::class,
        MoodEntity::class,
        MusicGenreCrossRef::class,
        MusicMoodCrossRef::class,
        PlayListMusicCrossRef::class
    ],
    version = 1
)
internal abstract class NcsDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun moodDao(): MoodDao
    abstract fun musicDao(): MusicDao
    abstract fun playListDao(): PlayListDao
}