package com.ccc.ncs.domain.repository

import kotlinx.coroutines.flow.Flow

interface LyricsRepository {
    fun getLyrics(title: String): Flow<String?>
}