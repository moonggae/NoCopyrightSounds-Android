package com.ccc.ncs

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ccc.ncs.analytics.AnalyticsHelper
import com.ccc.ncs.analytics.LocalAnalyticsHelper
import com.ccc.ncs.data.util.NetworkMonitor
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.ui.NcsApp
import com.ccc.ncs.ui.rememberNcsAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        viewModel.initGenreAndMood()

        setContent {
            val appState = rememberNcsAppState(
                networkMonitor = networkMonitor
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

    companion object {
        private const val TAG = "MainActivity"
    }
}