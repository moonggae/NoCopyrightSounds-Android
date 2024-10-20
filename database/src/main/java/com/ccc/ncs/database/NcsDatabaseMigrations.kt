package com.ccc.ncs.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

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

val MIGRATION_3_5_AddMusicStatus = object: Migration(3, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE music ADD COLUMN localUri TEXT NULL DEFAULT NULL")
        db.execSQL("ALTER TABLE music ADD COLUMN status TEXT NOT NULL DEFAULT 'None'")
    }
}