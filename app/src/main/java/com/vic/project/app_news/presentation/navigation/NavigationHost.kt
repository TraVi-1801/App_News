package com.vic.project.app_news.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vic.project.app_news.presentation.navigation.AppScreens.Details
import com.vic.project.app_news.presentation.navigation.AppScreens.Home

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

        }

        composable<AppScreens.Setting> {

        }

        composable<Details>(
            typeMap = Details.typeMap,
        ) {

        }
    }
}