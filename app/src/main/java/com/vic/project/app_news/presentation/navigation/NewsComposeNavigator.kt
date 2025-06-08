package com.vic.project.app_news.presentation.navigation

import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import javax.inject.Inject

class NewsComposeNavigator @Inject constructor() : AppComposeNavigator<AppScreens>() {
    override fun navigate(
        route: AppScreens,
        optionsBuilder: (NavOptionsBuilder.() -> Unit)?
    ) {
        val options = optionsBuilder?.let { navOptions(it) }
        navigationCommands.tryEmit(ComposeNavigationCommand.NavigateToRoute(route, options))
    }

    override fun <R> navigateBackWithResult(
        key: String,
        result: R,
        route: AppScreens?
    ) {
        navigationCommands.tryEmit(
            ComposeNavigationCommand.NavigateUpWithResult(
                key = key,
                result = result,
                route = route,
            ),
        )
    }

    override fun popUpTo(
        route: AppScreens,
        inclusive: Boolean
    ) {
        navigationCommands.tryEmit(ComposeNavigationCommand.PopUpToRoute(route, inclusive))
    }

    override fun navigateAndClearBackStack(route: AppScreens) {
        navigationCommands.tryEmit(
            ComposeNavigationCommand.NavigateToRoute(
                route,
                navOptions {
                    popUpTo(0)
                },
            ),
        )
    }

}