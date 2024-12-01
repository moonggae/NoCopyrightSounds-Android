package com.ccc.ncs.playback.repository

import com.ccc.ncs.domain.model.PlaybackState
import com.ccc.ncs.domain.repository.PlaybackStateRepository
import com.ccc.ncs.playback.playstate.PlaybackStateManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultPlaybackStateRepository @Inject constructor(
    playbackStateManager: PlaybackStateManager
): PlaybackStateRepository {
    override val playbackState: Flow<PlaybackState> = playbackStateManager.flow
}