package com.vic.project.app_news.data.model.state

sealed interface NetworkResponseState {
    object Nothing : NetworkResponseState
    object Loading : NetworkResponseState
    class Success(val message: String? = null) : NetworkResponseState
    class Error(val code : String? = null,val message: String? = null) : NetworkResponseState
}