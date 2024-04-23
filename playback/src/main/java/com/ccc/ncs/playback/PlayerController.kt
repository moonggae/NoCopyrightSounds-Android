package com.ccc.ncs.playback

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.common.C
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.ccc.ncs.model.PlayList
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

    fun previous() = executeAfterPrepare { controller ->
        controller.seekToPrevious()
    }

    fun next() = executeAfterPrepare { controller ->
        controller.seekToNext()
    }

    fun play() = executeAfterPrepare { controller ->
        controller.play()
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

    fun setPlayList(playList: PlayList) = executeAfterPrepare { controller ->
        controller.setMediaItems(
            playList.musics.map { it.asMediaItem() },
            C.INDEX_UNSET,
            C.TIME_UNSET
        )
        controller.prepare()
        controller.play()
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