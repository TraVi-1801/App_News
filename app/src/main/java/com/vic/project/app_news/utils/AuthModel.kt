package com.vic.project.app_news.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.vic.project.app_news.data.model.EnumLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@SuppressLint("StaticFieldLeak")
object AuthModel {

    private val _baseContext = MutableStateFlow<Context?>(null)
    val baseContext = _baseContext.asStateFlow()
    fun updateStateContext(status: Context) {
        _baseContext.value = status
    }

    var language: String = EnumLanguage.ENGLISH.locale.language

    fun headerWithContentType(): HashMap<String, String> {
        val headers: HashMap<String, String> = HashMap()
        return headers
    }
}