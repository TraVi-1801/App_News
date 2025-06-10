package com.vic.project.app_news.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.vic.project.app_news.domain.repository.UserRepository
import com.vic.project.app_news.utils.AuthModel
import com.vic.project.app_news.utils.LogUtils.logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<SettingState, SettingEvent>(SettingState()) {

    init {
        observeCurrentLanguage()
    }

    private fun observeCurrentLanguage() {
        userRepository.currentLanguage
            .onEach { language ->
                updateUiState(
                    uiState.value.copy(
                        currentLanguage = language
                    )
                )
            }
            .launchIn(viewModelScope)
    }

    override fun handleEvent(event: SettingEvent) {
        when (event) {
            is SettingEvent.UpdateLanguage -> {
                updateLanguage(event.data)
            }
            is SettingEvent.SelectLanguage -> {
                updateUiState(
                    uiState.value.copy(
                        currentLanguage = event.data
                    )
                )
            }
        }
    }

    private fun updateLanguage(language: String) {
        async {
            userRepository.setLanguage(language)
        }
    }
}

sealed interface SettingEvent {
    data class UpdateLanguage(val data: String) : SettingEvent
    data class SelectLanguage(val data: String) : SettingEvent
}

data class SettingState(
    val currentLanguage: String = AuthModel.language
)