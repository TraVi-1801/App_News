package com.vic.project.app_news.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.vic.project.app_news.presentation.navigation.AppScreens.Details
import com.vic.project.app_news.presentation.navigation.AppScreens.Home
import com.vic.project.app_news.presentation.navigation.AppScreens.ReadMore
import com.vic.project.app_news.presentation.screen.detail.DetailScreen
import com.vic.project.app_news.presentation.screen.home.HomeScreen
import com.vic.project.app_news.presentation.screen.web_view.WebViewScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier,
    ) {

        composable<Home> {
            HomeScreen()
        }

        composable<AppScreens.Setting> {

        }

        composable<Details>(
            typeMap = Details.typeMap,
        ) {
            DetailScreen()
        }

        composable<ReadMore>{
            val args = it.toRoute<ReadMore>()
            WebViewScreen(
               url = args.url
            )
        }
    }
}