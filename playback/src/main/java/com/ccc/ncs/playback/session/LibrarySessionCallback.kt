package com.ccc.ncs.playback.session

import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ConnectionResult.AcceptedResultBuilder
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.ccc.ncs.domain.repository.PlayerRepository
import com.ccc.ncs.playback.session.MediaCustomLayoutHandler.Companion.customCommandRepeat
import com.ccc.ncs.playback.session.MediaCustomLayoutHandler.Companion.customCommandShuffle
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.guava.future
import java.util.UUID
import javax.inject.Inject


@OptIn(UnstableApi::class)
internal class LibrarySessionCallback @Inject constructor(
    private val scope: CoroutineScope,
    private val playerRepository: PlayerRepository,
    private val customLayoutHandler: MediaCustomLayoutHandler,
    private val autoSessionHandler: AutoSessionHandler,
    private val mediaLibraryBrowser: MediaLibraryBrowser
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
        val sessionCommands = MediaSession.ConnectionResult.DEFAULT_SESSION_AND_LIBRARY_COMMANDS.buildUpon()
            .add(customCommandRepeat)
            .add(customCommandShuffle)
            .remove(SessionCommand.COMMAND_CODE_SESSION_SET_RATING)
            .remove(SessionCommand.COMMAND_CODE_LIBRARY_SUBSCRIBE)
            .remove(SessionCommand.COMMAND_CODE_LIBRARY_UNSUBSCRIBE)
            .remove(SessionCommand.COMMAND_CODE_LIBRARY_SEARCH)
            .remove(SessionCommand.COMMAND_CODE_LIBRARY_GET_SEARCH_RESULT)
            .build()

        val playerCommands = MediaSession.ConnectionResult.DEFAULT_PLAYER_COMMANDS.buildUpon().build()

        return when {
            session.isMediaNotificationController(controller) || autoSessionHandler.isAutoController(session, controller) ->
                AcceptedResultBuilder(session)
                    .setAvailablePlayerCommands(playerCommands)
                    .setAvailableSessionCommands(sessionCommands)
                    .setCustomLayout(customLayoutHandler.createCustomLayout(session))
                    .build()

            else -> AcceptedResultBuilder(session).build()
        }
    }

    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: SessionCommand,
        args: Bundle
    ): ListenableFuture<SessionResult> {
        customLayoutHandler.handleCustomCommand(session, customCommand)
        return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
    }

    override fun onGetLibraryRoot(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
        val rootItem = mediaLibraryBrowser.getRootItem()
        val result = LibraryResult.ofItem(rootItem, params)
        return Futures.immediateFuture(result)
    }

    override fun onGetChildren(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> =
        scope.future {
            val items = mediaLibraryBrowser.getChildren(parentId)
            autoSessionHandler.setPlaylistId(parentId)
            LibraryResult.ofItemList(items, params)
        }


    override fun onSetMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>,
        startIndex: Int,
        startPositionMs: Long
    ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
        if (autoSessionHandler.isAutoController(mediaSession, controller)) {
            autoSessionHandler.handleAutoMediaItems(mediaItems)
        }

        return super.onSetMediaItems(mediaSession, controller, mediaItems, startIndex, startPositionMs)
    }
}

fun String.toUUID(): UUID? = try {
    UUID.fromString(this)
} catch (e: Exception) {
    null
}