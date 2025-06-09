package com.vic.project.app_news.presentation.screen.home.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vic.project.app_news.data.model.state.ListState
import com.vic.project.app_news.presentation.components.ImageLoad
import com.vic.project.app_news.presentation.navigation.AppScreens
import com.vic.project.app_news.presentation.navigation.currentComposeNavigator
import com.vic.project.app_news.presentation.viewmodel.HomeEvent
import com.vic.project.app_news.presentation.viewmodel.HomeState
import com.vic.project.app_news.utils.LogUtils.logger
import com.vic.project.app_news.utils.ModifierExtension.clickableSingle
import com.vic.project.app_news.utils.ModifierExtension.shadowCustom
import com.vic.project.app_news.utils.StringExtension.orNullWithHolder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsList(
    uiState: HomeState,
    modifier: Modifier,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onPaginate: () -> Unit
) {
    val composeNavigator = currentComposeNavigator
    val newsState = rememberLazyListState()
    val shouldStartPaginate = remember (uiState.canPaginate){
        derivedStateOf {
            uiState.canPaginate && (newsState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -9) >= (newsState.layoutInfo.totalItemsCount - 10)
        }
    }
    val pullRefreshState = rememberPullToRefreshState()
    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value && uiState.listState == ListState.IDLE) {
            onPaginate.invoke()
        }
    }
    LaunchedEffect(uiState.listNews) {
        if (uiState.listNews.isEmpty()){
            newsState.scrollToItem(0)
        }
    }
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .pullToRefresh(
                isRefreshing = false,
                state = pullRefreshState,
                onRefresh = onRefresh
            ),
        state = newsState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        item {
            Spacer(modifier = Modifier)
        }

        when {
            uiState.listNews.isEmpty() && uiState.page == 1 -> {
                when (uiState.listState) {
                    ListState.LOADING -> item { LoadingPage() }
                    ListState.ERROR -> item { ErrorPage(onRetry) }
                    else -> if (uiState.isNotFound) item { SearchNewsNotFound() } else item { EmptyPage() }
                }
            }

            else -> {
                items(
                    uiState.listNews,
                    key = { it.id }
                ) {
                    val news = remember { it }

                    NewsItem(
                        name = news.title.orNullWithHolder(),
                        description = news.description.orNullWithHolder(),
                        imageUrl = news.urlToImage.orEmpty(),
                        onItemClick = {
                            composeNavigator.navigate(
                                AppScreens.Details(news)
                            )
                        }
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NewsItem(
    name : String,
    description : String,
    imageUrl : String,
    onItemClick : () -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .shadowCustom(
                color = MaterialTheme.colorScheme.onSurface.copy(0.3f),
                offsetY = 0.dp,
                offsetX = 3.dp,
                spread = 0.dp,
                blurRadius = 4.dp,
                borderRadius = 16.dp
            )
            .background(
                MaterialTheme.colorScheme.background,
                RoundedCornerShape(16.dp)
            )
            .clickableSingle { onItemClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        ImageLoad(
            url = imageUrl,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth(0.3f)
                .height(120.dp)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )

        Column (
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.W600,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}