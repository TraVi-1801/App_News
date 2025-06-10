package com.vic.project.app_news.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val currentLanguage: Flow<String>
    fun setLanguage(data: String)
}