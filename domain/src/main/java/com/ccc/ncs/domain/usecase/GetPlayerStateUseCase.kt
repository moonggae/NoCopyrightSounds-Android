package com.ccc.ncs.domain.usecase

import com.ccc.ncs.domain.model.PlayerState
import com.ccc.ncs.domain.repository.LyricsRepository
import com.ccc.ncs.domain.repository.PlaybackStateRepository
import com.ccc.ncs.domain.repository.PlayerRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull

class GetPlayerStateUseCase(
    private val playbackStateRepository: PlaybackStateRepository,
    private val playerRepository: PlayerRepository,
    private val lyricsRepository: LyricsRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val lyricsFlow: Flow<String?> = combine(
        playerRepository.playlist.filterNotNull(),
        playbackStateRepository.playbackState
    ) { playlist, playbackState ->
        PlayerState(
            playlist = playlist,
            playbackState = playbackState
        )
    }
        .distinctUntilChangedBy { it.currentMusic?.title }
        .mapNotNull { it.currentMusic?.title }
        .flatMapLatest { title ->
            lyricsRepository.getLyrics(title)
        }

    operator fun invoke(): Flow<PlayerState?> = combine(
        playbackStateRepository.playbackState,
        playerRepository.playlist,
        lyricsFlow
    ) { playbackState, playlist, lyrics ->
        if (playlist == null) null

        else {
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