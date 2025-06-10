package com.vic.project.app_news.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vic.project.app_news.data.model.EnumLanguage
import com.vic.project.app_news.domain.repository.UserRepository
import com.vic.project.app_news.presentation.viewmodel.MainActivityUiState.Loading
import com.vic.project.app_news.presentation.viewmodel.MainActivityUiState.Success
import com.vic.project.app_news.utils.AuthModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = userRepository.currentLanguage.map {
        Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState

    data class Success(val data: String) : MainActivityUiState {
        override val language: String = data
    }

    /**
     * Returns `true` if the state wasn't loaded yet and it should keep showing the splash screen.
     */
    fun shouldKeepSplashScreen() = this is Loading

    /**
     * Returns `true` if the dynamic color is disabled.
     */
    val language: String get() = AuthModel.language
}