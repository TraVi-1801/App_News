package com.vic.project.app_news.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.vic.project.app_news.data.model.NewDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<NewsDetailState, NewsDetailEvent>(NewsDetailState()) {
    private val data = savedStateHandle.get<NewDetail>("news")

    init {
        handleEvent(
            NewsDetailEvent.UpdateNews(
                data
            )
        )
    }

    override fun handleEvent(event: NewsDetailEvent) {
        when (event) {
            is NewsDetailEvent.UpdateNews -> {
                updateUiState(
                    uiState.value.copy(
                        data = event.data
                    )
                )
            }
        }
    }
}

sealed interface NewsDetailEvent {
    data class UpdateNews(val data: NewDetail?) : NewsDetailEvent
}

data class NewsDetailState(
    val data: NewDetail? = null
)