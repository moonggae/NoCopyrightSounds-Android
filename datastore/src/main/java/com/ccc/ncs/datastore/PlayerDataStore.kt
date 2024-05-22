package com.ccc.ncs.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PlayerDataStore @Inject constructor (
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore by preferencesDataStore(name = "player_data")

    val playlistId: Flow<String?> = context.dataStore.data.map { preferences ->
        val id = preferences[PLAYLIST_ID_KEY]
        if (id.isNullOrEmpty()) null else id
    }

    suspend fun setPlaylistId(playlistId: UUID) {
        context.dataStore.edit { preferences ->
            preferences[PLAYLIST_ID_KEY] = playlistId.toString()
        }
    }

    val currentMusicIndex: Flow<Int?> = context.dataStore.data.map { preferences ->
        val index = preferences[CURRENT_MUSIC_INDEX_KEY]
        if (index == -1) null else index
    }

    suspend fun setCurrentMusicIndex(index: Int?) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_MUSIC_INDEX_KEY] = index ?: -1
        }
    }

    val position: Flow<Long?> = context.dataStore.data.map { preferences ->
        val position = preferences[POSITION_KEY]
        if (position == -1L) null else position
    }

    suspend fun setPosition(position: Long?) {
        context.dataStore.edit { preferences ->
            preferences[POSITION_KEY] = position ?: -1L
        }
    }

    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val PLAYLIST_ID_KEY = stringPreferencesKey("PLAYLIST_ID_KEY")
        private val CURRENT_MUSIC_INDEX_KEY = intPreferencesKey("CURRENT_MUSIC_INDEX_KEY")
        private val POSITION_KEY = longPreferencesKey("POSITION_KEY")
    }
}