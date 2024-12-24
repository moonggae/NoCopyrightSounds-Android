package com.ccc.ncs

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.ccc.ncs.analytics.AnalyticsHelper
import com.ccc.ncs.analytics.LocalAnalyticsHelper
import com.ccc.ncs.data.util.NetworkMonitor
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.playback.util.PlaybackConstraint
import com.ccc.ncs.ui.NcsApp
import com.ccc.ncs.ui.rememberNcsAppState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel: MainViewModel by viewModels()

    private val notificationEvent = MutableSharedFlow<Unit>(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        viewModel.initGenreAndMood()

        setContent {
            val appState = rememberNcsAppState(
                networkMonitor = networkMonitor,
                notificationEvent = notificationEvent
            )

            CompositionLocalProvider(
                LocalAnalyticsHelper provides analyticsHelper
            ) {
                NcsTheme(darkTheme = true) {
                    NcsApp(appState = appState)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.getStringExtra(PlaybackConstraint.EXTRA_NAME_EVENT) == PlaybackConstraint.EVENT_NOTIFICATION_CLICK) {
            lifecycleScope.launch {
                try {
                    notificationEvent.emit(Unit)   
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to emit notification event", e)
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}