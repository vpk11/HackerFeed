package com.vpk.hackerfeed.integration

import com.vpk.hackerfeed.domain.repository.NewsRepository
import com.vpk.hackerfeed.domain.usecase.ClearCacheUseCase
import com.vpk.hackerfeed.domain.usecase.ClearExpiredCacheUseCase
import com.vpk.hackerfeed.helpers.FakeStringResourceProvider
import com.vpk.hackerfeed.presentation.cache.CacheViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CacheViewModelIntegrationTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var newsRepository: NewsRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        newsRepository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): CacheViewModel {
        return CacheViewModel(
            clearCacheUseCase = ClearCacheUseCase(newsRepository),
            clearExpiredCacheUseCase = ClearExpiredCacheUseCase(newsRepository),
            stringResourceProvider = FakeStringResourceProvider()
        )
    }

    @Test
    fun clearAllCache_success_setsMessage() {
        coEvery { newsRepository.clearCache() } returns Result.success(Unit)

        val viewModel = createViewModel()
        viewModel.clearAllCache()

        assertNotNull(viewModel.uiState.value.message)
        assertNull(viewModel.uiState.value.error)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun clearExpiredCache_success_setsMessage() {
        coEvery { newsRepository.clearExpiredCache() } returns Result.success(Unit)

        val viewModel = createViewModel()
        viewModel.clearExpiredCache()

        assertNotNull(viewModel.uiState.value.message)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun clearAllCache_failure_setsError() {
        coEvery { newsRepository.clearCache() } returns Result.failure(RuntimeException("db error"))

        val viewModel = createViewModel()
        viewModel.clearAllCache()

        assertNotNull(viewModel.uiState.value.error)
        assertNull(viewModel.uiState.value.message)
    }

    @Test
    fun clearExpiredCache_failure_setsError() {
        coEvery { newsRepository.clearExpiredCache() } returns Result.failure(RuntimeException("fail"))

        val viewModel = createViewModel()
        viewModel.clearExpiredCache()

        assertNotNull(viewModel.uiState.value.error)
    }

    @Test
    fun clearMessageOnly_clearsOnlyMessage() {
        coEvery { newsRepository.clearCache() } returns Result.success(Unit)

        val viewModel = createViewModel()
        viewModel.clearAllCache()
        viewModel.clearMessageOnly()

        assertNull(viewModel.uiState.value.message)
    }

    @Test
    fun clearErrorOnly_clearsOnlyError() {
        coEvery { newsRepository.clearCache() } returns Result.failure(RuntimeException("fail"))

        val viewModel = createViewModel()
        viewModel.clearAllCache()
        viewModel.clearErrorOnly()

        assertNull(viewModel.uiState.value.error)
    }
}
