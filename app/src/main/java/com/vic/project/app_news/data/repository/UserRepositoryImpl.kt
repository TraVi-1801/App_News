package com.vic.project.app_news.data.repository

import android.content.SharedPreferences
import com.vic.project.app_news.data.source.local.preferences.Preferences
import com.vic.project.app_news.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(
    private val preferences: Preferences,
    @Named("io") private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    override val currentLanguage: Flow<String> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == Preferences.CURRENT_LANGUAGE) {
                trySend(preferences.language)
            }
        }

        // Emit the current value initially
        trySend(preferences.language)

        preferences.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            preferences.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }.conflate()

    override fun setLanguage(data: String) {
        preferences.language = data
    }
}