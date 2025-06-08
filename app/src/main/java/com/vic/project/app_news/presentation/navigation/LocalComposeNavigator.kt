package com.vic.project.app_news.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

public val LocalComposeNavigator: ProvidableCompositionLocal<AppComposeNavigator<AppScreens>> =
  compositionLocalOf {
      error(
          "No AppComposeNavigator provided! " +
                  "Make sure to wrap all usages of App components in AppTheme.",
      )
  }

/**
 * Retrieves the current [NewsComposeNavigator] at the call site's position in the hierarchy.
 */
public val currentComposeNavigator: AppComposeNavigator<AppScreens>
  @Composable
  @ReadOnlyComposable
  get() = LocalComposeNavigator.current
