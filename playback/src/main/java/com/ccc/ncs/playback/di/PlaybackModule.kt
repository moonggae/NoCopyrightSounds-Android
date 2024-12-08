package com.ccc.ncs.playback.di

import android.app.Service
import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import com.ccc.ncs.cache.CacheManager
import com.ccc.ncs.playback.session.LibrarySessionCallback
import com.ccc.ncs.playback.session.PlaybackService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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
        @ApplicationContext context: Context,
        service: Service,
        cacheManager: CacheManager,
    ): ExoPlayer {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        val renderersFactory = DefaultRenderersFactory(service)
            .forceEnableMediaCodecAsynchronousQueueing()

        val builder = ExoPlayer.Builder(service, renderersFactory)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)

        if (cacheManager.enableCache) {
            cacheManager.getProgressiveMediaSourceFactory(context)?.let { mediaSourceFactory ->
                builder.setMediaSourceFactory(mediaSourceFactory)
            }
        }

        return builder.build()
    }

    @Provides
    @ServiceScoped
    fun providesScope(): CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    @Provides
    @ServiceScoped
    fun providesSession(
        service: Service,
        player: ExoPlayer,
        callback: LibrarySessionCallback,
    ): MediaLibraryService.MediaLibrarySession =
        MediaLibraryService.MediaLibrarySession
            .Builder(service as PlaybackService, player, callback)
            .build()
}