package com.vic.project.app_news.data.source.local.preferences.initializer

import android.content.Context
import androidx.startup.Initializer
import com.vic.project.app_news.data.source.local.preferences.Preferences
import com.vic.project.app_news.di.preferences.PreferencesEntryPoint
import com.vic.project.app_news.utils.AuthModel
import javax.inject.Inject

class PreferencesInitializer : Initializer<Unit> {

    @set:Inject
    internal lateinit var preferences: Preferences

    override fun create(context: Context) {
        PreferencesEntryPoint.resolve(context).inject(this)
        AuthModel.language = preferences.language
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf()
}