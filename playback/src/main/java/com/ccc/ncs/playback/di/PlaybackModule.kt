package com.ccc.ncs.playback.di

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import com.ccc.ncs.cache.CacheManager
import com.ccc.ncs.playback.util.PlaybackConstraint
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
    fun providesPendingIntent(
        @ApplicationContext context: Context,
        mainActivityClass: Class<out ComponentActivity>
    ): PendingIntent = PendingIntent.getActivity(
        context,
        1,
        Intent(context, mainActivityClass).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(PlaybackConstraint.EXTRA_NAME_EVENT, PlaybackConstraint.EVENT_NOTIFICATION_CLICK)
        },
        FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}