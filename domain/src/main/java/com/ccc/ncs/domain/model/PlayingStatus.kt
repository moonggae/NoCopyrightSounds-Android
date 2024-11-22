package com.ccc.ncs.domain.model

enum class PlayingStatus {
    PLAYING,        // 재생 중
    PAUSED,         // 일시 중지
    BUFFERING,      // 버퍼링 중
    IDLE,           // 준비되지 않음
    ENDED           // 재생 완료
}