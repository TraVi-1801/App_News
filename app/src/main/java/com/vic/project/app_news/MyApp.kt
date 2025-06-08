package com.vic.project.app_news

import android.app.Application
import com.vic.project.app_news.utils.AppConstants
import com.vic.project.app_news.utils.LogUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            LogUtils.plantDebugTree()
        }
        AppConstants.VERSION_NAME = BuildConfig.VERSION_NAME
    }
}