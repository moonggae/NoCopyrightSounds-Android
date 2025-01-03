package com.ccc.ncs.feature.play

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.domain.MediaPlaybackController
import com.ccc.ncs.domain.model.PlayerState
import com.ccc.ncs.domain.model.RepeatMode
import com.ccc.ncs.domain.model.TIME_UNSET
import com.ccc.ncs.domain.repository.PlayerRepository
import com.ccc.ncs.domain.usecase.AddPlaylistMusicUseCase
import com.ccc.ncs.domain.usecase.DeletePlaylistMusicUseCase
import com.ccc.ncs.domain.usecase.GetPlayerStateUseCase
import com.ccc.ncs.domain.usecase.PlayMusicsUseCase
import com.ccc.ncs.domain.usecase.UpdatePlaylistMusicOrderUseCase
import com.ccc.ncs.model.Music
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playbackController: MediaPlaybackController,
    private val playerRepository: PlayerRepository,
    getPlayerStateUseCase: GetPlayerStateUseCase,
    private val updatePlaylistMusicOrderUseCase: UpdatePlaylistMusicOrderUseCase,
    private val deletePlaylistMusicUseCase: DeletePlaylistMusicUseCase,
    private val addPlaylistMusicUseCase: AddPlaylistMusicUseCase,
    private val playMusicsUseCase: PlayMusicsUseCase
) : ViewModel() {

    val playerUiState: StateFlow<PlayerUiState> = getPlayerStateUseCase().map { playerState ->
        if (playerState == null) PlayerUiState.Idle
        else PlayerUiState.Success(playerState)
    }.onStart {
        val musics = playerRepository.playlist.first()?.musics
        val index = playerRepository.musicIndex.first()
        val position = playerRepository.position.first() ?: TIME_UNSET
        if (musics != null && index != null) {
            playbackController.prepare(musics, index, position)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = PlayerUiState.Idle
    )

    fun play() {
        playbackController.play()
    }

    fun pause() {
        playbackController.pause()
    }

    fun prev() {
        playbackController.previous()
    }

    fun next() {
        playbackController.next()
    }

    fun setPosition(position: Long) {
        playbackController.setPosition(position)
    }

    fun setShuffleMode(isOn: Boolean) {
        playbackController.setShuffleMode(isOn)
    }

    fun setRepeatMode(repeatMode: RepeatMode) {
        playbackController.setRepeatMode(repeatMode)
    }

    fun seekToMusic(musicIndex: Int) {
        playbackController.seekTo(musicIndex)
    }

    fun updateMusicOrder(prevIndex: Int, currentIndex: Int) {
        viewModelScope.launch {
            val state = playerUiState.value as? PlayerUiState.Success ?: return@launch
            updatePlaylistMusicOrderUseCase(state.playlist.id, prevIndex, currentIndex)
                .onFailure {
                    Log.e(TAG, "updateMusicOrder", it)
                }
        }
    }

    fun closePlayer() {
        viewModelScope.launch {
            playbackController.stop()
            playerRepository.clear()
        }
    }

    fun addQueueToCurrentPlaylist(newMusicIds: List<UUID>) {
        viewModelScope.launch {
            val currentPlaylist = (playerUiState.value as? PlayerUiState.Success)?.playlist ?: return@launch
            addPlaylistMusicUseCase(currentPlaylist.id, newMusicIds).onFailure {
                Log.e(TAG, "addQueueToCurrentPlaylist", it)
            }
        }
    }

    fun playMusics(musicIds: List<UUID>) {
        viewModelScope.launch {
            playMusicsUseCase(musicIds).onFailure {
                Log.e(TAG, "playMusics", it)
            }
        }
    }

    fun removeFromQueue(music: Music) {
        viewModelScope.launch {
            val currentPlaylist = (playerUiState.value as? PlayerUiState.Success)?.playlist ?: return@launch
            deletePlaylistMusicUseCase(currentPlaylist.id, music).onFailure {
                Log.e(TAG, "removeFromQueue", it)
            }
        }
    }

    companion object {
        private const val TAG = "PlayerViewModel"
    }
}


sealed interface PlayerUiState {
    data object Idle : PlayerUiState
    data class Success(
        val playerState: PlayerState
    ) : PlayerUiState {
        val playlist by playerState::playlist
        val playbackState by playerState::playbackState
        val lyrics by playerState::lyrics
        val currentMusic by playerState::currentMusic
    }
}
