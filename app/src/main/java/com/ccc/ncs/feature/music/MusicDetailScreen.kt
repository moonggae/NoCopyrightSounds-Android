package com.ccc.ncs.feature.music

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.icon.NcsIcons
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.feature.library.detail.CommonAppBar
import com.ccc.ncs.feature.library.detail.CoverImage
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.MusicTag
import com.ccc.ncs.ui.component.mockMusics
import com.ccc.ncs.util.conditional
import com.ccc.ncs.util.toString
import java.time.LocalDate


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
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        CommonAppBar(
            onBack = onBack,
            title = null,
            padding = PaddingValues(
                top = 12.dp,
                bottom = 16.dp
            ),
            onClickMenu = { }
        )

        CoverImage(
            url = uiState.music.coverUrl,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
        )

        MusicDetailTitleText(
            title = uiState.music.title,
            modifier = Modifier.padding(top = 24.dp)
        )

        MusicDetailArtistText(artist = uiState.music.artist)

        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 12.dp
            )
        )

        MusicDetailReleaseDateText(releaseDate = uiState.music.releaseDate)

        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 15.dp
            )
        )

        uiState.lyrics?.let {
            MusicDetailLyrics(
                lyrics = uiState.lyrics,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }

        MusicTagContent(
            musicTags = uiState.music.genres,
            onClick = { },
            modifier = Modifier.padding(bottom = 28.dp)
        )

        MusicTagContent(
            musicTags = uiState.music.moods,
            onClick = { },
        )

        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        Spacer(Modifier.padding(bottom = 48.dp))
    }
}

@Composable
private fun MusicDetailLyrics(
    modifier: Modifier = Modifier,
    lyrics: String
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ContentLabelText(text = "Lyrics")
            Icon(
                imageVector = NcsIcons.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { expanded = !expanded }
                    .rotate(if (expanded) 180f else 0f)
            )
        }


        Text(
            text = lyrics,
            style = NcsTypography.Music.Lyrics.musicDetail.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .animateContentSize()
                .conditional(!expanded) {
                    height(36.dp)
                }
        )
    }
}

@Composable
private fun ContentLabelText(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(modifier = modifier.height(29.dp)) {
        Text(
            text = text,
            style = NcsTypography.Label.contentLabel.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
@NonRestartableComposable
private fun MusicTagContent(
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
        ContentLabelText(
            text = label,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            musicTags.forEach { tag ->
                Text(
                    text = "#${tag.name}",
                    style = NcsTypography.Label.tagButton.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(onClick = { onClick(tag) })
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
    Box(modifier = modifier.height(29.dp)) {
        Text(
            text = title,
            style = NcsTypography.Music.Title.large.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .basicMarquee()
                .align(Alignment.CenterStart)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MusicDetailArtistText(
    modifier: Modifier = Modifier,
    artist: String
) {
    Box(modifier = modifier.height(29.dp)) {
        Text(
            text = artist,
            style = NcsTypography.Music.Artist.large.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier
                .basicMarquee()
                .align(Alignment.TopStart)
        )
    }
}

@Composable
private fun MusicDetailReleaseDateText(
    modifier: Modifier = Modifier,
    releaseDate: LocalDate
) {
    Text(
        text = releaseDate.toString("dd MMM yyyy"),
        style = NcsTypography.Music.ReleaseData.large.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.End
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Preview
@Composable
private fun MusicDetailScreenPreview() {
    val successUiState = remember {
        MusicDetailUiState.Success(
            music = mockMusics.first(),
            lyrics = """
                In the still of the night
                Gazing up at the sky
                When the stars began to fade
                Dimming lanterns in our eyes
                Thought there was nothing to find
                I stopped searching for a reason
                Just let the world embrace us
                In the silence we are one
                 
                Can't escape from this feeling inside you
                Released from heart ache, you'll be fine
                When the cracks start to let your own light through
                I'll be there to help you shine
                 
                I used to come here all the time in late July
                To send a wish into the night
                That maybe when one day when we leave the hurt behind
                We'll fight our way to make our hearts burn bright
                Like a comet in the sky
                 
                (I'll be there to help you—)
                 
                I'll promise you this
                As long as you're looking, you'll see the signs
                Just for a moment, a flash of light
                You'll never forget, I heard you crying aloud
                And a part of me cried with you
                I'd finally see why all the meteors fall out of our view
                 
                Can't escape from this pressure inside you
                But tonight the stars align
                When the cracks start to let your own light through
                I'll be there to help you shine
                 
                I used to come here all the time in late July (late July)
                To send a wish into the night
                That maybe when one day when we leave the hurt behind
                We'll find a way to make our hearts burn bright
                Like a comet in the sky
                 
                (Like a comet in the sky)
            """.trimIndent()
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