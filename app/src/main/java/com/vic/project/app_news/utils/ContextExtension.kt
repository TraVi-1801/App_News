package com.vic.project.app_news.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.annotation.StringRes
import androidx.compose.runtime.compositionLocalOf
import com.vic.project.app_news.data.model.EnumLanguage
import java.util.Locale

object ContextExtension {

    fun getLocalizedString(@StringRes resId: Int): String {
        return AuthModel.baseContext.value?.getString(resId) ?: run {
            ""
        }
    }

    fun Context.findActivity(): Activity {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        throw IllegalStateException("no activity")
    }

    val LocalAppLocale = compositionLocalOf { EnumLanguage.ENGLISH.locale.language }

    fun Context.updateLocale(locale: Locale): Context {
        val locale = locale
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        return createConfigurationContext(config)
    }
}