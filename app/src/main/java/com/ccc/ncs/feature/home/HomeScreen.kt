package com.ccc.ncs.feature.home

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ccc.ncs.model.Music
import com.ccc.ncs.ui.component.TestMusicCard

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen(
        modifier = modifier,
        testMusics = viewModel.musics.collectAsLazyPagingItems()
    )
}

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    testMusics: LazyPagingItems<Music>,
) {
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

    LazyColumn {
        items(count = testMusics.itemCount) { index ->
            testMusics[index]?.let { music ->
                TestMusicCard(
                    item = music,
                    modifier = Modifier.clickable {
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