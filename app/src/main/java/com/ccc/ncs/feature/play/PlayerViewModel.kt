package com.ccc.ncs.feature.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.ncs.data.repository.LyricsRepository
import com.ccc.ncs.data.repository.MusicRepository
import com.ccc.ncs.data.repository.PlayListRepository
import com.ccc.ncs.data.repository.PlayerRepository
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.PlayList
import com.ccc.ncs.model.util.reorder
import com.ccc.ncs.playback.PlayerController
import com.ccc.ncs.playback.playstate.PlaybackStateManager
import com.ccc.ncs.playback.playstate.PlayingStatus
import com.ccc.ncs.playback.playstate.RepeatMode
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playbackStateManager: PlaybackStateManager,
    private val playerController: PlayerController,
    private val playerRepository: PlayerRepository,
    private val playlistRepository: PlayListRepository,
    private val lyricsRepository: LyricsRepository,
    private val musicRepository: MusicRepository,
    private val analytics: FirebaseAnalytics
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val lyricsFlow: StateFlow<String?> = combine(
        playerRepository.playlist.filterNotNull(),
        playbackStateManager.flow
    ) { playlist, playbackState ->
        val currentMusic = playlist.musics.getOrNull(playbackState.currentIndex)
        currentMusic
    }.filterNotNull()
        .distinctUntilChangedBy { music -> music.id }
        .flatMapLatest { music ->
            lyricsRepository.getLyrics(music.title)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )


    val playerUiState: StateFlow<PlayerUiState> = combine(
        playerRepository.playlist,
        playerRepository.musicIndex,
        playbackStateManager.flow,
        lyricsFlow
    ) { playlist, musicIndex, playbackState, lyrics ->
        when {
            playlist == null || playlist.musics.isEmpty() -> PlayerUiState.Loading
            else -> PlayerUiState.Success(
                playlist = playlist,
                currentIndex = playbackState.currentIndex.takeIf { it >= 0 } ?: musicIndex ?: 0,
                position = playbackState.position,
                playingStatus = playbackState.playingStatus,
                hasPrevious = playbackState.hasPrevious,
                hasNext = playbackState.hasNext,
                duration = playbackState.duration,
                speed = playbackState.speed,
                isShuffleOn = playbackState.isShuffleEnabled,
                repeatMode = playbackState.repeatMode,
                lyrics = lyrics
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = PlayerUiState.Loading
    )

    private fun observerCurrentPlayingMusicIndex() {
        viewModelScope.launch {
            playerUiState
                .filterNotNull()
                .filterIsInstance(PlayerUiState.Success::class)
                .distinctUntilChangedBy { it.currentIndex }
                .map { it.currentIndex }
                .collect { currentIndex ->
                    if (playerRepository.musicIndex.first() != currentIndex && currentIndex != -1) {
                        playerRepository.updateMusicIndex(currentIndex)
                    }
                }
        }
    }

    init {
        observerCurrentPlayingMusicIndex()
    }

    // Player control functions
    fun playPause() {
        analytics.logEvent("player_play_pause", null)
        playerController.playPause()
    }
    fun prev() {
        analytics.logEvent("player_prev", null)
        playerController.previous()
    }
    fun next() {
        analytics.logEvent("player_next", null)
        playerController.next()
    }
    fun setPosition(position: Long) {
        analytics.logEvent("player_set_position") {
            param("position", position)
        }
        playerController.setPosition(position)
    }
    fun setShuffleMode(isOn: Boolean) {
        analytics.logEvent("player_set_shuffle") {
            param("on", "$isOn")
        }
        playerController.setShuffleMode(isOn)
    }
    fun setRepeatMode(repeatMode: RepeatMode) {
        analytics.logEvent("player_set_repeat") {
            param("repeat_mode", repeatMode.name)
        }
        playerController.setRepeatMode(repeatMode)
    }
    fun seekToMusic(musicIndex: Int) {
        analytics.logEvent("player_seek_to_music") {
            param("music_index", musicIndex.toLong())
        }
        playerController.seekTo(musicIndex)
    }

    fun updateMusicOrder(prevIndex: Int, currentIndex: Int) {
        analytics.logEvent("player_update_music_order") {
            param("prev", prevIndex.toLong())
            param("current", currentIndex.toLong())
        }

        viewModelScope.launch {
            val state = playerUiState.value as? PlayerUiState.Success ?: return@launch
            val prevPlayingMusicId = state.currentMusic?.id

            val prevMusicList = state.playlist.musics
            val reorderedMusicList = prevMusicList.reorder(prevIndex, currentIndex)

            playlistRepository.setPlayListMusics(state.playlist.id, reorderedMusicList)
            playerController.moveMediaItem(prevIndex, currentIndex)

            reorderedMusicList
                .indexOfFirst { it.id == prevPlayingMusicId }
                .takeIf { it > 0 }
                ?.let {
                    playerRepository.updateMusicIndex(it)
                }
        }
    }

    fun playPlayList(playList: PlayList, startIndex: Int = 0) {
        analytics.logEvent("player_play_playlist") {
            param("playlist_name", playList.name)
            param("playlist_is_user_created", "${playList.isUserCreated}")
            param("playlist_music_size", playList.musics.size.toLong())
        }

        viewModelScope.launch {
            playerRepository.setPlaylist(playList.id)
            playerRepository.updateMusicIndex(startIndex)
            playerController.playMusics(playList.musics, startIndex)
        }
    }

    fun closePlayer() {
        analytics.logEvent("player_close", null)
        viewModelScope.launch {
            playerController.stop()
            playerRepository.clear()
        }
    }

    private suspend fun addNewMusicsToPlaylist(playlist: PlayList, newMusicIds: List<UUID>): Pair<PlayList, List<Music>> {
        val newMusics = musicRepository.getMusics(newMusicIds).first()
        val distinctNewMusics = newMusics.filterNot { playlist.musics.contains(it) }
        val updatedMusics = playlist.musics + distinctNewMusics
        val updatedPlaylist = playlist.copy(musics = updatedMusics)
        playlistRepository.setPlayListMusics(playlist.id, updatedMusics)
        return updatedPlaylist to distinctNewMusics
    }

    fun addQueueToCurrentPlaylist(newMusicIds: List<UUID>): Job = viewModelScope.launch {
        analytics.logEvent("player_add_queue") {
            param("music_ids", newMusicIds.joinToString { it.toString() })
        }

        val currentPlaylist = (playerUiState.value as? PlayerUiState.Success)?.playlist ?: return@launch
        val newMusics = musicRepository.getMusics(newMusicIds).first()
        val distinctNewMusics = newMusics.filterNot { currentPlaylist.musics.contains(it) }
        val updatedMusics = currentPlaylist.musics + distinctNewMusics
        playlistRepository.setPlayListMusics(currentPlaylist.id, updatedMusics)
        playerController.appendMusics(distinctNewMusics)
    }

    fun playMusicWithRecentPlaylist(musicIds: List<UUID>) {
        analytics.logEvent("player_play_musics_with_recent_playlist") {
            param("music_ids", musicIds.joinToString { it.toString() })
        }

        viewModelScope.launch {
            val playlist = playlistRepository.getAutoGeneratedPlayList()
            val newMusics = musicRepository.getMusics(musicIds).first()
            val updatedMusics = (playlist.musics - newMusics) + newMusics
            val startIndex = updatedMusics.indexOfFirst { musicIds.contains(it.id) }
            val updatedPlaylist = playlist.copy(musics = updatedMusics)
            playlistRepository.setPlayListMusics(playlist.id, updatedMusics)
            playPlayList(updatedPlaylist, startIndex)
        }
    }

    private fun playMusicWithCurrentPlaylist(musicId: UUID) {
        analytics.logEvent("player_play_musics_with_current_playlist") {
            param("music_id", "$musicId")
        }

        viewModelScope.launch {
            addQueueToCurrentPlaylist(listOf(musicId)).join()

            playerUiState
                .filterIsInstance<PlayerUiState.Success>()
                .first { it.playlist.musics.any { music -> music.id == musicId } }
                .let { state ->
                    val playMusicIndex = state.playlist.musics.indexOfFirst { it.id == musicId }
                    if (playMusicIndex >= 0) {
                        seekToMusic(playMusicIndex)
                    }
                }
        }
    }

    fun addToQueueAndPlay(musicId: UUID) {
        analytics.logEvent("player_add_to_queue_and_play") {
            param("music_id", "$musicId")
        }

        viewModelScope.launch {
            when (playerUiState.value) {
                is PlayerUiState.Loading -> playMusicWithRecentPlaylist(listOf(musicId))
                is PlayerUiState.Success -> playMusicWithCurrentPlaylist(musicId)
            }
        }
    }

    fun removeFromQueue(music: Music) {
        analytics.logEvent("player_remove_from_queue") {
            param("music_id", "${music.id}")
        }

        viewModelScope.launch {
            val currentPlaylist = (playerUiState.value as? PlayerUiState.Success)?.playlist ?: return@launch
            val updatedMusics = currentPlaylist.musics - music
            playlistRepository.setPlayListMusics(currentPlaylist.id, updatedMusics)
            playerController.removeMusic(music)
        }
    }
}





sealed interface PlayerUiState {
    object Loading : PlayerUiState
    data class Success(
        val playlist: PlayList,
        val playingStatus: PlayingStatus = PlayingStatus.IDLE,
        val currentIndex: Int = -1,
        val hasPrevious: Boolean = false,
        val hasNext: Boolean = false,
        val position: Long = 0,
        val duration: Long = 0,
        val speed: Float = 1f,
        val isShuffleOn: Boolean = false,
        val repeatMode: RepeatMode = RepeatMode.REPEAT_MODE_OFF,
        val lyrics: String? = null
    ) : PlayerUiState {
        val currentMusic: Music?
            get() = playlist.musics.getOrNull(currentIndex)
    }
}

