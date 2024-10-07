package com.ccc.ncs.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ccc.ncs.database.dao.GenreDao
import com.ccc.ncs.database.dao.MoodDao
import com.ccc.ncs.database.dao.MusicDao
import com.ccc.ncs.database.dao.PlayListDao
import com.ccc.ncs.database.dao.RecentSearchQueryDao
import com.ccc.ncs.database.model.GenreEntity
import com.ccc.ncs.database.model.MoodEntity
import com.ccc.ncs.database.model.MusicEntity
import com.ccc.ncs.database.model.PlayListEntity
import com.ccc.ncs.database.model.RecentSearchQueryEntity
import com.ccc.ncs.database.model.reference.MusicGenreCrossRef
import com.ccc.ncs.database.model.reference.MusicMoodCrossRef
import com.ccc.ncs.database.model.reference.PlayListMusicCrossRef
import com.ccc.ncs.database.util.ArtistConverter
import com.ccc.ncs.database.util.InstantConverter

@Database(
    entities = [
        PlayListEntity::class,
        MusicEntity::class,
        GenreEntity::class,
        MoodEntity::class,
        MusicGenreCrossRef::class,
        MusicMoodCrossRef::class,
        PlayListMusicCrossRef::class,
        RecentSearchQueryEntity::class
    ],
    version = 5
)
@TypeConverters(
    InstantConverter::class,
    ArtistConverter::class
)
abstract class NcsDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun moodDao(): MoodDao
    abstract fun musicDao(): MusicDao
    abstract fun playListDao(): PlayListDao
    abstract fun recentSearchQueryDao(): RecentSearchQueryDao
}

val MIGRATION_1_2_AddIsUserCreatedPlaylistColumn = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE play_list ADD COLUMN isUserCreated INTEGER NOT NULL DEFAULT 1")
    }
}

val MIGRATION_2_3_AddArtistToMusic = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE music DROP COLUMN artist")
        db.execSQL("ALTER TABLE music DROP COLUMN artistDetailUrl")
        db.execSQL("ALTER TABLE music ADD COLUMN artists TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_3_4_AddMusicDownload = object: Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE music ADD COLUMN isDownloading INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE music ADD COLUMN localUri TEXT NULL DEFAULT NULL")
    }
}

val MIGRATION_4_5_AddMusicStatus = object: Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE music DROP COLUMN isDownloading")
        db.execSQL("ALTER TABLE music ADD COLUMN status TEXT NOT NULL DEFAULT 'None'")
    }
}