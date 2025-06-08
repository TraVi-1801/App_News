package com.vic_project.search_image.data.remote.models.request


const val RESPONSE_OK = "200"

enum class KeyRequest(val url: String, val codeResponse: String = RESPONSE_OK) {

    RECOMMEND_KEY("https://www.pexels.com/en-us/api/v3/search/suggestions/"),
    IMAGE_KEY("search?"),
}