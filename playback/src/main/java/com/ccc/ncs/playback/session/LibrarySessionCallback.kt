package com.ccc.ncs.playback.session

import android.os.Bundle
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.ccc.ncs.playback.data.PlaybackSessionDataSource
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.guava.future
import javax.inject.Inject


@UnstableApi
internal class LibrarySessionCallback @Inject constructor(
    private val scope: CoroutineScope,
    private val playbackSessionDataSource: PlaybackSessionDataSource
) : MediaLibrarySession.Callback {
    override fun onPlaybackResumption(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo
    ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
        return scope.future {
            val mediaItems = playbackSessionDataSource.getCurrentPlaylist()?.musics?.map { it.asMediaItem() } ?: emptyList()
            val startIndex = playbackSessionDataSource.getCurrentMusicIndex() ?: C.INDEX_UNSET
            val startPosition = playbackSessionDataSource.getCurrentPosition() ?: C.TIME_UNSET
            MediaSession.MediaItemsWithStartPosition(mediaItems, startIndex, startPosition)
        }
    }

    override fun onConnect(session: MediaSession, controller: MediaSession.ControllerInfo): MediaSession.ConnectionResult {
        if (session.isMediaNotificationController(controller)) {
            val sessionCommands = MediaSession.ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
                .add(customCommandRepeat)
                .add(customCommandShuffle)
                .build()

            val playerCommands = MediaSession.ConnectionResult.DEFAULT_PLAYER_COMMANDS.buildUpon().build()

            return MediaSession.ConnectionResult.AcceptedResultBuilder(session)
                .setCustomLayout(
                    ImmutableList.of(
                        createRepeatButton(session),
                        createShuffleButton(session)
                    )
                )
                .setAvailablePlayerCommands(playerCommands)
                .setAvailableSessionCommands(sessionCommands)
                .build()
        }
        return MediaSession.ConnectionResult.AcceptedResultBuilder(session).build()
    }

    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {
        when (customCommand.customAction) {
            ACTION_REPEAT -> {
                session.player.repeatMode = when (session.player.repeatMode) {
                    Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
                    Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
                    Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_OFF
                    else -> Player.REPEAT_MODE_OFF
                }
            }
            ACTION_SHUFFLE -> {
                session.player.shuffleModeEnabled = !session.player.shuffleModeEnabled
            }
        }

        // 상태 변경 후, MediaSession을 통한 알림 갱신
        updateCustomLayout(session)

        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
    }

    private fun updateCustomLayout(session: MediaSession) {
        session.setCustomLayout(
            ImmutableList.of(
                createRepeatButton(session),
                createShuffleButton(session)
            )
        )
    }
}