package com.ccc.ncs.domain.repository

import com.ccc.ncs.domain.model.PlaybackState
import kotlinx.coroutines.flow.Flow

interface PlaybackStateRepository {
    val playbackState: Flow<PlaybackState>
}