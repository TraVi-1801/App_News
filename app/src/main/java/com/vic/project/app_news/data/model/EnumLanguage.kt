package com.vic.project.app_news.data.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vic.project.app_news.R
import com.vic.project.app_news.utils.AuthModel
import com.vic.project.app_news.utils.ContextExtension.getLocalizedString
import java.util.Locale


enum class EnumLanguage(val locale: Locale, val displayName: String) {
    ENGLISH(Locale("en"), "English"),
    VIETNAMESE(Locale("vi"), "Tiếng Việt"),
    CHINESE_TW(Locale("zh-rTW"), "繁體中文");

    companion object {
        fun fromLocale(language: String): EnumLanguage =
            entries.firstOrNull { it.locale.language == language }
                ?: ENGLISH
    }

    val language : String
        @Composable get() = when(this){
            VIETNAMESE -> getLocalizedString(R.string.vi_language)
            CHINESE_TW -> getLocalizedString(R.string.tw_language)
            else -> getLocalizedString(R.string.english_language)
        }

    val localDefault : Locale
         get() = when(this){
            VIETNAMESE -> {
                Locale("vi")
            }
            CHINESE_TW -> {
                Locale("zh", "TW")
            }
            else -> {
                Locale("en")
            }
        }

    val getAPI : String
         get() = when(this){
            VIETNAMESE -> {
                "vi"
            }
            CHINESE_TW -> {
                "zh"
            }
            else -> {
                "en"
            }
        }
}