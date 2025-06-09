package com.vic.project.app_news.utils

import java.util.UUID
import kotlin.text.format
import kotlin.text.replace
import kotlin.text.toRegex
import kotlin.text.trim
import kotlin.to

object StringExtension {
    private const val holderForNull = "N/A"
    private const val otherForNull = "-"

    fun String?.orNullWithHolder(): String {
        this ?: return holderForNull
        return this
    }

    fun Any?.orNullWithOther(): String {
        this ?: return otherForNull
        return this.toString()
    }

    fun randomUUID(): String {
        return UUID.randomUUID().toString()
    }
}