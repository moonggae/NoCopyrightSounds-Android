package com.ccc.ncs.playback.di

import android.app.Service
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import com.ccc.ncs.playback.playstate.PlaybackStateListener
import com.ccc.ncs.playback.session.LibrarySessionCallback
import com.ccc.ncs.playback.session.PlaybackService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@UnstableApi
@Module
@InstallIn(ServiceComponent::class)
internal object PlaybackModule {
    @Provides
    @ServiceScoped
    fun providesPlayer(
        service: Service,
        playbackStateListener: PlaybackStateListener,
    ): Player {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .setUsage(C.USAGE_MEDIA)
            .build()
        val renderersFactory = DefaultRenderersFactory(service)
            .forceEnableMediaCodecAsynchronousQueueing()
        return ExoPlayer.Builder(service, renderersFactory)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()
            .also { player ->
                playbackStateListener.attachTo(player)
            }
    }

    @Provides
    @ServiceScoped
    fun providesScope(): CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    @Provides
    @ServiceScoped
    fun providesSession(
        service: Service,
        player: Player,
        callback: LibrarySessionCallback,
    ): MediaLibraryService.MediaLibrarySession =
        MediaLibraryService.MediaLibrarySession
            .Builder(service as PlaybackService, player, callback)
            .build()
}