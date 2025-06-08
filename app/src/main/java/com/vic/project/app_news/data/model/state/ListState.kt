package com.vic.project.app_news.data.model.state

enum class ListState {
    IDLE,
    LOADING,
    PAGINATING,
    ERROR,
    PAGINATION_EXHAUST,
}