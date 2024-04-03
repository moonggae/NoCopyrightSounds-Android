package com.ccc.ncs.ui

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import com.ccc.ncs.R
import com.ccc.ncs.model.Music
import com.ccc.ncs.ui.component.TestMusicCard

@Composable
fun NcsApp(
    appState: NcsAppState,
    testMusics: LazyPagingItems<Music>
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val isOffline by appState.isOffline.collectAsStateWithLifecycle()

    val mediaPlayer: MediaPlayer = remember {
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    }

    val notConnectedMessage = stringResource(R.string.not_connected)
    LaunchedEffect(isOffline) {
        if (isOffline) {
            snackbarHostState.showSnackbar(
                message = notConnectedMessage,
                duration = SnackbarDuration.Indefinite
            )
        } else {
            testMusics.retry()
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            items(count = testMusics.itemCount) { index ->
                testMusics[index]?.let { music ->
                    TestMusicCard(
                        item = music,
                        modifier = Modifier.clickable {
                            Log.d("TAG", "${music.dataUrl}")
                            mediaPlayer.apply {
                                reset()
                                setDataSource(music.dataUrl)
                                setOnPreparedListener {
                                    start()
                                }
                                prepareAsync()
                            }
                        }
                    )
                }
            }
        }
    }
}