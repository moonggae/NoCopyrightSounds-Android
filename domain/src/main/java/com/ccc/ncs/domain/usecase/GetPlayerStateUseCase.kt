package com.ccc.ncs.domain.usecase

import com.ccc.ncs.domain.model.PlayerState
import com.ccc.ncs.domain.repository.LyricsRepository
import com.ccc.ncs.domain.repository.PlaybackStateRepository
import com.ccc.ncs.domain.repository.PlayerRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetPlayerStateUseCase(
    private val playbackStateRepository: PlaybackStateRepository,
    private val playerRepository: PlayerRepository,
    private val lyricsRepository: LyricsRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val lyricsFlow: Flow<String?> = combine(
        playerRepository.playlist,
        playbackStateRepository.playbackState
    ) { playlist, playbackState ->
        playlist?.let {
            PlayerState(playlist = playlist, playbackState = playbackState)
        }
    }
        .distinctUntilChangedBy { it?.currentMusic?.title }
        .map { it?.currentMusic?.title }
        .flatMapLatest { title ->
            title?.let { lyricsRepository.getLyrics(it) } ?: flowOf(null)
        }

    operator fun invoke(): Flow<PlayerState?> = combine(
        playbackStateRepository.playbackState,
        playerRepository.playlist,
        lyricsFlow
    ) { playbackState, playlist, lyrics ->
        playlist?.let {
            if (playerRepository.musicIndex.first() != playbackState.currentIndex) {
                playerRepository.updateMusicIndex(playbackState.currentIndex)
            }

            PlayerState(
                playlist = playlist,
                lyrics = lyrics,
                playbackState = playbackState
            )
        }
    }
}