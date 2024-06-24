package com.ccc.ncs.feature.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.LyricsRepository
import com.ccc.ncs.data.repository.PlayListRepository
import com.ccc.ncs.data.repository.PlayerRepository
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.playback.PlayerController
import com.ccc.ncs.playback.playstate.PlaybackStateManager
import com.ccc.ncs.util.swap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playbackStateManager: PlaybackStateManager,
    private val playerController: PlayerController,
    private val playerRepository: PlayerRepository,
    private val playlistRepository: PlayListRepository,
    private val lyricsRepository: LyricsRepository
) : ViewModel() {
    private val _playerUiState: MutableStateFlow<PlayerUiState> = MutableStateFlow(PlayerUiState.Loading)
    val playerUiState: StateFlow<PlayerUiState> = _playerUiState

    init {
        observePlaylistState()
        observePlaybackState()
        observeLyrics()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeLyrics() {
        viewModelScope.launch {
            playerUiState
                .distinctUntilChangedBy { state ->
                    when (state) {
                        is PlayerUiState.Success -> state.currentMusic
                        else -> state
                    }
                }.flatMapLatest { state ->
                when (state) {
                    is PlayerUiState.Success -> {
                        val musicTitle = state.currentMusic?.title
                        if (musicTitle == null) flowOf(null)
                        else lyricsRepository.getLyrics(musicTitle)
                    }

                    is PlayerUiState.Loading -> flowOf(null)
                }
            }.collect { lyrics ->
                _playerUiState.update {
                    if (it is PlayerUiState.Success) {
                        it.copy(lyrics = lyrics)
                    } else { it }
                }
            }
        }
    }

    private fun observePlaylistState() {
        viewModelScope.launch {
            playerRepository.playlist.collect { playlist ->
                when (playlist == null) {
                    true -> _playerUiState.update { PlayerUiState.Loading }
                    false -> _playerUiState.update { playerState ->
                        when (playerState) {
                            is PlayerUiState.Success -> {
                                playerState.copy(
                                    playlist = playlist
                                )
                            }

                            is PlayerUiState.Loading -> {
                                val musicIndex = playerRepository.musicIndex.first() ?: 0
                                val position = playerRepository.position.first() ?: 0
                                playerController.prepare(playlist.musics, musicIndex, position)
                                PlayerUiState.Success(
                                    playlist = playlist,
                                    currentIndex = musicIndex,
                                    position = position
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observePlaybackState() {
        viewModelScope.launch {
            playbackStateManager.flow.collect { playbackState ->
                _playerUiState.update {
                    if (it is PlayerUiState.Loading) return@update it

                    playerRepository.updateMusicIndex(playbackState.currentIndex)
                    playerRepository.updatePosition(playbackState.position)

                    (it as PlayerUiState.Success).copy(
                        isPlaying = playbackState.isPlaying,
                        currentIndex = playbackState.currentIndex,
                        hasPrevious = playbackState.hasPrevious,
                        hasNext = playbackState.hasNext,
                        position = playbackState.position,
                        duration = playbackState.duration,
                        speed = playbackState.speed,
                        isShuffleOn = playbackState.isShuffleEnabled,
                        isRepeatOn = playbackState.isRepeatMode
                    )
                }
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

    fun setShuffleMode(isOn: Boolean) {
        playerController.setShuffleMode(isOn)
    }

    fun setRepeatMode(isOn: Boolean) {
        playerController.setRepeatMode(isOn)
    }

    // note: same feature as in PlaylistDetailViewModel.kt
    fun updateMusicOrder(prevIndex: Int, currentIndex: Int) {
        viewModelScope.launch {
            _playerUiState.value.let { state ->
                if (state is PlayerUiState.Success) {
                    val reorderedMusicList = state.playlist.musics.swap(prevIndex, currentIndex)
                    playlistRepository.setPlayListMusics(state.playlist.id, reorderedMusicList)
                    playerController.moveMediaItem(prevIndex, currentIndex)
                }
            }
        }
    }

    fun playPlayList(playList: PlayList) {
        viewModelScope.launch {
            playerRepository.setPlaylist(playList.id)
            playerController.playMusics(playList.musics)
        }
    }

    fun closePlayer() {
        viewModelScope.launch {
            _playerUiState.update { PlayerUiState.Loading }
            playerController.stop()
            playerRepository.clear()
        }
    }

    fun playMusics(musics: List<Music>) {
        viewModelScope.launch {
            val playlist = playlistRepository.getAutoGeneratedPlayList().copy(musics = musics)
            playlistRepository.setPlayListMusics(playListId = playlist.id, musics = playlist.musics)
            playPlayList(playlist)
        }
    }

    fun addToQueue(newMusic: List<Music>) {
        viewModelScope.launch {
            val currentPlaylist = _playerUiState.value.let { state ->
                if (state is PlayerUiState.Success) state.playlist
                else playlistRepository.getAutoGeneratedPlayList()
            }
            val distinctNewMusics = newMusic - currentPlaylist.musics
            val updateMusics = currentPlaylist.musics + distinctNewMusics
            playlistRepository.setPlayListMusics(currentPlaylist.id, updateMusics)
            playerController.appendMusics(distinctNewMusics)
        }
    }
}

sealed interface PlayerUiState {
    data object Loading : PlayerUiState
    data class Success(
        val playlist: PlayList,
        val isPlaying: Boolean = false,
        val currentIndex: Int = -1,
        val hasPrevious: Boolean = false,
        val hasNext: Boolean = false,
        val position: Long = 0,
        val duration: Long = 0,
        val speed: Float = 1f,
        val isShuffleOn: Boolean = false,
        val isRepeatOn: Boolean = false,
        val lyrics: String? = null
    ) : PlayerUiState {
        val currentMusic: Music?
            get() =
                if (currentIndex == -1) null
                else playlist.musics.getOrNull(currentIndex)
    }
}