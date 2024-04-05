package com.ccc.ncs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ccc.ncs.data.util.NetworkMonitor
import com.ccc.ncs.ui.NcsApp
import com.ccc.ncs.ui.rememberNcsAppState
import com.ccc.ncs.ui.theme.NcsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel: MainActivityViewModel by viewModels()
    
    @Inject
    lateinit var networkMonitor: NetworkMonitor
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            val appState = rememberNcsAppState(
                networkMonitor = networkMonitor
            )

            NcsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NcsApp(appState = appState)
                }
            }
        }
    }


    companion object {
        private const val TAG = "MainActivity"
    }
}