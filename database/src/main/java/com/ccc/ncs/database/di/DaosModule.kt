package com.ccc.ncs.database.di

import com.ccc.ncs.database.NcsDatabase
import com.ccc.ncs.database.dao.GenreDao
import com.ccc.ncs.database.dao.MoodDao
import com.ccc.ncs.database.dao.MusicDao
import com.ccc.ncs.database.dao.PlayListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesGenreDao(
        database: NcsDatabase
    ): GenreDao = database.genreDao()

    @Provides
    fun providesMoodDao(
        database: NcsDatabase
    ): MoodDao = database.moodDao()

    @Provides
    fun providesMusicDao(
        database: NcsDatabase
    ): MusicDao = database.musicDao()

    @Provides
    fun providesPlayListDao(
        database: NcsDatabase
    ): PlayListDao = database.playListDao()
}