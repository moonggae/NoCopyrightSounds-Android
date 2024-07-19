package com.ccc.ncs.feature.artist.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.ccc.ncs.R
import com.ccc.ncs.designsystem.component.CommonAppBar
import com.ccc.ncs.designsystem.component.ListItemCardDefaults
import com.ccc.ncs.designsystem.theme.NcsTheme
import com.ccc.ncs.designsystem.theme.NcsTypography
import com.ccc.ncs.feature.music.ContentLabelText
import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.ArtistDetail
import com.ccc.ncs.model.Music
import com.ccc.ncs.ui.component.ArtistListCard
import com.ccc.ncs.ui.component.MusicCard
import com.ccc.ncs.ui.component.mockMusics


@Composable
fun ArtistDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: ArtistDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onMoveToMusicDetail: (Music) -> Unit,
    onMoveToArtistDetail: (Artist) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is ArtistDetailUiState.Success -> {
            ArtistDetailScreen(
                modifier = modifier,
                artistDetail = state.artistDetail,
                onBack = onBack,
                onClickMusic = onMoveToMusicDetail,
                onClickArtist = onMoveToArtistDetail
            )
        }

        else -> {
            // Handle other states
        }
    }
}

@Composable
internal fun ArtistDetailScreen(
    modifier: Modifier = Modifier,
    artistDetail: ArtistDetail,
    onBack: () -> Unit,
    onClickMusic: (Music) -> Unit,
    onClickArtist: (Artist) -> Unit
) {
    ArtistDetailLayout(
        imageUrl = artistDetail.artist.photoUrl,
        name = artistDetail.artist.name,
        tags = artistDetail.artist.tags,
        onBack = onBack
    ) {
        Column(
            Modifier
                .padding(
                    horizontal = 20.dp,
                    vertical = 32.dp
                )
                .fillMaxWidth()
        ) {
            ArtistDetailPopularTracks(
                musics = artistDetail.musics,
                onClick = onClickMusic
            )

            ArtistDetailSimilarTagArtists(
                artists = artistDetail.similarArtists,
                onClick = onClickArtist,
                modifier = Modifier.padding(vertical = 20.dp)
            )
        }
    }
}

@Composable
private fun ArtistDetailPopularTracks(
    modifier: Modifier = Modifier,
    musics: List<Music>,
    onClick: (Music) -> Unit
) {
    Column(modifier) {
        ContentLabelText(
            text = "Popular Tracks",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Column {
            musics.forEach { music ->
                MusicCard(
                    item = music,
                    onClick = onClick,
                    suffix = {},
                    style = ListItemCardDefaults.listItemCardStyle.small(),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
    }
}


@Composable
private fun ArtistDetailSimilarTagArtists(
    modifier: Modifier = Modifier,
    artists: List<Artist>,
    onClick: (Artist) -> Unit
) {
    Column(modifier) {
        ContentLabelText(
            text = "Similar Tag Artists",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Column {
            artists.forEach { artist ->
                ArtistListCard(
                    item = artist,
                    showDivider = false,
                    style = ListItemCardDefaults.listItemCardStyle.small(),
                    onClick = onClick,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
    }
}


@Composable
private fun ArtistDetailLayout(
    imageUrl: String?,
    name: String,
    tags: String,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val scrollState = rememberScrollState()
    val statusBarTopPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val coverPaddingSize = calculatePaddingSize(screenWidth, density)
    val coverVisibility = calculateCoverVisibility(density, coverPaddingSize, statusBarTopPadding, scrollState)

    Column {
        Box {
            ArtistImage(
                imageUrl = imageUrl,
                paddingSize = coverPaddingSize,
                coverVisibility = coverVisibility
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(coverPaddingSize))
                ArtistInfo(
                    name = name,
                    tags = tags,
                    coverVisibility = coverVisibility
                )
                content()
            }
            ArtistAppBar(
                name = name,
                coverVisibility = coverVisibility,
                onBack = onBack
            )
        }
    }
}

@Composable
private fun ArtistImage(
    imageUrl: String?,
    paddingSize: Dp,
    coverVisibility: Float
) {
    val surfaceColor = MaterialTheme.colorScheme.surface

    Image(
        painter = rememberAsyncImagePainter(
            model = imageUrl,
            placeholder = painterResource(R.drawable.ncs_cover),
            fallback = painterResource(R.drawable.ncs_cover)
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .drawWithCache {
                val gradient = Brush.verticalGradient(
                    colors = listOf(surfaceColor.copy(alpha = 0.1f), surfaceColor),
                    startY = 0f,
                    endY = paddingSize.toPx() * coverVisibility
                )
                onDrawWithContent {
                    drawContent()
                    drawRect(gradient, blendMode = BlendMode.SrcAtop)
                }
            }
    )
}

@Composable
private fun ArtistInfo(
    name: String,
    tags: String,
    coverVisibility: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = name,
            style = NcsTypography.ArtistDetail.name.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = coverVisibility)
            )
        )
        Text(
            text = tags,
            style = NcsTypography.ArtistDetail.tags.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = coverVisibility)
            )
        )
    }
}

@Composable
private fun ArtistAppBar(
    name: String,
    coverVisibility: Float,
    onBack: () -> Unit
) {
    CommonAppBar(
        padding = PaddingValues(
            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp,
            bottom = 20.dp,
            start = 12.dp,
            end = 12.dp
        ),
        onBack = onBack,
        modifier = Modifier.background(if (coverVisibility == 0f) MaterialTheme.colorScheme.surface else Color.Transparent)
    ) {
        Text(
            text = name,
            style = NcsTypography.Label.appbarTitle.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f - coverVisibility)
            )
        )
    }
}

private fun calculatePaddingSize(screenWidth: Dp, density: Density): Dp {
    return screenWidth - with(density) {
        NcsTypography.ArtistDetail.name.fontSize.toDp() + NcsTypography.ArtistDetail.tags.fontSize.toDp()
    }
}

private fun calculateCoverVisibility(
    density: Density,
    paddingSize: Dp,
    statusBarTopPadding: Dp,
    scrollState: ScrollState
): Float {
    return ((with(density) { paddingSize.toPx() - statusBarTopPadding.toPx() } - scrollState.value) /
            with(density) { paddingSize.toPx() - statusBarTopPadding.toPx() }).coerceIn(0f, 1f)
}

@Preview
@Composable
private fun ArtistDetailScreenPreview() {
    NcsTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize()) {
            val artist = Artist(
                detailUrl = "",
                tags = "Drum & Bass",
                name = "Sunny Lukas",
                photoUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/artists/000/001/121/1000x0/1718124757_xj0GFTsmfd_image_123650291-1.JPG"
            )
            ArtistDetailScreen(
                artistDetail = ArtistDetail(
                    artist = artist,
                    similarArtists = listOf(artist),
                    musics = mockMusics,
                ),
                onBack = {},
                onClickMusic = {},
                onClickArtist = {}
            )
        }
    }
}