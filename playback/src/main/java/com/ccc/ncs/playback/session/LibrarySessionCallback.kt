package com.ccc.ncs.playback.session

import android.os.Bundle
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.ccc.ncs.domain.repository.PlayerRepository
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.guava.future
import javax.inject.Inject


// 커스텀 커맨드 정의
val customCommandRepeat = SessionCommand("ACTION_REPEAT", Bundle())
val customCommandShuffle = SessionCommand("ACTION_SHUFFLE", Bundle())

// 현재 상태에 따라 Repeat 버튼 생성
fun createRepeatButton(session: MediaSession): CommandButton {
    val repeatIconResId = when (session.player.repeatMode) {
        Player.REPEAT_MODE_OFF -> androidx.media3.session.R.drawable.media3_icon_repeat_off
        Player.REPEAT_MODE_ONE -> androidx.media3.session.R.drawable.media3_icon_repeat_one
        Player.REPEAT_MODE_ALL -> androidx.media3.session.R.drawable.media3_icon_repeat_all
        else -> androidx.media3.session.R.drawable.media3_icon_repeat_off
    }

    return CommandButton.Builder()
        .setSessionCommand(customCommandRepeat)
        .setIconResId(repeatIconResId) // 현재 repeat 모드에 맞는 아이콘 설정
        .setDisplayName("Repeat")
        .build()
}

// 현재 상태에 따라 Shuffle 버튼 생성
fun createShuffleButton(session: MediaSession): CommandButton {
    val shuffleIconResId = if (session.player.shuffleModeEnabled) {
        androidx.media3.session.R.drawable.media3_icon_shuffle_on
    } else {
        androidx.media3.session.R.drawable.media3_icon_shuffle_off
    }

    return CommandButton.Builder()
        .setSessionCommand(customCommandShuffle)
        .setIconResId(shuffleIconResId) // 현재 shuffle 상태에 맞는 아이콘 설정
        .setDisplayName("Shuffle")
        .build()
}

@UnstableApi
internal class LibrarySessionCallback @Inject constructor(
    private val scope: CoroutineScope,
    private val playerRepository: PlayerRepository
) : MediaLibrarySession.Callback {
    override fun onPlaybackResumption(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo
    ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
        return scope.future {
            val mediaItems = playerRepository.playlist.first()?.musics?.map { it.asMediaItem() } ?: emptyList()
            val startIndex = playerRepository.musicIndex.first() ?: C.INDEX_UNSET
            val startPosition = playerRepository.position.first() ?: C.TIME_UNSET
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
            "ACTION_REPEAT" -> {
                session.player.repeatMode = when (session.player.repeatMode) {
                    Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
                    Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
                    Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_OFF
                    else -> Player.REPEAT_MODE_OFF
                }
            }
            "ACTION_SHUFFLE" -> {
                session.player.shuffleModeEnabled = !session.player.shuffleModeEnabled
            }
        }

        // 상태 변경 후, MediaSession을 통한 알림 갱신
        updateCustomLayout(session)

        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
    }

    // 상태 변경 후 MediaSession의 Custom Layout을 갱신하는 메서드
    private fun updateCustomLayout(session: MediaSession) {
        session.setCustomLayout(
            ImmutableList.of(
                createRepeatButton(session),
                createShuffleButton(session)
            )
        )
    }
}