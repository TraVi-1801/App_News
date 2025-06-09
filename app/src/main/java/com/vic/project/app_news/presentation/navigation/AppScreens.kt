package com.vic.project.app_news.presentation.navigation

import androidx.navigation.NavType
import com.vic.project.app_news.data.model.NewDetail
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf


sealed interface AppScreens {

    @Serializable
    data object Home : AppScreens

    @Serializable
    data object Setting : AppScreens

    @Serializable
    data class Details(val news: NewDetail) : AppScreens {
        companion object {
            val typeMap = mapOf(typeOf<NewDetail>() to NewsDetailType)
        }
    }

    @Serializable
    data class ReadMore(val url: String) : AppScreens
}