package com.vic.project.app_news.presentation.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vic.project.app_news.data.model.state.ListState
import com.vic.project.app_news.presentation.navigation.currentComposeNavigator
import com.vic.project.app_news.presentation.screen.home.components.HomeHeader
import com.vic.project.app_news.presentation.screen.home.components.NewsList
import com.vic.project.app_news.presentation.screen.home.components.PagingAnimation
import com.vic.project.app_news.presentation.screen.home.components.RefreshAnimation
import com.vic.project.app_news.presentation.screen.home.components.SearchHistorySection
import com.vic.project.app_news.presentation.viewmodel.HomeEvent
import com.vic.project.app_news.presentation.viewmodel.HomeViewModel
import com.vic.project.app_news.utils.LogUtils.logger
import com.vic.project.app_news.utils.ModifierExtension.clickOutSideToHideKeyBoard

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val composeNavigator = currentComposeNavigator
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val search by remember { derivedStateOf { uiState.search } }
    val currentOnSearch by rememberUpdatedState(newValue = {
        if (search.isNotEmpty()) {
            viewModel.handleEvent(HomeEvent.SearchNews(uiState.search))
        }
    })


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .clickOutSideToHideKeyBoard()
    ) {
        val (header, listRecommence, content, refresh, paging) = createRefs()


        HomeHeader(
            search = search,
            modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onSearch = currentOnSearch,
            onFocusChanged = {
                viewModel.handleEvent(HomeEvent.UpdateFocus(it.hasFocus))
            },
            onSearchValueChanged = {
                viewModel.handleEvent(HomeEvent.UpdateSearch(it))
            }
        )

        RefreshAnimation(
            visible = uiState.pullRefresh && uiState.listState == ListState.LOADING,
            modifier = Modifier.constrainAs(refresh) {
                top.linkTo(header.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        NewsList(
            uiState = uiState,
            modifier = Modifier
                .constrainAs(content) {
                    top.linkTo(refresh.bottom)
                    bottom.linkTo(paging.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            onRetry = {
                // Retry logic here
            },
            onRefresh = {
                viewModel.handleEvent(HomeEvent.PullRefresh)
            },
            onPaginate = {
                viewModel.handleEvent(HomeEvent.GetListNews)
            }
        )

        PagingAnimation(
            visible = uiState.listState == ListState.PAGINATING,
            modifier = Modifier.constrainAs(paging) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        SearchHistorySection(
            visible = uiState.onFocus && uiState.listHistory.isNotEmpty(),
            history = uiState.listHistory,
            modifier = Modifier.constrainAs(listRecommence) {
                top.linkTo(header.bottom, margin = (-8).dp)
                start.linkTo(header.start, margin = 16.dp)
                end.linkTo(header.end, margin = 16.dp)
                width = Dimension.fillToConstraints
            },
            onSearchItemClick = {
                viewModel.handleEvent(HomeEvent.SearchNews(it))
            }
        )
    }
}