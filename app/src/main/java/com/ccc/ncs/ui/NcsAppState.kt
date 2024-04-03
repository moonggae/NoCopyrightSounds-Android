package com.ccc.ncs.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.ccc.ncs.data.util.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberNcsAppState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): NcsAppState {
    return remember(
        coroutineScope,
        networkMonitor
    ) {
        NcsAppState(
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor,
        )
    }
}

@Stable
class NcsAppState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope
) {

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
}