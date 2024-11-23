package com.ccc.ncs.playback

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.ccc.ncs.domain.MediaPlaybackController
import com.ccc.ncs.domain.model.RepeatMode
import com.ccc.ncs.model.Music
import com.ccc.ncs.playback.session.PlaybackService
import com.ccc.ncs.playback.session.asMediaItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.guava.asDeferred
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException
import javax.inject.Inject

internal class DefaultMediaPlaybackController @Inject constructor(
    private val context: Context
): MediaPlaybackController {
    private var controllerDeferred: Deferred<MediaController> = newControllerAsync()

    private fun newControllerAsync() = MediaController
        .Builder(context, SessionToken(context, ComponentName(context, PlaybackService::class.java)))
        .buildAsync()
        .asDeferred()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val activeControllerDeferred: Deferred<MediaController>
        get() {
            if (controllerDeferred.isCompleted) {
                val completedController = controllerDeferred.getCompleted()
                if (!completedController.isConnected) {
                    completedController.release()
                    controllerDeferred = newControllerAsync()
                }
            }
            return controllerDeferred
        }
    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    override fun setPosition(positionMs: Long) = executeAfterPrepare { controller ->
        controller.seekTo(positionMs)
    }

    override fun fastForward() = executeAfterPrepare { controller ->
        controller.seekForward()
    }

    override fun rewind() = executeAfterPrepare { controller ->
        controller.seekBack()
    }

    override fun setRepeatMode(repeatMode: RepeatMode) = executeAfterPrepare { controller ->
        controller.setRepeatMode(repeatMode.value)
    }

    override fun setShuffleMode(isOn: Boolean) = executeAfterPrepare { controller ->
        controller.setShuffleModeEnabled(isOn)
    }

    override fun previous() = executeAfterPrepare { controller ->
        controller.seekToPrevious()
    }

    override fun next() = executeAfterPrepare { controller ->
        controller.seekToNext()
    }

    override fun play() = executeAfterPrepare { controller ->
        controller.play()
    }

    override fun pause() = executeAfterPrepare { controller ->
        controller.pause()
    }

    override fun moveMediaItem(currentIndex: Int, newIndex: Int) = executeAfterPrepare { controller ->
        controller.moveMediaItem(currentIndex, newIndex)
    }

    override fun seekTo(musicIndex: Int) = executeAfterPrepare { controller ->
        controller.seekToDefaultPosition(musicIndex)
    }

    override fun setSpeed(speed: Float) = executeAfterPrepare { controller ->
        controller.setPlaybackSpeed(speed)
    }

    override fun prepare(musics: List<Music>, index: Int, positionMs: Long) = executeAfterPrepare { controller ->
        controller.setMediaItems(musics.map { it.asMediaItem() }, index, positionMs)
        controller.prepare()
    }

    override fun playMusics(
        musics: List<Music>,
        startIndex: Int
    ) = executeAfterPrepare { controller ->
        prepare(musics, startIndex, 0)
        controller.play()
    }

    override fun stop() = executeAfterPrepare { controller ->
        controller.stop()
        controller.release()
    }

    override fun appendMusics(musics: List<Music>) = executeAfterPrepare { controller ->
        controller.addMediaItems(musics.map { it.asMediaItem() })
    }

    override fun removeMusic(music: Music) = executeAfterPrepare { controller ->
        val index = controller.getMediaItemIndex(music.asMediaItem())
        if (index != null)
            controller.removeMediaItem(index)
    }

    override fun updateCurrentPlaylistMusic(music: Music) = executeAfterPrepare { controller ->
        val mediaItem = music.asMediaItem()
        val index = controller.getMediaItemIndex(mediaItem) ?: return@executeAfterPrepare

        if (index == controller.currentMediaItemIndex) {
            val listener = object : Player.Listener {
                override fun onMediaItemTransition(newMediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(newMediaItem, reason)

                    // 업데이트할 미디어 항목 인덱스 가져오기
                    val updateMediaItemIndex = controller.getMediaItemIndex(mediaItem)
                    if (updateMediaItemIndex == null) {
                        controller.removeListener(this)
                        return
                    }

                    // 현재 재생 중인 항목이 다를 경우 교체
                    val currentMediaItemIndex = newMediaItem?.let { controller.getMediaItemIndex(it) } ?: -1
                    if (currentMediaItemIndex != updateMediaItemIndex) {
                        controller.replaceMediaItem(updateMediaItemIndex, mediaItem)
                        controller.removeListener(this)
                    }
                }
            }
            controller.addListener(listener)
        } else {
            // 현재 재생 항목이 아닌 경우 바로 교체
            controller.replaceMediaItem(index, mediaItem)
        }
    }



    private inline fun executeAfterPrepare(crossinline action: suspend (MediaController) -> Unit) {
        scope.launch {
            val controller = awaitConnect() ?: return@launch
            action(controller)
        }
    }

    private suspend fun awaitConnect(): MediaController? {
        return runCatching {
            activeControllerDeferred.await()
        }.getOrElse { e ->
            if (e is CancellationException) throw e
            null
        }
    }
}

private fun  MediaController.getMediaItemIndex(mediaItem: MediaItem):Int? {
    repeat(this.mediaItemCount) { index ->
        val currentItem = this.getMediaItemAt(index)
        if (currentItem.mediaId == mediaItem.mediaId) return index
    }
    return null
}