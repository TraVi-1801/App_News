package com.vic.project.app_news.viewmodel

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.vic.project.app_news.data.source.remote.model.response.BaseResponse
import com.vic.project.app_news.data.source.remote.model.response.ResultWrapper
import com.vic.project.app_news.data.source.remote.network.NetworkMonitor
import com.vic.project.app_news.domain.repository.NewsRepository
import com.vic.project.app_news.domain.repository.UserRepository
import com.vic.project.app_news.presentation.viewmodel.HomeEvent
import com.vic.project.app_news.presentation.viewmodel.HomeViewModel
import com.vic.project.app_news.testutils.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: HomeViewModel
    private val newsRepository: NewsRepository = mockk(relaxed = true)
    private val networkMonitor: NetworkMonitor = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)


    @Before
    fun setup() {
        // JSON tương tự từ API thực tế
        val jsonString = """
            {
              "status": "ok",
              "totalResults": 9440,
              "articles": [
                {
                  "source": {
                    "id": null,
                    "name": "Itainews.com"
                  },
                  "author": "damono3856",
                  "title": "【架空融資】顧客名義の口座を無断で偽造",
                  "description": "1 名前：蚤の市 ★：2025/05/19(月)...",
                  "url": "https://itainews.com/archives/2048585.html",
                  "urlToImage": "https://livedoor.blogimg.jp/.../615375d9-s.png",
                  "publishedAt": "2025-05-19T12:01:24Z",
                  "content": "..."
                }
              ]
            }
        """.trimIndent()

        // Parse JSON
        val jsonElement: JsonElement = JsonParser.parseString(jsonString)
        val articlesJson: JsonArray = jsonElement.asJsonObject.getAsJsonArray("articles")

        // Mock BaseResponse
        val fakeResponse = mockk<BaseResponse>(relaxed = true)
        every { fakeResponse.data } returns articlesJson
        every { fakeResponse.dataArray } returns articlesJson

        // Mock Repository
        coEvery { newsRepository.getListNews(any(), any(), any()) } returns flowOf(
            ResultWrapper.Success(
                fakeResponse
            )
        )
        every { newsRepository.getListHistory() } returns listOf("history1", "history2")
        every { userRepository.currentLanguage } returns MutableStateFlow("en")

        // Mock Network Monitor
        every { networkMonitor.isOnline } returns flowOf(true)

        // Init ViewModel
        viewModel = HomeViewModel(newsRepository, userRepository, networkMonitor)
    }

    @Test
    fun `UpdateFocus true should load history from repository`() = runTest {
        viewModel.handleEvent(HomeEvent.UpdateFocus(true))
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertEquals(listOf("history1", "history2"), state.listHistory)
    }

    @Test
    fun `UpdateSearch should update search text in state`() = runTest {
        viewModel.handleEvent(HomeEvent.UpdateSearch("bitcoin"))
        assertEquals("bitcoin", viewModel.uiState.value.search)
    }

    @Test
    fun `SearchNews should reset and trigger fetch`() = runTest {
        viewModel.handleEvent(HomeEvent.SearchNews("japan"))

        val state = viewModel.uiState.value
        assertEquals("japan", state.search)
        assertEquals("japan", state.currentSearch)
        assertEquals(1, state.page)
    }

    @Test
    fun `PullRefresh should reset pagination and reload data`() = runTest {
        viewModel.handleEvent(HomeEvent.PullRefresh)

        val state = viewModel.uiState.value
        assertEquals(1, state.page)
        assertEquals(true, state.pullRefresh)
    }

    @Test
    fun `GetListNews should fetch news and update state`() = runTest {

        viewModel.handleEvent(HomeEvent.GetListNews)

        val state = viewModel.uiState.value
        assertEquals(false, state.pullRefresh)
        assertEquals(false, state.isNotFound)
    }
}