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
import com.ccc.ncs.ui.NcsApp
import com.ccc.ncs.ui.rememberNcsAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

//    @Inject
//    lateinit var playerController: PlayerController

    private val cacheViewModel: CacheViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)

        // 폴더블 스크린 상태 변경시 초기화 필요함
        cacheViewModel.initCacheManager(applicationContext)

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

    companion object {
        private const val TAG = "MainActivity"
    }
}