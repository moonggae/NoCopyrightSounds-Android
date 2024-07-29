package com.ccc.ncs.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.ccc.ncs.R

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_loading_3_dots))
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                Color.Black.hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf("**")
        )
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.2f))
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            dynamicProperties = dynamicProperties,
            modifier = Modifier
                .align(Alignment.Center)
                .height(150.dp),
        )
    }
}

@Preview
@Composable
fun LoadingScreenPreview(modifier: Modifier = Modifier) {
    LoadingScreen()
}