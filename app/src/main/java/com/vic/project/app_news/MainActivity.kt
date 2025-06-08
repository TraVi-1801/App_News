package com.vic.project.app_news

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.vic.project.app_news.data.source.remote.network.NetworkMonitor
import com.vic.project.app_news.presentation.navigation.AppComposeNavigator
import com.vic.project.app_news.presentation.navigation.AppNavHost
import com.vic.project.app_news.presentation.navigation.AppScreens
import com.vic.project.app_news.presentation.theme.App_NewsTheme
import com.vic.project.app_news.presentation.theme.DarkAndroidBackgroundTheme
import com.vic.project.app_news.presentation.theme.DarkGreenGray95
import com.vic.project.app_news.presentation.theme.LightAndroidBackgroundTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    internal lateinit var composeNavigator: AppComposeNavigator<AppScreens>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val isOffline = networkMonitor.isOnline.map(Boolean::not).collectAsStateWithLifecycle(
                initialValue = false,
            )

            val darkTheme = isSystemInDarkTheme()

            val navHostController = rememberNavController()

            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        LightAndroidBackgroundTheme.color.toArgb(),
                        DarkAndroidBackgroundTheme.color.toArgb()
                    ) { darkTheme }
                )
                onDispose {}
            }

            val snackbarHostState = remember { SnackbarHostState() }
            val notConnectedMessage = "⚠️ You aren’t connected to the internet"
            LaunchedEffect(isOffline.value) {
                if (isOffline.value) {
                    snackbarHostState.showSnackbar(
                        message = notConnectedMessage,
                        duration = Indefinite,
                    )
                }
            }

            LaunchedEffect(Unit) {
                composeNavigator.handleNavigationCommands(navHostController)
            }

            App_NewsTheme {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    },
                    content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(PaddingValues(bottom = padding.calculateBottomPadding()))
                        ) {
                            AppNavHost(
                                navController = navHostController
                            )
                        }
                    },
                )
            }
        }
    }
}