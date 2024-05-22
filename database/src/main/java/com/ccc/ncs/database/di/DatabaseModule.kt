package com.ccc.ncs.database.di

import android.content.Context
import androidx.room.Room
import com.ccc.ncs.database.MIGRATION_1_2_AddIsUserCreatedPlaylistColumn
import com.ccc.ncs.database.NcsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesNcsDatabase(
        @ApplicationContext context: Context
    ): NcsDatabase = Room.databaseBuilder(
        context,
        NcsDatabase::class.java,
        "ncs-database"
    )
        .addMigrations(MIGRATION_1_2_AddIsUserCreatedPlaylistColumn)
        .build()
}