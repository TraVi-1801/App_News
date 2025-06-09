package com.vic.project.app_news.data.source.remote.model.request


const val RESPONSE_OK = "200"

enum class KeyRequest(val url: String, val codeResponse: String = RESPONSE_OK) {

    GET_NEWS("everything?"),
}