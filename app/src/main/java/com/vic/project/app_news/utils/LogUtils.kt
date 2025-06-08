package com.vic.project.app_news.utils

import android.util.Log
import timber.log.Timber

object LogUtils {
    inline fun Any.logger(
        tag: String? = null,
        level: Int = Log.DEBUG,
        message: () -> String
    ) {
        if (tag != null) {
            Timber.d("$tag | ${message()}")
        } else {
            when (level) {
                Log.DEBUG -> Timber.d("${this.outerClassSimpleTagName()} | ${message()}")
                Log.INFO -> Timber.i("${this.outerClassSimpleTagName()} | ${message()}")
                Log.WARN -> Timber.w("${this.outerClassSimpleTagName()} | ${message()}")
                Log.ERROR -> Timber.e("${this.outerClassSimpleTagName()} | ${message()}")
                Log.VERBOSE -> Timber.v("${this.outerClassSimpleTagName()} | ${message()}")
                else -> Timber.d("${this.outerClassSimpleTagName()} | ${message()}")
            }
        }
    }

    fun plantDebugTree() {
        Timber.plant(Timber.DebugTree())
    }
}

@PublishedApi
internal fun Any.outerClassSimpleTagName(): String {
    val javaClass = this::class.java
    val fullClassName = javaClass.name
    val outerClassName = fullClassName.substringBefore('$')
    val simplerOuterClassName = outerClassName.substringAfterLast('.')
    return if (simplerOuterClassName.isEmpty()) {
        fullClassName
    } else {
        simplerOuterClassName.removeSuffix("Kt")
    }
}