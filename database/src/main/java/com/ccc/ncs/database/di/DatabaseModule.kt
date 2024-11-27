package com.ccc.ncs.database.di

import android.content.Context
import androidx.room.Room
import com.ccc.ncs.database.MIGRATION_1_2_AddIsUserCreatedPlaylistColumn
import com.ccc.ncs.database.MIGRATION_2_3_AddArtistToMusic
import com.ccc.ncs.database.MIGRATION_3_5_AddMusicStatus
import com.ccc.ncs.database.MIGRATION_5_6_AddMapTableIndex
import com.ccc.ncs.database.NcsDatabase
import com.ccc.ncs.database.util.ArtistConverter
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun providesNcsDatabase(
        @ApplicationContext context: Context,
        gson: Gson
    ): NcsDatabase = Room.databaseBuilder(
        context,
        NcsDatabase::class.java,
        "ncs-database"
    )
        .addMigrations(MIGRATION_1_2_AddIsUserCreatedPlaylistColumn)
        .addMigrations(MIGRATION_2_3_AddArtistToMusic)
        .addMigrations(MIGRATION_3_5_AddMusicStatus)
        .addMigrations(MIGRATION_5_6_AddMapTableIndex)
        .addTypeConverter(ArtistConverter(gson))
        .build()
}