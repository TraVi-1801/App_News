package com.vic.project.app_news.data.source.local.preferences

import android.content.SharedPreferences
import com.vic.project.app_news.data.model.EnumLanguage
import com.vic.project.app_news.data.source.local.preferences.delegate.stringPreferences
import javax.inject.Inject

class Preferences @Inject constructor(
    val sharedPreferences: SharedPreferences
) {

    var listSearch by stringPreferences(
        key = LIST_SEARCH,
        defaultValue = ""
    )

    var language by stringPreferences(
        key = CURRENT_LANGUAGE,
        defaultValue = EnumLanguage.ENGLISH.locale.language
    )

    companion object {
        const val LIST_SEARCH: String = "key_list_search"
        const val CURRENT_LANGUAGE: String = "key_language"
    }
}

val String.Companion.Empty
    inline get() = ""

val Boolean.Companion.BolDefault
    inline get() = false
