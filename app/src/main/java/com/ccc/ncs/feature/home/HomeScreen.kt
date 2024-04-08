package com.ccc.ncs.feature.home

import android.content.ComponentName
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ccc.ncs.feature.play.PlaybackService
import com.ccc.ncs.model.Music
import com.ccc.ncs.ui.component.TestMusicCard
import com.google.common.util.concurrent.MoreExecutors

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
    val context = LocalContext.current
    var mediaController: MediaController? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            mediaController = controllerFuture.get()
        }, MoreExecutors.directExecutor())
    }


    LazyColumn {
        items(count = testMusics.itemCount) { index ->
            testMusics[index]?.let { music ->
                TestMusicCard(
                    item = music,
                    modifier = Modifier.clickable {
                        mediaController?.run {
                            val mediaItem =
                            MediaItem.Builder()
                                .setUri(music.dataUrl)
                                .setMediaMetadata(MediaMetadata
                                    .Builder()
                                    .setArtist(music.artist)
                                    .setTitle(music.title)
                                    .setArtworkUri(Uri.parse(music.coverUrl))
                                    .build()
                                )
                                .build()
                            setMediaItem(mediaItem)
                            prepare()
                            play()
                        }
                    }
                )
            }
        }
    }
}