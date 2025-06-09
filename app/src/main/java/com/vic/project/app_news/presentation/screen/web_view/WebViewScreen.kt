package com.vic.project.app_news.presentation.screen.web_view

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.vic.project.app_news.R
import com.vic.project.app_news.presentation.navigation.currentComposeNavigator
import com.vic.project.app_news.utils.ModifierExtension.clickableSingle

@Composable
fun WebViewScreen(
    url: String,
) {
    val composeNavigator = currentComposeNavigator
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val webViewState = remember(url) {
            mutableStateOf(url)
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.apply {
                        javaScriptEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        domStorageEnabled = true
                        setSupportZoom(true)
                    }
                    loadUrl(webViewState.value)
                }
            },
            update = { webView ->
                if (webView.url != webViewState.value) {
                    webView.loadUrl(webViewState.value)
                }
            }
        )
        Icon(
            painter = painterResource(R.drawable.ic_close),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .size(32.dp)
                .clickableSingle {
                    composeNavigator.navigateUp()
                }
                .background(MaterialTheme.colorScheme.background, CircleShape)
                .padding(4.dp)
        )
    }
}