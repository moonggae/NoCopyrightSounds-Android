package com.ccc.ncs.feature.music

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.feature.library.detail.CommonAppBar
import com.ccc.ncs.feature.library.detail.CoverImage
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.MusicTag
import com.ccc.ncs.ui.component.mockMusics
import com.ccc.ncs.ui.model.backgroundColor
import com.ccc.ncs.ui.model.color


@Composable
fun MusicDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: MusicDetailViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val musicDetailUiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val uiState = musicDetailUiState) {
        is MusicDetailUiState.Success -> MusicDetailScreen(
            modifier = modifier,
            uiState = uiState,
            onBack = onBack
        )

        else -> {

        }
    }
}

@Composable
internal fun MusicDetailScreen(
    modifier: Modifier = Modifier,
    uiState: MusicDetailUiState.Success,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        CommonAppBar(
            onBack = onBack,
            title = null,
            padding = PaddingValues(
                top = 4.dp,
                bottom = 12.dp
            ),
            onClickMenu = { }
        )

        CoverImage(
            url = uiState.music.coverUrl,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
        )

        MusicDetailTitleText(
            title = uiState.music.title,
            modifier = Modifier.padding(top = 16.dp)
        )

        MusicDetailArtistText(
            artist = uiState.music.artist,
            modifier = Modifier.padding(bottom = 12.dp)
        )


        MusicTagContent(
            musicTags = uiState.music.genres,
            onClick = { },
            modifier = Modifier.padding(vertical = 12.dp)
        )

        MusicTagContent(
            musicTags = uiState.music.moods,
            onClick = { },
//            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
@NonRestartableComposable
fun MusicTagContent(
    modifier: Modifier = Modifier,
    musicTags: Set<MusicTag>,
    onClick: (MusicTag) -> Unit
) {
    val label: String = when {
        musicTags.any { it is Genre } -> stringResource(R.string.Genres)
        musicTags.any { it is Mood } -> stringResource(R.string.Moods)
        else -> stringResource(R.string.Tags)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = NcsTypography.Label.contentLabel.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            musicTags.forEach { tag ->
                Text(
                    text = tag.name,
                    style = NcsTypography.Label.tagButton.copy(
                        color = tag.color()
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            tag
                                .backgroundColor()
                                .copy(alpha = 0.6f)
                        )
                        .clickable(
                            onClick = { onClick(tag) },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(color = tag.color())
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 4.dp
                        )
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MusicDetailTitleText(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        text = title,
        style = NcsTypography.Music.Title.large.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier.basicMarquee()
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MusicDetailArtistText(
    modifier: Modifier = Modifier,
    artist: String
) {
    Text(
        text = artist,
        style = NcsTypography.Music.Artist.medium.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier.basicMarquee()
    )
}


@Preview
@Composable
private fun MusicDetailScreenPreview() {
    val successUiState = remember {
        MusicDetailUiState.Success(
            music = mockMusics.first(),
            lyrics = null
        )
    }

    NcsTheme(darkTheme = true) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            MusicDetailScreen(
                uiState = successUiState,
                onBack = {}
            )
        }
    }
}