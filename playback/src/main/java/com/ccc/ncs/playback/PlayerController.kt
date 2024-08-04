package com.ccc.ncs.playback

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.ccc.ncs.model.Music
import com.ccc.ncs.playback.playstate.RepeatMode
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

class PlayerController @Inject constructor(
    private val context: Context
) {
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

    fun setPosition(positionMs: Long) = executeAfterPrepare { controller ->
        controller.seekTo(positionMs)
    }

    fun fastForward() = executeAfterPrepare { controller ->
        controller.seekForward()
    }

    fun rewind() = executeAfterPrepare { controller ->
        controller.seekBack()
    }

    fun setRepeatMode(repeatMode: RepeatMode) = executeAfterPrepare { controller ->
        controller.setRepeatMode(repeatMode.value)
    }

    fun setShuffleMode(isOn: Boolean) = executeAfterPrepare { controller ->
        controller.setShuffleModeEnabled(isOn)
    }

    fun previous() = executeAfterPrepare { controller ->
        controller.seekToPrevious()
    }

    fun next() = executeAfterPrepare { controller ->
        controller.seekToNext()
    }

    fun play() = executeAfterPrepare { controller ->
        controller.play()
    }

    fun moveMediaItem(currentIndex: Int, newIndex: Int) = executeAfterPrepare { controller ->
        controller.moveMediaItem(currentIndex, newIndex)
    }

    fun playPause() = executeAfterPrepare { controller ->
        if (controller.isPlaying) {
            controller.pause()
        } else {
            controller.play()
        }
    }

    fun setSpeed(speed: Float) = executeAfterPrepare { controller ->
        controller.setPlaybackSpeed(speed)
    }

    fun prepare(musics: List<Music>, index: Int, positionMs: Long) = executeAfterPrepare { controller ->
        controller.setMediaItems(musics.map { it.asMediaItem() }, index, positionMs)
        controller.prepare()
    }

    fun playMusics(musics: List<Music>) = executeAfterPrepare { controller ->
        controller.setMediaItems(musics.map { it.asMediaItem() }, 0, 0)
        controller.prepare()
        controller.play()
    }

    fun stop() = executeAfterPrepare { controller ->
        controller.stop()
        controller.release()
    }

    fun appendMusics(musics: List<Music>) = executeAfterPrepare { controller ->
        controller.addMediaItems(musics.map { it.asMediaItem() })
    }

    fun removeMusic(music: Music) = executeAfterPrepare { controller ->
        val index = controller.getMediaItemIndex(music.asMediaItem())
        if (index != null)
            controller.removeMediaItem(index)
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
        if (currentItem == mediaItem) return index
    }
    return null
}