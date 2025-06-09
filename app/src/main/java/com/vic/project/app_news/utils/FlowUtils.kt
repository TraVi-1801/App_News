package com.vic.project.app_news.utils

import com.vic.project.app_news.data.model.state.NetworkResponseState
import com.vic.project.app_news.data.source.remote.model.response.BaseResponse
import com.vic.project.app_news.data.source.remote.model.response.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart


object FlowUtils {
    fun Flow<ResultWrapper<BaseResponse>>.emitLoading(): Flow<ResultWrapper<BaseResponse>> {
        return onStart { emit(ResultWrapper.Loading) }
    }

    suspend fun Flow<ResultWrapper<BaseResponse>>.useDefaultCollector(
        _commonUiState: MutableStateFlow<NetworkResponseState>,
        doOnSuccess: (String) -> Unit
    ) {
        collect {
            when (it) {
                is ResultWrapper.Success -> {
                    doOnSuccess.invoke(it.value.data())
                    _commonUiState.value = NetworkResponseState.Success()
                }

                is ResultWrapper.Error -> {
                    _commonUiState.value = NetworkResponseState.Error(it.code, it.message)
                }

                is ResultWrapper.Loading -> {
                    _commonUiState.value = NetworkResponseState.Loading
                }
            }
        }
    }
}