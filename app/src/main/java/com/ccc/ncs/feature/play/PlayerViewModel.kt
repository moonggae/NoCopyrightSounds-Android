package com.ccc.ncs.feature.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.playback.PlayerController
import com.ccc.ncs.playback.playstate.PlaybackStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playbackStateManager: PlaybackStateManager,
    private val playerController: PlayerController,
): ViewModel() {
    private val _playerUiState =
        MutableStateFlow<PlayerUiState>(PlayerUiState.Loading)
    val playerUiState: StateFlow<PlayerUiState> = _playerUiState


    init {
        viewModelScope.launch {
            playbackStateManager.flow.collect {
                _playerUiState.value = PlayerUiState.Success(
                    it.isPlaying,
                    it.hasPrevious,
                    it.hasNext,
                    it.position,
                    it.duration,
                    it.speed,
                    it.aspectRatio
                )
            }
        }
    }


    fun playPause() {
        playerController.playPause()
    }

    fun prev() {
        playerController.previous()
    }

    fun next() {
        playerController.next()
    }

    fun setPosition(position: Long) {
        playerController.setPosition(position)
    }

    fun setPlayList(playList: PlayList) = playerController.setPlayList(playList)
}