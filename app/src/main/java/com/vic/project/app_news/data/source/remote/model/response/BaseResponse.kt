package com.vic.project.app_news.data.source.remote.model.response

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.util.Objects

open class BaseResponse {
    @SerializedName("articles")
    val data: JsonElement? = null
    @SerializedName("messages")
    val message: String = ""
    @SerializedName("code")
    val code: String? = null
    @SerializedName("status")
    val status: String? = null

    fun data(): String {
        return Objects.toString(data)
    }

    val dataObject: JsonObject
        get() = (data)!!.asJsonObject

    val dataArray: JsonArray
        get() = (data)!!.asJsonArray

}