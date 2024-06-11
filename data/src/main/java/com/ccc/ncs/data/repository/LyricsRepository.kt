package com.ccc.ncs.data.repository

import kotlinx.coroutines.flow.Flow

interface LyricsRepository {
    fun getLyrics(title: String): Flow<String?>
}