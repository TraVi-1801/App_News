package com.vic.project.app_news.domain.repository

import com.vic.project.app_news.data.source.remote.model.response.BaseResponse
import com.vic.project.app_news.data.source.remote.model.response.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getListNews(page: Int, q: String, language: String): Flow<ResultWrapper<BaseResponse>>
    fun getListHistory(): List<String>
    fun addHistory(data: String)
}