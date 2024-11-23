package com.ccc.ncs.domain

import com.ccc.ncs.domain.model.RepeatMode
import com.ccc.ncs.model.Music

interface MediaPlaybackController {
    fun setPosition(positionMs: Long)

    fun fastForward()

    fun rewind()

    fun setRepeatMode(repeatMode: RepeatMode)

    fun setShuffleMode(isOn: Boolean)

    fun previous()

    fun next()

    fun play()

    fun pause()

    fun moveMediaItem(currentIndex: Int, newIndex: Int)

    fun seekTo(musicIndex: Int)

    fun setSpeed(speed: Float)

    fun prepare(musics: List<Music>, index: Int, positionMs: Long)

    fun playMusics(musics: List<Music>, startIndex: Int = 0)

    fun stop()

    fun appendMusics(musics: List<Music>)

    fun removeMusic(music: Music)

    fun updateCurrentPlaylistMusic(music: Music)
}