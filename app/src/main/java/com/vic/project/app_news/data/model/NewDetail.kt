package com.vic.project.app_news.data.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Immutable
@Parcelize
@Serializable
data class NewDetail(
    val id: String,
    val title: String? = null,
    val author: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null,
) : Parcelable
