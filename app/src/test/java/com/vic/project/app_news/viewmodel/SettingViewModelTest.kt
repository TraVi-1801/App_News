package com.vic.project.app_news.viewmodel

import com.vic.project.app_news.domain.repository.UserRepository
import com.vic.project.app_news.presentation.viewmodel.SettingEvent
import com.vic.project.app_news.presentation.viewmodel.SettingViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingViewModelTest {

    private lateinit var viewModel: SettingViewModel

    private val userRepository: UserRepository = mockk(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Fake language flow
        every { userRepository.currentLanguage } returns MutableStateFlow("en")

        viewModel = SettingViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `observeCurrentLanguage should update uiState with emitted language`() = runTest {
        val flow = MutableStateFlow("en")
        every { userRepository.currentLanguage } returns flow

        viewModel = SettingViewModel(userRepository)

        flow.value = "vi"

        advanceUntilIdle()

        assertEquals("vi", viewModel.uiState.value.currentLanguage)
    }

    @Test
    fun `handleEvent UpdateLanguage should call repository and update state`() = runTest {
        val language = "fr"

        coEvery { userRepository.setLanguage(language) } just Runs

        viewModel.handleEvent(SettingEvent.UpdateLanguage(language))

        advanceUntilIdle()

        coVerify { userRepository.setLanguage(language) }
        assertEquals("fr", viewModel.uiState.value.currentLanguage)
    }
}
