package com.vic.project.app_news.presentation.screen.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Visibility
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.vic.project.app_news.R
import com.vic.project.app_news.data.model.state.ListState

@Composable
fun RefreshAnimation(
    modifier: Modifier = Modifier,
    visible: Boolean,
) {
    val refreshAnimation by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.refresh))

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
        ) {
            LottieAnimation(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(0.2f)
                    .aspectRatio(1f),
                composition = refreshAnimation,
                isPlaying = true,
                iterations = LottieConstants.IterateForever,
                contentScale = ContentScale.Crop,
            )
        }
    }
}