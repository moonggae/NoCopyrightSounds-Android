package com.ccc.ncs.designsystem.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


object NcsTypography {
    val Music = MusicTypography
    val Search = SearchTypography
    val Label = LabelTypography

    object MusicTypography {
        val Title = TitleTypography
        val Artist = ArtistTypography

        object TitleTypography {
            val large = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            )
            val medium = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            )
            val small = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 17.sp,
                letterSpacing = 0.5.sp
            )
        }

        object ArtistTypography {
            val large = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp
            )
            val medium = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp
            )
            val small = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 17.sp,
                letterSpacing = 0.25.sp
            )
        }
    }

    object SearchTypography {
        val placeholder = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
        val content = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
    }

    object LabelTypography {
        val button = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
            textAlign = TextAlign.Center
        )

        val navigationLabel = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 14.sp,
            letterSpacing = 0.1.sp,
            textAlign = TextAlign.Center
        )
    }
}