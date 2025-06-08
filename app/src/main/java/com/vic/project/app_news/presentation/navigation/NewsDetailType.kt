package com.vic.project.app_news.presentation.navigation

import android.net.Uri
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.navigation.NavType
import com.vic.project.app_news.data.model.NewDetail
import kotlinx.serialization.json.Json

object NewsDetailType : NavType<NewDetail>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): NewDetail? =
        BundleCompat.getParcelable(bundle, key, NewDetail::class.java)

    override fun parseValue(value: String): NewDetail {
        return Json.Default.decodeFromString(Uri.decode(value))
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: NewDetail
    ) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: NewDetail): String =
        Uri.encode(Json.Default.encodeToString(value))
}