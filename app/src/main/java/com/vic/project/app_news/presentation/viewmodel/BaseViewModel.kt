package com.vic.project.app_news.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.vic.project.app_news.data.source.remote.model.response.BaseResponse
import com.vic.project.app_news.data.source.remote.model.response.ResultWrapper
import com.vic.project.app_news.data.model.state.NetworkResponseState
import com.vic.project.app_news.utils.LogUtils.logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class BaseViewModel<State : Any, Event>(initialState: State) : ViewModel() {
    private val handler = CoroutineExceptionHandler { _, exception ->
        logger("CoroutineExceptionHandler", level = Log.ERROR) {
            "CoroutineExceptionHandler got $exception"
        }
    }
    private val viewModelJob = SupervisorJob()
    val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob + handler)
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob + handler)

    private val _networkResponseState =
        MutableStateFlow<NetworkResponseState>(NetworkResponseState.Nothing)
    val networkResponseState: StateFlow<NetworkResponseState> = _networkResponseState.asStateFlow()

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    open fun handleEvent(event: Event) {
        // function for handle view's event
    }

    open fun updateNetworkResponseState(networkResponseState: NetworkResponseState) {
        _networkResponseState.update {
            networkResponseState
        }
    }

    open fun updateUiState(uiState: State) {
        _uiState.update { uiState }
    }

    fun Flow<ResultWrapper<BaseResponse>>.collectNetworkState(
        showLoading: Boolean = true,
        showError: Boolean = true,
        excludeErrorCode: List<String> = emptyList(),
        doOnError: (ResultWrapper.Error) -> Unit = {},
        doOnTryAgain: () -> Unit = {},
        doOnSuccess: (String) -> Unit,
    ) {
        async {
            collect {
                when (it) {
                    is ResultWrapper.Success -> {
                        doOnSuccess.invoke(it.value.data())
                        updateNetworkResponseState(NetworkResponseState.Success())
                    }

                    is ResultWrapper.Error -> {
                        if (it.code == "NO_INTERNET"){
                            doOnTryAgain.invoke()
                            updateNetworkResponseState(NetworkResponseState.Nothing)
                        } else {
                            doOnError.invoke(it)
                            if (showError && excludeErrorCode.contains(it.code).not()) {
                                updateNetworkResponseState(
                                    NetworkResponseState.Error(
                                        it.code,
                                        it.message
                                    )
                                )
                            } else {
                                updateNetworkResponseState(NetworkResponseState.Nothing)
                            }
                        }
                    }

                    is ResultWrapper.Loading -> {
                        if (showLoading) {
                            updateNetworkResponseState(NetworkResponseState.Loading)
                        } else {
                            updateNetworkResponseState(NetworkResponseState.Nothing)
                        }
                    }
                }
            }
        }
    }

    fun Flow<ResultWrapper<BaseResponse>>.collectNetworkStateOther(
        doOnError: (ResultWrapper.Error) -> Unit = {},
        doOnLoading: () -> Unit = {},
        doOnTryAgain: () -> Unit = {},
        doOnSuccess: (String) -> Unit
    ) {
        async {
            collect {
                when (it) {
                    is ResultWrapper.Success -> {
                        doOnSuccess.invoke(it.value.data())
                    }

                    is ResultWrapper.Error -> {
                        if (it.code == "NO_INTERNET"){
                            doOnTryAgain.invoke()
                            updateNetworkResponseState(NetworkResponseState.Nothing)
                        } else {
                            doOnError.invoke(it)
                        }
                    }

                    is ResultWrapper.Loading -> {
                        doOnLoading.invoke()
                    }
                }
            }
        }
    }

    protected inline fun async(crossinline block: suspend () -> Unit) =
        ioScope.launch {
            block()
        }

    protected inline fun asyncUi(crossinline block: suspend () -> Unit) =
        uiScope.launch {
            block()
        }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}