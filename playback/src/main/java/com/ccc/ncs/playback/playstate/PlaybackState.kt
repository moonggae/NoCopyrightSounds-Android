package com.ccc.ncs.playback.playstate

import androidx.media3.common.Player
import com.ccc.ncs.domain.model.PlayingStatus


val Player.playingStatus: PlayingStatus
    get() = when {
        playbackState == Player.STATE_READY && playWhenReady -> PlayingStatus.PLAYING
        playbackState == Player.STATE_READY && !playWhenReady -> PlayingStatus.PAUSED
        playbackState == Player.STATE_BUFFERING -> PlayingStatus.BUFFERING
        playbackState == Player.STATE_IDLE -> PlayingStatus.IDLE
        playbackState == Player.STATE_ENDED -> PlayingStatus.ENDED
        else -> PlayingStatus.IDLE // 기본값을 IDLE로 설정
    }