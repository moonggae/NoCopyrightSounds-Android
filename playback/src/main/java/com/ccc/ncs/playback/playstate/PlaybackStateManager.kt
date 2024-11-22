package com.ccc.ncs.playback.playstate

import com.ccc.ncs.domain.model.PlaybackState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackStateManager @Inject constructor() {
    private val _playbackState = MutableStateFlow(PlaybackState())

    val flow: StateFlow<PlaybackState> get() = _playbackState
    var playbackState: PlaybackState
        set(value) {
            _playbackState.value = value
        }
        get() = _playbackState.value
}