package com.vic.project.app_news.data.repository

import com.vic.project.app_news.BuildConfig
import com.vic.project.app_news.data.source.local.preferences.Preferences
import com.vic.project.app_news.data.source.remote.model.request.KeyRequest
import com.vic.project.app_news.data.source.remote.model.response.BaseResponse
import com.vic.project.app_news.data.source.remote.model.response.ResultWrapper
import com.vic.project.app_news.data.source.remote.service.RetrofitService
import com.vic.project.app_news.domain.repository.NewsRepository
import com.vic.project.app_news.utils.AuthModel
import com.vic.project.app_news.utils.FlowUtils.emitLoading
import com.vic.project.app_news.utils.JSON
import com.vic.project.app_news.utils.JSON.toJson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class NewsRepositoryImpl @Inject constructor(
    private val retrofitService: RetrofitService,
    private val preferences: Preferences,
    @Named("io") private val ioDispatcher: CoroutineDispatcher
) : NewsRepository {
    override fun getListNews(
        page: Int, q: String, language: String
    ): Flow<ResultWrapper<BaseResponse>> {
        return flow {
            val response = retrofitService.getMethod(
                headers = AuthModel.headerWithContentType(),
                request = KeyRequest.GET_NEWS,
                message = "q=${q.ifBlank { "ai+OR+war+OR+market+OR+economy" }}&language=${language}&pageSize=20&page=${page}&sortBy=popularity&apiKey=${BuildConfig.API_KEY}",
                codeRequired = KeyRequest.GET_NEWS.codeResponse
            )
            emit(response)
        }.emitLoading().flowOn(ioDispatcher)
    }

    override fun getListHistory(): List<String> {
        return JSON.decodeToList(preferences.listSearch, Array<String>::class.java).orEmpty()
            .take(5)
    }

    override fun addHistory(data: String) {
        val listData =
            JSON.decodeToList(preferences.listSearch, Array<String>::class.java).orEmpty()
                .toMutableList().also {
                    it.remove(data)
                    it.add(0, data)
                }

        preferences.listSearch = listData.toJson()
    }
}