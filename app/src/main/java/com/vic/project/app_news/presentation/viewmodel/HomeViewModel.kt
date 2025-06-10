package com.vic.project.app_news.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.vic.project.app_news.data.model.EnumLanguage
import com.vic.project.app_news.data.model.NewDetail
import com.vic.project.app_news.data.model.state.ListState
import com.vic.project.app_news.data.source.remote.network.NetworkMonitor
import com.vic.project.app_news.domain.repository.NewsRepository
import com.vic.project.app_news.domain.repository.UserRepository
import com.vic.project.app_news.presentation.viewmodel.MainActivityUiState.Loading
import com.vic.project.app_news.presentation.viewmodel.MainActivityUiState.Success
import com.vic.project.app_news.utils.AuthModel
import com.vic.project.app_news.utils.JSON
import com.vic.project.app_news.utils.LogUtils.logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val userRepository: UserRepository,
    networkMonitor: NetworkMonitor
) : BaseViewModel<HomeState, HomeEvent>(HomeState()) {
    val currentLanguage: StateFlow<String> = userRepository.currentLanguage.stateIn(
        scope = viewModelScope,
        initialValue = AuthModel.language,
        started = SharingStarted.WhileSubscribed(5_000),
    )
    init {

        uiState.distinctUntilChangedBy { it.onFocus }.onEach {
                if (it.onFocus) {
                    val list = newsRepository.getListHistory()
                    updateUiState(uiState.value.copy(listHistory = list))
                }
            }.launchIn(viewModelScope)

        networkMonitor.isOnline.distinctUntilChangedBy { it }.onEach {
            if (it && uiState.value.retry) {
                updateUiState(
                    uiState.value.copy(
                        retry = false
                    )
                )
                getImagesTryAgain()
            }
        }.launchIn(viewModelScope)

        currentLanguage
            .distinctUntilChangedBy { it }
            .onEach { language ->
                updateUiState(
                    uiState.value.copy(
                        listNews = emptyList(),
                        page = 1,
                        canPaginate = false,
                        listState = ListState.IDLE
                    )
                )
                getImages()
            }
            .launchIn(viewModelScope)
    }

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.UpdateSearch -> {
                updateUiState(
                    uiState.value.copy(
                        search = event.data
                    )
                )
            }

            HomeEvent.GetListNews -> {
                getImages()
            }

            is HomeEvent.SearchNews -> {
                if (event.data.isNotBlank()) {
                    updateUiState(
                        uiState.value.copy(
                            currentSearch = event.data,
                            search = event.data,
                            page = 1,
                            canPaginate = false,
                            listState = ListState.IDLE,
                            listNews = emptyList(),
                        )
                    )
                    getImages()
                }
            }

            HomeEvent.PullRefresh -> {
                updateUiState(
                    uiState.value.copy(
                        page = 1,
                        canPaginate = false,
                        pullRefresh = true,
                        listState = ListState.IDLE
                    )
                )
                getImages()
            }

            is HomeEvent.UpdateFocus -> {
                updateUiState(
                    uiState.value.copy(
                        onFocus = event.data
                    )
                )
            }

            HomeEvent.OnBackHome -> {
                updateUiState(
                    uiState.value.copy(
                        page = 1,
                        currentSearch = "",
                        search = "",
                        listNews = emptyList(),
                        canPaginate = false,
                        listState = ListState.IDLE
                    )
                )
                getImages()
            }

            HomeEvent.OnTryAgain -> {
                getImagesTryAgain()
            }
        }
    }

    private fun getImagesTryAgain() {
        async {
            newsRepository.getListNews(
                q = uiState.value.currentSearch, page = uiState.value.page, language = EnumLanguage.fromLocale(currentLanguage.value).getAPI
            ).collectNetworkState(showLoading = false, showError = false, doOnError = {
                updateUiState(
                    uiState.value.copy(
                        pullRefresh = false,
                        listState = ListState.ERROR,
                        listNews = if (uiState.value.page == 1) emptyList() else uiState.value.listNews
                    )
                )
            }, doOnTryAgain = {
                updateUiState(
                    uiState.value.copy(
                        retry = true
                    )
                )
            }) {
                handleNewsResponse(it)
            }
        }
    }

    private fun getImages() {
        async {
            if (uiState.value.page == 1 || (uiState.value.page != 1 && uiState.value.canPaginate) && uiState.value.listState == ListState.IDLE) {
                updateUiState(
                    uiState.value.copy(
                        listState = if (uiState.value.page == 1) ListState.LOADING else ListState.PAGINATING,
                    )
                )
                newsRepository.getListNews(
                    q = uiState.value.currentSearch, page = uiState.value.page, language = EnumLanguage.fromLocale(currentLanguage.value).getAPI
                ).collectNetworkState(showLoading = false, showError = false, doOnError = {
                    onNewsLoadError()
                }, doOnTryAgain = {
                    updateUiState(
                        uiState.value.copy(
                            retry = true
                        )
                    )
                }) {
                    handleNewsResponse(it)
                }
            }
        }
    }

    private fun onNewsLoadError() {
        updateUiState(
            uiState.value.copy(
                pullRefresh = false,
                listState = ListState.ERROR,
                listNews = if (uiState.value.page == 1) emptyList() else uiState.value.listNews
            )
        )
    }

    private fun handleNewsResponse(response: String) {
        val results = JSON.decodeToList(
            response, Array<NewDetail>::class.java
        ).orEmpty()
        val state = uiState.value
        val isFirstPage = state.page == 1
        val isNotFound = isFirstPage && results.isEmpty() && state.currentSearch.isNotBlank()

        if (results.isNotEmpty()) {
            if (isFirstPage && state.currentSearch.isNotBlank()) {
                newsRepository.addHistory(state.currentSearch)
            }

            val canPaginate = results.size == 20
            val listData = if (isFirstPage) results else state.listNews + results
            updateUiState(
                uiState.value.copy(
                    listNews = listData,
                    canPaginate = canPaginate,
                    pullRefresh = false,
                    isNotFound = isNotFound,
                    listState = if (canPaginate) ListState.IDLE else ListState.PAGINATION_EXHAUST,
                    page = if (canPaginate) uiState.value.page + 1 else uiState.value.page
                )
            )
        } else {
            updateUiState(
                uiState.value.copy(
                    pullRefresh = false,
                    isNotFound = isNotFound,
                    canPaginate = false,
                    listState = ListState.PAGINATION_EXHAUST,
                )
            )
        }
    }

    override fun onCleared() {
        updateUiState(
            uiState.value.copy(
                page = 1,
                canPaginate = false,
                listState = ListState.IDLE,
            )
        )
        super.onCleared()
    }
}

sealed interface HomeEvent {
    data class UpdateSearch(val data: String) : HomeEvent
    data class UpdateFocus(val data: Boolean) : HomeEvent
    data class SearchNews(val data: String) : HomeEvent
    data object PullRefresh : HomeEvent
    data object OnBackHome : HomeEvent
    data object OnTryAgain : HomeEvent
    data object GetListNews : HomeEvent
}

data class HomeState(
    val search: String = "",
    val currentSearch: String = "",
    val listNews: List<NewDetail> = emptyList(),
    val listHistory: List<String> = emptyList(),
    val onFocus: Boolean = false,
    val retry: Boolean = false,
    val pullRefresh: Boolean = false,
    val isNotFound: Boolean = false,
    val page: Int = 1,
    val canPaginate: Boolean = false,
    val listState: ListState = ListState.IDLE
)