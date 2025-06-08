package com.vic.project.app_news

import android.content.Context
import androidx.startup.Initializer
import com.vic.project.app_news.data.source.local.preferences.initializer.PreferencesInitializer
import com.vic.project.app_news.BuildConfig
import timber.log.Timber

class AppInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("TimberInitializer is initialized.")
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(
        PreferencesInitializer::class.java,
    )
}