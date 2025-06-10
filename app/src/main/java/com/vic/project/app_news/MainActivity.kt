package com.vic.project.app_news

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.vic.project.app_news.data.model.EnumLanguage
import com.vic.project.app_news.data.source.remote.network.NetworkMonitor
import com.vic.project.app_news.presentation.navigation.AppComposeNavigator
import com.vic.project.app_news.presentation.navigation.AppNavHost
import com.vic.project.app_news.presentation.navigation.AppScreens
import com.vic.project.app_news.presentation.navigation.LocalComposeNavigator
import com.vic.project.app_news.presentation.theme.App_NewsTheme
import com.vic.project.app_news.presentation.theme.DarkAndroidBackgroundTheme
import com.vic.project.app_news.presentation.theme.LightAndroidBackgroundTheme
import com.vic.project.app_news.presentation.viewmodel.MainActivityUiState
import com.vic.project.app_news.presentation.viewmodel.MainViewModel
import com.vic.project.app_news.utils.AuthModel
import com.vic.project.app_news.utils.AuthModel.updateStateContext
import com.vic.project.app_news.utils.ContextExtension.LocalAppLocale
import com.vic.project.app_news.utils.ContextExtension.updateLocale
import com.vic.project.app_news.utils.LogUtils.logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    internal lateinit var composeNavigator: AppComposeNavigator<AppScreens>

    private val viewModel: MainViewModel by viewModels()

    // We keep this as a mutable state, so that we can track changes inside the composition.
    var language by mutableStateOf(
        MainActivityUiState.Loading.language
    )

    override fun attachBaseContext(newBase: Context?) {
        val context = newBase?.updateLocale(EnumLanguage.fromLocale(language).localDefault)
        context?.let {
            updateStateContext(it)
        }
        super.attachBaseContext(context)
    }

    @SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val isOffline = networkMonitor.isOnline.map(Boolean::not).collectAsStateWithLifecycle(
                initialValue = false,
            )
            val darkTheme = isSystemInDarkTheme()
            val orientation = resources.configuration.orientation

            DisposableEffect(darkTheme,orientation) {
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
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
                } else {
                    WindowCompat.setDecorFitsSystemWindows(window, true)
                }
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

            LaunchedEffect(orientation) {
                val newContext = baseContext.updateLocale(EnumLanguage.fromLocale(viewModel.uiState.value.language).localDefault)
                updateStateContext(newContext)
            }



            // Update the uiState
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState
                        .filterIsInstance<MainActivityUiState.Success>()
                        .map { it.language }
                        .distinctUntilChanged()
                        .collect { newLanguage ->
                            language = newLanguage
                        }
                }
            }

            // Keep the splash screen on-screen until the UI state is loaded. This condition is
            // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
            // the UI.
            splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.shouldKeepSplashScreen() }

            CompositionLocalProvider(LocalAppLocale provides language) {

                // Pass updated context to Compose
                CompositionLocalProvider(
                    LocalComposeNavigator provides composeNavigator,
                ) {
                    App_NewsTheme {
                        val navHostController = rememberNavController()

                        LaunchedEffect(Unit) {
                            composeNavigator.handleNavigationCommands(navHostController)
                        }

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
    }
}