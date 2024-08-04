package com.ccc.ncs

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.ccc.ncs.data.util.NetworkMonitor
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.playback.PlayerController
import com.ccc.ncs.ui.NcsApp
import com.ccc.ncs.ui.rememberNcsAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel: MainActivityViewModel by viewModels()
    
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var playerController: PlayerController
    
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)

        setContent {
            val appState = rememberNcsAppState(
                networkMonitor = networkMonitor
            )

            NcsTheme(
                darkTheme = true
            ) {
                NcsApp(appState = appState)
            }
        }
    }

    override fun onDestroy() {
        playerController.stop()
        super.onDestroy()
    }


    companion object {
        private const val TAG = "MainActivity"
    }
}