package com.ccc.ncs.playback.di

import android.app.Service
import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.session.MediaLibraryService
import com.ccc.ncs.cache.CacheManager
import com.ccc.ncs.playback.playstate.PlaybackStateListener
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
        cache: Cache,
        playbackStateListener: PlaybackStateListener,
    ): ExoPlayer {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        val renderersFactory = DefaultRenderersFactory(service)
            .forceEnableMediaCodecAsynchronousQueueing()

        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCacheKeyFactory { it.key ?: "" }
            .setCache(cache)
            .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context))

        val progressiveMediaSourceFactory = ProgressiveMediaSource
            .Factory(cacheDataSourceFactory)

        return ExoPlayer.Builder(service, renderersFactory)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .setMediaSourceFactory(progressiveMediaSourceFactory)
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
        player: ExoPlayer,
        callback: LibrarySessionCallback,
    ): MediaLibraryService.MediaLibrarySession =
        MediaLibraryService.MediaLibrarySession
            .Builder(service as PlaybackService, player, callback)
            .build()

    @Provides
    @ServiceScoped
    fun providesCache(): Cache = CacheManager.cache
}