package com.vic.project.app_news.di

import android.app.Application
import android.content.Context
import com.vic.project.app_news.data.repository.NewsRepositoryImpl
import com.vic.project.app_news.data.repository.UserRepositoryImpl
import com.vic.project.app_news.data.source.remote.network.ConnectivityManagerNetworkMonitor
import com.vic.project.app_news.data.source.remote.network.NetworkMonitor
import com.vic.project.app_news.domain.repository.NewsRepository
import com.vic.project.app_news.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun provideContext(application: Application): Context


    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor



    @Singleton
    @Binds
    fun bindsNewsRepository(
        newsRepository: NewsRepositoryImpl
    ): NewsRepository

    @Singleton
    @Binds
    fun bindsUserRepository(
        userRepository: UserRepositoryImpl
    ): UserRepository
}
