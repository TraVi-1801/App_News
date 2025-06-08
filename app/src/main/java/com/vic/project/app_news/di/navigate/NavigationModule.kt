package com.vic.project.app_news.di.navigate

import com.vic.project.app_news.presentation.navigation.AppComposeNavigator
import com.vic.project.app_news.presentation.navigation.AppScreens
import com.vic.project.app_news.presentation.navigation.NewsComposeNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface NavigationModule {

  @Binds
  @Singleton
  fun provideComposeNavigator(
    searchImageComposeNavigator: NewsComposeNavigator,
  ): AppComposeNavigator<AppScreens>
}
