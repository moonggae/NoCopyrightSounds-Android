package com.ccc.ncs.feature.play

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ccc.ncs.BuildConfig
import com.ccc.ncs.R

private const val WIKI_TAG = "wiki"
private const val ANNOTATION_START_TAG = "<annotation>"
private const val ANNOTATION_END_TAG = "</annotation>"

@Composable
fun PlayerMenuLyricsTabView(
    modifier: Modifier = Modifier,
    title: String?,
    lyrics: String?
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (lyrics.isNullOrBlank() && !title.isNullOrBlank()) {
            NoLyricsText(
                modifier = modifier.fillMaxSize(),
                title = title
            )
        } else {
            Text(text = lyrics ?: "", modifier = modifier.fillMaxSize())
        }

        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
    }
}

private fun String.titleToPath(): String =
    this
        .split("(").first().trim()
        .replace(" ", "_")


@Composable
fun NoLyricsText(
    modifier: Modifier = Modifier,
    title: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.player_menu_lyrics_empty),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}


@Composable
private fun createAnnotatedString(formattedString: String, title: String): AnnotatedString {
    val startIndex = formattedString.indexOf(ANNOTATION_START_TAG)
    val endIndex = formattedString.indexOf(ANNOTATION_END_TAG)

    return buildAnnotatedString {
        if (startIndex != -1 && endIndex != -1) {
            val beforeAnnotation = formattedString.substring(0, startIndex)
            val annotationText = formattedString.substring(startIndex + ANNOTATION_START_TAG.length, endIndex)
            val afterAnnotation = formattedString.substring(endIndex + ANNOTATION_END_TAG.length)

            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                append(beforeAnnotation)
            }

            pushStringAnnotation(tag = WIKI_TAG, annotation = "${BuildConfig.FANDOM_URL}/wiki/${title.titleToPath()}")
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(annotationText)
            }
            pop()

            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                append(afterAnnotation)
            }
        } else {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                append(formattedString)
            }
        }
    }
}